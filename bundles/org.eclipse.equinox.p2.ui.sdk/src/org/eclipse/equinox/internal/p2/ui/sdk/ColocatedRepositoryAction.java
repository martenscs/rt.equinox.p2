/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.equinox.internal.p2.ui.sdk;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.equinox.internal.provisional.p2.ui.ProvisioningOperationRunner;
import org.eclipse.equinox.internal.provisional.p2.ui.actions.ProvisioningAction;
import org.eclipse.equinox.internal.provisional.p2.ui.model.MetadataRepositoryElement;
import org.eclipse.equinox.internal.provisional.p2.ui.operations.ProvisioningOperation;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.statushandlers.StatusManager;

public abstract class ColocatedRepositoryAction extends ProvisioningAction {

	public ColocatedRepositoryAction(String label, String tooltipText, ISelectionProvider selectionProvider, Shell shell) {
		super(label, selectionProvider, shell);
		setToolTipText(tooltipText);
		init();
	}

	public void run() {
		ProvisioningOperationRunner.schedule(getOperation(), getShell(), StatusManager.SHOW | StatusManager.LOG);
	}

	protected abstract ProvisioningOperation getOperation();

	protected URL[] getSelectedURLs(Object[] selectionArray) {
		List urls = new ArrayList();
		for (int i = 0; i < selectionArray.length; i++) {
			if (selectionArray[i] instanceof MetadataRepositoryElement)
				urls.add(((MetadataRepositoryElement) selectionArray[i]).getLocation());
		}
		return (URL[]) urls.toArray(new URL[urls.size()]);
	}

	protected void checkEnablement(Object[] selectionArray) {
		setEnabled(getSelectedURLs(selectionArray).length > 0);
	}
}
