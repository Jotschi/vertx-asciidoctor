# Vert.x Asciidoctor Handler

This project contains asciidoctor handlers which can be used to process asciidoctor files on the fly and add them to the routing context so that the rendered content can be used in combination with a handler and `TemplateEngine`.

## Usage

Example usage:

```
	router.getWithRegex("/(.*)").handler(CacheHandler.create(cache));
	router.get("/Blog/").handler(AsciiDoctorCollectionsHandler.create("site", "**/Blog/*.adoc"));
	router.getWithRegex("/(.*)").handler(AsciiDoctorHandler.create(vertx, "site"));
	router.getWithRegex("/(.*)").handler(DynamicTemplateHandler.create(vertx, cache));
```

### AsciiDoctorHandler

The `AsciiDoctorHandler` tries to map request paths to asciidoc documents in the filesystem.

Examples:

* `GET /`  -> `site/index.adoc`
* `GET /index.html`  -> `site/index.adoc`
* `GET /Blog/`  -> `site/Blog/index.adoc`

The found document will be rendered and added to the render context via the `page` field.

### AsciiDoctorCollectionsHandler

The `AsciiDoctorCollectionsHandler` will parse all found documents which match the pattern and add them into different collections.

You can control to which collection a document will be added by marking each document using the `collections` variable

Example Asciidoctor document:
```
:collections: post

== My Heading

* A
* B
```

The final collection will be added to the render context and thus can be used within the template engine.

## TODO

* Caching
* Maven Central Deployment
* Tests