
package com.bernardomg.cli.picocli.version.test.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bernardomg.cli.picocli.version.test.util.TestManifestVersionProvider;

import picocli.CommandLine.IVersionProvider;

@DisplayName("Manifest version provider")
public class ManifestVersionProviderTest {

    private final IVersionProvider provider = new TestManifestVersionProvider("Test App Impl");

    public ManifestVersionProviderTest() {
        super();
    }

    @Test
    @DisplayName("Returns all the data")
    public final void testGetVersion_Data() throws Exception {
        final String[] version;

        version = provider.getVersion();

        Assertions.assertEquals("Test App Impl version 1.0.0", version[0]);
    }

    @Test
    @DisplayName("Returns a single version line")
    public final void testGetVersion_Size() throws Exception {
        final String[] version;

        version = provider.getVersion();

        Assertions.assertEquals(1, version.length);
    }

}
