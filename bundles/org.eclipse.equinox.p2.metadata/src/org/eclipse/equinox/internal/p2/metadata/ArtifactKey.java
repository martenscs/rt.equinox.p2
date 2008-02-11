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
package org.eclipse.equinox.internal.p2.metadata;

import java.util.ArrayList;
import java.util.StringTokenizer;
import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.internal.provisional.p2.metadata.IArtifactKey;
import org.osgi.framework.Version;

/** 
 * The concrete type for representing IArtifactKey's.
 * <p>
 * See {link IArtifact for a description of the lifecycle of artifact keys) 
 */
public class ArtifactKey implements IArtifactKey {
	private static final String SEPARATOR = ","; //$NON-NLS-1$

	private final String namespace;
	private final String id;
	private final String classifier;
	private final Version version;

	private static String[] getArrayFromList(String stringList, String separator) {
		if (stringList == null || stringList.trim().length() == 0)
			return new String[0];
		ArrayList list = new ArrayList();
		boolean separatorSeen = true;
		StringTokenizer tokens = new StringTokenizer(stringList, separator, true);
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken().trim();
			if (token.equals(separator)) {
				if (separatorSeen)
					list.add("");
				separatorSeen = true;
			} else {
				separatorSeen = false;
				if (token.length() != 0)
					list.add(token);
			}
		}
		if (separatorSeen)
			list.add(""); //$NON-NLS-1$
		return (String[]) list.toArray(new String[list.size()]);
	}

	public static IArtifactKey parse(String specification) {
		String[] parts = getArrayFromList(specification, SEPARATOR);
		if (parts.length < 3 || parts.length > 4)
			throw new IllegalArgumentException("Unexpected number of parts in artifact key: " + specification); //$NON-NLS-1$
		Version version = Version.emptyVersion;
		if (parts.length == 4 && parts[3].trim().length() > 0)
			version = Version.parseVersion(parts[3]);
		try {
			return new ArtifactKey(parts[0], parts[1], parts[2], version);
		} catch (IllegalArgumentException e) {
			throw (IllegalArgumentException) new IllegalArgumentException("Wrong version syntax in artifact key: " + specification).initCause(e); //$NON-NLS-1$
		}
	}

	public ArtifactKey(String namespace, String classifier, String id, Version version) {
		super();
		Assert.isNotNull(namespace);
		Assert.isNotNull(classifier);
		Assert.isNotNull(id);
		Assert.isNotNull(version);
		if (namespace.indexOf(SEPARATOR) != -1)
			throw new IllegalArgumentException("comma not allowed in namespace"); //$NON-NLS-1$
		if (classifier.indexOf(SEPARATOR) != -1)
			throw new IllegalArgumentException("comma not allowed in classifier"); //$NON-NLS-1$
		if (id.indexOf(SEPARATOR) != -1)
			throw new IllegalArgumentException("comma not allowed in id"); //$NON-NLS-1$
		this.namespace = namespace;
		this.classifier = classifier;
		this.id = id;
		this.version = version;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getClassifier() {
		return classifier;
	}

	public Version getVersion() {
		return version;
	}

	public int hashCode() {
		int hash = id.hashCode();
		hash = 17 * hash + getVersion().hashCode();
		hash = 17 * hash + namespace.hashCode();
		hash = 17 * hash + classifier.hashCode();
		return hash;
	}

	public String toString() {
		return id + '/' + namespace + '/' + classifier + '/' + getVersion();
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof IArtifactKey))
			return false;
		IArtifactKey ak = (IArtifactKey) obj;
		return ak.getId().equals(id) && ak.getVersion().equals(getVersion()) && ak.getNamespace().equals(namespace) && ak.getClassifier().equals(classifier);
	}

	public String getId() {
		return id;
	}

	public String toExternalForm() {
		StringBuffer data = new StringBuffer(namespace).append(SEPARATOR);
		data.append(classifier).append(SEPARATOR);
		data.append(id).append(SEPARATOR);
		data.append(version.toString());
		return data.toString();
	}

}
