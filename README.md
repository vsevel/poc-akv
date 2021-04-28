# Quarkus - Azure Key Vault

Provides an Azure Key Vault Microprofile config source extension.

## Getting Started

To get started, add the following dependency:

```
<dependency>
    <groupId>io.quarkiverse.quarkus-azure-key-vault</groupId>
    <artifactId>quarkus-azure-key-vault</artifactId>
</dependency>
```

Configure the following properties:

```
quarkus.azure-key-vault.enabled=true
quarkus.azure-key-vault.url=https://myvault.vault.azure.net
quarkus.azure-key-vault.tls.skip-verify=true

quarkus.azure-key-vault.tenant-id=65365722-...
quarkus.azure-key-vault.client-id=1462f469-...
quarkus.azure-key-vault.client-secret=l.5P...

quarkus.azure-key-vault.prefix=myprops
```

Assuming, `myvault` contains secret `mysecret=s3cr3t`, you can now code:

```
@ApplicationScoped
public class SomeClass {

    @ConfigProperty(name = "myprops.mysecret")
    String secret;
...
}
```

Instance variable `secret` will contain `s3cr3t`.

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://github.com/vsevel"><img src="https://avatars3.githubusercontent.com/u/6041620?v=4" width="100px;" alt=""/><br /><sub><b>Vincent Sevel</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkiverse-freemarker/commits?author=vsevel" title="Code">💻</a> <a href="#maintenance-vsevel" title="Maintenance">🚧</a></td>
  </tr>
</table>

<!-- markdownlint-enable -->
<!-- prettier-ignore-end -->
<!-- ALL-CONTRIBUTORS-LIST:END -->

