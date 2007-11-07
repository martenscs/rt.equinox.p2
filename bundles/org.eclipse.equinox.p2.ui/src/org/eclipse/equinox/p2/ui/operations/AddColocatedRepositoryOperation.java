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
package org.eclipse.equinox.p2.ui.operations;

import java.net.URL;
import org.eclipse.core.runtime.*;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.core.repository.IRepository;

/**
 * Operation that adds colocated artifact and metadata repositories
 * given a URL.
 * 
 * @since 3.4
 */
public class AddColocatedRepositoryOperation extends RepositoryOperation {

	boolean added = false;

	public AddColocatedRepositoryOperation(String label, URL url) {
		super(label, new URL[] {url});
	}

	protected IStatus doExecute(IProgressMonitor monitor, IAdaptable uiInfo) throws ProvisionException {
		for (int i = 0; i < urls.length; i++) {
			IRepository repo = ProvisioningUtil.addMetadataRepository(urls[i], monitor);
			if (repo == null) {
				return failureStatus();
			}
			repo = ProvisioningUtil.addArtifactRepository(urls[i], monitor);
			if (repo == null) {
				// remove the metadata repo we just added
				ProvisioningUtil.removeMetadataRepository(urls[i], monitor);
				return failureStatus();
			}
		}
		added = true;
		return okStatus();
	}

	protected IStatus doUndo(IProgressMonitor monitor, IAdaptable uiInfo) throws ProvisionException {
		for (int i = 0; i < urls.length; i++) {
			ProvisioningUtil.removeMetadataRepository(urls[i], monitor);
			ProvisioningUtil.removeArtifactRepository(urls[i], monitor);
		}
		added = false;
		return okStatus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.operations.AbstractOperation#canExecute()
	 */
	public boolean canExecute() {
		return super.canExecute() && !added;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.operations.AbstractOperation#canUndo()
	 */
	public boolean canUndo() {
		return super.canUndo() && added;
	}
}
