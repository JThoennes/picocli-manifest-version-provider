# picocli Manifest Version Provider

Manifest provider for picocli which takes the info from the packaged manifest.

To use build your own provider by extending AbstractManifestVersionProvider:

```
public class MyVersionProvider extends AbstractManifestVersionProvider {

    public MyVersionProvider() {
        super("Project implementation name");
    }

}
```

All it requires is the project implementation name as it appears on the manifest. Usually this is the Maven project name.
