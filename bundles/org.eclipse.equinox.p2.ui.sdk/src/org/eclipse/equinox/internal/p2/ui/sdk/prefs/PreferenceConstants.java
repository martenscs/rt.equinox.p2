/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.equinox.internal.p2.ui.sdk.prefs;

/**
 * @since 3.4
 */
public class PreferenceConstants {
	public static final String PREF_PAGE_PROVISIONING = "org.eclipse.equinox.internal.p2.ui.sdk.ProvisioningPreferencePage"; //$NON-NLS-1$
	public static final String PREF_PAGE_AUTO_UPDATES = "org.eclipse.equinox.internal.p2.ui.sdk.AutomaticUpdatesPreferencePage"; //$NON-NLS-1$
	public static final String PREF_AUTO_UPDATE_ENABLED = "enabled"; //$NON-NLS-1$
	public static final String PREF_AUTO_UPDATE_SCHEDULE = "schedule"; //$NON-NLS-1$
	public static final String PREF_UPDATE_ON_STARTUP = "on-startup"; //$NON-NLS-1$
	public static final String PREF_UPDATE_ON_SCHEDULE = "on-schedule"; //$NON-NLS-1$  // string value defined in AutomaticUpdateScheduler 
	public static final String PREF_DOWNLOAD_ONLY = "download"; // value is true or false, default is false //$NON-NLS-1$
	public static final String PREF_REMIND_SCHEDULE = "remindOnSchedule"; // value is true or false //$NON-NLS-1$
	public static final String PREF_REMIND_ELAPSED = "remindElapsedTime";//$NON-NLS-1$
	public static final String PREF_SHOW_LATEST_VERSION = "showLatestVersion"; //$NON-NLS-1$
	public static final String PREF_OPEN_WIZARD_ON_NONOK_PLAN = "allowNonOKPlan"; //$NON-NLS-1$
	public static final String PREF_GENERATE_REPOFOLDER = "generateReposForFolder"; //$NON-NLS-1$
	public static final String PREF_GENERATE_ARCHIVEREPOFOLDER = "generateReposForArchive"; //$NON-NLS-1$
	public static final String PREF_AUTO_INSTALL_BUNDLES = "autoInstallBundles"; //$NON-NLS-1$
}
