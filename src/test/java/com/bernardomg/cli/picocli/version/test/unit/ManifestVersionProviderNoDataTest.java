
package com.bernardomg.cli.picocli.version.test.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bernardomg.cli.picocli.version.test.util.TestManifestVersionProvider;

import picocli.CommandLine.IVersionProvider;

@DisplayName("Manifest version provider without data")
public class ManifestVersionProviderNoDataTest {

    private final IVersionProvider provider = new TestManifestVersionProvider("abc");

    public ManifestVersionProviderNoDataTest() {
        super();
    }

    @Test
    @DisplayName("Returns no version")
    public final void testGetVersion_Size() throws Exception {
        final String[] version;

        version = provider.getVersion();

        Assertions.assertEquals(0, version.length);
    }

}
