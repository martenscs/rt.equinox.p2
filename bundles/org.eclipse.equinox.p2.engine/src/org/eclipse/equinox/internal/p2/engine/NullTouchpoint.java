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
package org.eclipse.equinox.internal.p2.engine;

import java.util.Map;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.p2.engine.*;
import org.eclipse.equinox.p2.metadata.TouchpointType;

/**
 * A touchpoint that performs no processing.
 */
public class NullTouchpoint implements ITouchpoint {
	public static final ITouchpoint INSTANCE = new NullTouchpoint();

	/**
	 * Public constructor only intended to be called by extension registry.
	 */
	public NullTouchpoint() {
		super();
	}

	public TouchpointType getTouchpointType() {
		return TouchpointType.NONE;
	}

	public boolean supports(String phaseId) {
		if (phaseId.equals("install") || phaseId.equals("uninstall"))
			return true;
		return false;
	}

	public ITouchpointAction[] getActions(String phaseID, Profile profile, Operand operand) {
		return new ITouchpointAction[] {};
	}

	public IStatus completePhase(IProgressMonitor monitor, Profile profile, String phaseId, Map touchpointParameters) {
		return null;
	}

	public IStatus initializePhase(IProgressMonitor monitor, Profile profile, String phaseId, Map touchpointParameters) {
		return null;
	}
}
