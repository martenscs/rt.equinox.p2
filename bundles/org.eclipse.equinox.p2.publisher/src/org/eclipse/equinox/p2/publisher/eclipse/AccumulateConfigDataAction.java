/*******************************************************************************
 * Copyright (c) 2008 Code 9 and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   Code 9 - initial API and implementation
 *   IBM - ongoing development
 ******************************************************************************/
package org.eclipse.equinox.p2.publisher.eclipse;

import java.io.File;
import org.eclipse.core.runtime.*;
import org.eclipse.equinox.internal.p2.publisher.eclipse.DataLoader;
import org.eclipse.equinox.internal.provisional.frameworkadmin.ConfigData;
import org.eclipse.equinox.internal.provisional.frameworkadmin.LauncherData;
import org.eclipse.equinox.p2.publisher.*;

public class AccumulateConfigDataAction extends AbstractPublisherAction {

	private String configSpec;
	private DataLoader loader;

	public AccumulateConfigDataAction(IPublisherInfo info, String configSpec, File configurationLocation, File executableLocation) {
		this.configSpec = configSpec;
		loader = new DataLoader(configurationLocation, executableLocation);
	}

	public IStatus perform(IPublisherInfo info, IPublisherResult results, IProgressMonitor monitor) {
		storeConfigData(info, configSpec, results);
		return Status.OK_STATUS;
	}

	protected void storeConfigData(IPublisherInfo info, String configSpec, IPublisherResult result) {
		ConfigData data = loader.getConfigData();
		if (data == null)
			return;
		info.addAdvice(new ConfigAdvice(data, configSpec));
		LauncherData launcherData = loader.getLauncherData();
		if (launcherData == null)
			return;
		info.addAdvice(new LaunchingAdvice(launcherData, configSpec));
	}
}