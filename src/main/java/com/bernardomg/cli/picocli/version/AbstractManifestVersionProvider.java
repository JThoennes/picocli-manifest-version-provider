/**
 * Copyright 2020-2023 the original author or authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.bernardomg.cli.picocli.version;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import picocli.CommandLine.IVersionProvider;

/**
 * Version provider based on the JAR manifest. All it requires is the name of the project, so it can find the correct
 * manifest file.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public abstract class AbstractManifestVersionProvider implements IVersionProvider {

    /**
     * Manifest implementation title key.
     */
    private static final Attributes.Name KEY_TITLE     = new Attributes.Name("Implementation-Title");

    /**
     * Manifest implementation vesion key.
     */
    private static final Attributes.Name KEY_VERSION   = new Attributes.Name("Implementation-Version");

    /**
     * Logger for the class.
     */
    private static final Logger          log           = LoggerFactory.getLogger(AbstractManifestVersionProvider.class);

    /**
     * Path to the manifest file.
     */
    private static final String          MANIFEST_PATH = "META-INF/MANIFEST.MF";

    /**
     * Project title. Used to identify the correct manifest.
     */
    private final String                 project;

    /**
     * Builds a version provider for the specified project.
     *
     * @param prj
     *            project name to match and find the version
     */
    public AbstractManifestVersionProvider(final String prj) {
        super();

        project = Objects.requireNonNull(prj);
    }

    /**
     * Returns the project title. Used to identify the correct manifest.
     *
     * @return the project title for finding the version
     */
    public final String getProject() {
        return project;
    }

    @Override
    public final String[] getVersion() throws Exception {
        final Enumeration<URL> resources;
        final String[]         result;
        Optional<String>       version;

        // Acquire URL to manifest file
        resources = Thread.currentThread()
            .getContextClassLoader()
            .getResources(MANIFEST_PATH);

        // Searches for version
        version = Optional.empty();
        while ((version.isEmpty()) && (resources.hasMoreElements())) {
            final Optional<Manifest> manifest;
            final Attributes         attr;

            // Only a single manifest file should exist
            // So this loop would be executed once

            // Tries to acquire the manifest from the URL
            manifest = getManifest(resources.nextElement());

            if ((manifest.isPresent()) && (isValid(manifest.get()))) {
                attr = manifest.get()
                    .getMainAttributes();

                version = Optional.of(getVersion(attr));
            }
        }

        // Check if the version was found
        // If so, it will be returned
        if (version.isPresent()) {
            log.debug("Found version data");
            log.debug("Version: {}", version.get());
            result = new String[] { version.get() };
        } else {
            log.debug("Found no version data");
            result = new String[0];
        }

        return result;
    }

    /**
     * Returns the JAR manifest info structure.
     *
     * @param url
     *            URL to the manifest file
     * @return the manifest structure
     */
    private Optional<Manifest> getManifest(final URL url) {
        final Manifest     manifest;
        Optional<Manifest> result;

        log.debug("Reading manifest from {}", url);

        try {
            manifest = new Manifest(url.openStream());
            result = Optional.of(manifest);
        } catch (final IOException ex) {
            log.error("Unable to read from {}", url);
            result = Optional.empty();
        }

        return result;
    }

    /**
     * Returns the implementation version from the received attributes.
     * <p>
     * It will try to combine the implementation name and version in a string like:
     * <p>
     * {@code Implementation-Title version Implementation-Version}.
     * <p>
     * If the version is missing, then said part of the string will be also missing from the result.
     *
     * @param attr
     *            attributes with the version
     * @return the implementation version
     */
    private String getVersion(final Attributes attr) {
        final StringBuilder version;

        version = new StringBuilder();

        // Adds implementation title
        version.append(attr.get(KEY_TITLE))
            .append(' ');

        // Adds implementation version if it exists
        if (attr.containsKey(KEY_VERSION)) {
            version.append("version ")
                .append(attr.get(KEY_VERSION));
        }

        return version.toString();
    }

    /**
     * Checks if the manifest is the correct one.
     *
     * @param manifest
     *            manifest to check
     * @return {@code true} if it is the expected manifest, {@code false} in other case
     */
    private boolean isValid(final Manifest manifest) {
        final Attributes attributes;
        final Object     title;
        final boolean    valid;

        attributes = manifest.getMainAttributes();

        if (attributes.containsKey(KEY_TITLE)) {
            title = attributes.get(KEY_TITLE);
            valid = project.equals(title);
        } else {
            valid = false;
        }

        return valid;
    }

}
