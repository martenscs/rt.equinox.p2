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
package org.eclipse.equinox.internal.p2.ui.sdk.prefs;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.equinox.internal.p2.ui.sdk.ProvSDKMessages;
import org.eclipse.equinox.internal.p2.ui.sdk.ProvSDKUIActivator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference page for general provisioning preferences.
 * 
 * @since 3.4
 */

public class ProvisioningPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private Group browsingGroup, validateGroup;
	private Button showLatestRadio, showAllRadio;
	private Button alwaysShowFailedPlan, neverShowFailedPlan, promptOnFailedPlan;

	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		container.setLayout(layout);

		// Group for show all versions vs. show latest
		browsingGroup = new Group(container, SWT.NONE);
		browsingGroup.setText(ProvSDKMessages.ProvisioningPreferencePage_BrowsingPrefsGroup);
		layout = new GridLayout();
		layout.numColumns = 3;
		browsingGroup.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		browsingGroup.setLayoutData(gd);

		showLatestRadio = new Button(browsingGroup, SWT.RADIO);
		showLatestRadio.setText(ProvSDKMessages.ProvisioningPreferencePage_ShowLatestVersions);
		gd = new GridData();
		gd.horizontalSpan = 3;
		showLatestRadio.setLayoutData(gd);

		showAllRadio = new Button(browsingGroup, SWT.RADIO);
		showAllRadio.setText(ProvSDKMessages.ProvisioningPreferencePage_ShowAllVersions);
		gd = new GridData();
		gd.horizontalSpan = 3;
		showAllRadio.setLayoutData(gd);

		//Group for validating a failed plan
		validateGroup = new Group(container, SWT.NONE);
		validateGroup.setText(ProvSDKMessages.ProvisioningPreferencePage_OpenWizardIfInvalid);
		layout = new GridLayout();
		layout.numColumns = 3;
		validateGroup.setLayout(layout);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		validateGroup.setLayoutData(gd);

		alwaysShowFailedPlan = new Button(validateGroup, SWT.RADIO);
		alwaysShowFailedPlan.setText(ProvSDKMessages.ProvisioningPreferencePage_AlwaysOpenWizard);
		gd = new GridData();
		gd.horizontalSpan = 3;
		alwaysShowFailedPlan.setLayoutData(gd);

		neverShowFailedPlan = new Button(validateGroup, SWT.RADIO);
		neverShowFailedPlan.setText(ProvSDKMessages.ProvisioningPreferencePage_NeverOpenWizard);
		gd = new GridData();
		gd.horizontalSpan = 3;
		neverShowFailedPlan.setLayoutData(gd);

		promptOnFailedPlan = new Button(validateGroup, SWT.RADIO);
		promptOnFailedPlan.setText(ProvSDKMessages.ProvisioningPreferencePage_PromptToOpenWizard);
		gd = new GridData();
		gd.horizontalSpan = 3;
		promptOnFailedPlan.setLayoutData(gd);

		initialize();

		Dialog.applyDialogFont(container);
		return container;

	}

	private void initialize() {
		Preferences pref = ProvSDKUIActivator.getDefault().getPluginPreferences();
		showLatestRadio.setSelection(pref.getBoolean(PreferenceConstants.PREF_SHOW_LATEST_VERSION));
		showAllRadio.setSelection(!pref.getBoolean(PreferenceConstants.PREF_SHOW_LATEST_VERSION));
		String openWizard = pref.getString(PreferenceConstants.PREF_OPEN_WIZARD_ON_NONOK_PLAN);
		alwaysShowFailedPlan.setSelection(openWizard.equals(MessageDialogWithToggle.ALWAYS));
		neverShowFailedPlan.setSelection(openWizard.equals(MessageDialogWithToggle.NEVER));
		promptOnFailedPlan.setSelection(openWizard.equals(MessageDialogWithToggle.PROMPT));
	}

	protected void performDefaults() {
		super.performDefaults();
		Preferences pref = ProvSDKUIActivator.getDefault().getPluginPreferences();
		showLatestRadio.setSelection(pref.getDefaultBoolean(PreferenceConstants.PREF_SHOW_LATEST_VERSION));
		showAllRadio.setSelection(!pref.getDefaultBoolean(PreferenceConstants.PREF_SHOW_LATEST_VERSION));
		String openWizard = pref.getDefaultString(PreferenceConstants.PREF_OPEN_WIZARD_ON_NONOK_PLAN);
		alwaysShowFailedPlan.setSelection(openWizard.equals(MessageDialogWithToggle.ALWAYS));
		neverShowFailedPlan.setSelection(openWizard.equals(MessageDialogWithToggle.NEVER));
		promptOnFailedPlan.setSelection(openWizard.equals(MessageDialogWithToggle.PROMPT));
	}

	public boolean performOk() {
		Preferences pref = ProvSDKUIActivator.getDefault().getPluginPreferences();
		pref.setValue(PreferenceConstants.PREF_SHOW_LATEST_VERSION, showLatestRadio.getSelection());
		if (alwaysShowFailedPlan.getSelection())
			pref.setValue(PreferenceConstants.PREF_OPEN_WIZARD_ON_NONOK_PLAN, MessageDialogWithToggle.ALWAYS);
		else if (neverShowFailedPlan.getSelection())
			pref.setValue(PreferenceConstants.PREF_OPEN_WIZARD_ON_NONOK_PLAN, MessageDialogWithToggle.NEVER);
		else
			pref.setValue(PreferenceConstants.PREF_OPEN_WIZARD_ON_NONOK_PLAN, MessageDialogWithToggle.PROMPT);

		ProvSDKUIActivator.getDefault().savePluginPreferences();
		return true;
	}

	public void init(IWorkbench workbench) {
		// Nothing to do
	}

}
