/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.equinox.internal.simpleconfigurator;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.eclipse.equinox.internal.provisional.configurator.Configurator;
import org.eclipse.equinox.internal.simpleconfigurator.utils.*;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * SimpleConfigurator provides ways to install bundles listed in a file accessible
 * by the specified URL and expect states for it in advance without actual application.
 * 
 * In every methods of SimpleConfiguration object,
 *  
 * 1. A value will be gotten by @{link BundleContext#getProperty(key) with 
 * {@link SimpleConfiguratorConstants#PROP_KEY_EXCLUSIVE_INSTALLATION} as a key.
 * 2. If it equals "true", it will do exclusive installation, which means that 
 * the bundles will not be listed in the specified url but installed at the time
 * of the method call except SystemBundle will be uninstalled. Otherwise, no uninstallation will not be done.
 */
public class SimpleConfiguratorImpl implements Configurator {

	private static URL configurationURL = null;
	private static Object configurationLock = new Object();

	private BundleContext context;
	private ConfigApplier configApplier;
	private Bundle bundle;

	public SimpleConfiguratorImpl(BundleContext context, Bundle bundle) {
		this.context = context;
		this.bundle = bundle;
	}

	private URL getConfigurationURL() {
		String specifiedURL = context.getProperty(SimpleConfiguratorConstants.PROP_KEY_CONFIGURL);
		if (specifiedURL == null)
			specifiedURL = "file:" + SimpleConfiguratorConstants.CONFIGURATOR_FOLDER + "/" + SimpleConfiguratorConstants.CONFIG_LIST;

		try {
			//If it is not a file URL use it as is
			if (!specifiedURL.startsWith("file:"))
				return new URL(specifiedURL);
		} catch (MalformedURLException e) {
			return null;
		}

		try {
			// if it is an absolute file URL, use it as is
			boolean done = false;
			URL url = null;
			String file = specifiedURL;
			while (!done) {
				try {
					url = new URL(file);
					file = url.getFile();
				} catch (java.net.MalformedURLException e) {
					done = true;
				}
			}
			if (url != null && new File(url.getFile()).isAbsolute())
				return url;

			//if it is an relative file URL, then resolve it against the configuration area
			URL[] configURL = EquinoxUtils.getConfigAreaURL(context);
			if (configURL != null) {
				File target = new File(configURL[0].getFile(), url.getFile());
				if (target.exists())
					return target.toURL();
				else if (configURL.length > 1) {
					target = new File(configURL[1].getFile(), url.getFile());
					if (target.exists())
						return target.toURL();
				}
				return null;
			}
		} catch (MalformedURLException e) {
			return null;
		}

		//Last resort
		try {
			return new URL(specifiedURL);
		} catch (MalformedURLException e) {
			//Ignore
		}

		return null;
	}

	public void applyConfiguration(URL url) throws IOException {
		synchronized (configurationLock) {
			if (Activator.DEBUG)
				System.out.println("applyConfiguration() URL=" + url);
			if (url == null)
				return;
			configurationURL = url;

			List bundleInfoList = SimpleConfiguratorUtils.readConfiguration(url);
			if (Activator.DEBUG)
				System.out.println("applyConfiguration() bundleInfoList.size()=" + bundleInfoList.size());
			if (bundleInfoList.size() == 0)
				return;
			if (this.configApplier == null)
				configApplier = new ConfigApplier(context, bundle);
			configApplier.install(Utils.getBundleInfosFromList(bundleInfoList), url, isExclusiveInstallation());
		}
	}

	private boolean isExclusiveInstallation() {
		String value = context.getProperty(SimpleConfiguratorConstants.PROP_KEY_EXCLUSIVE_INSTALLATION);
		if (value == null || value.trim().length() == 0)
			value = "true";
		return Boolean.valueOf(value).booleanValue();
	}

	public void applyConfiguration() throws IOException {
		synchronized (configurationLock) {
			if (configurationURL == null)
				configurationURL = getConfigurationURL();

			applyConfiguration(configurationURL);
		}
	}

	public URL getUrlInUse() {
		synchronized (configurationLock) {
			return configurationURL;
		}
	}
}
