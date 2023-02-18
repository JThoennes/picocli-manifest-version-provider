# picocli Manifest Version Provider

Manifest provider for [picocli](https://picocli.info/) which takes the info from the packaged manifest.

To use build your own provider by extending AbstractManifestVersionProvider:

```
public class MyVersionProvider extends AbstractManifestVersionProvider {

    public MyVersionProvider() {
        super("Project implementation name");
    }

}
```

All it requires is the project implementation name as it appears on the manifest. Usually this is the Maven project name.

Then it can be added to any picocli command:

```
@Command(description = "Menu command" versionProvider = ManifestVersionProvider.class)
public class Menu
```
