package de.jotschi.vertx.asciidoctor;

import java.util.Map.Entry;

import org.asciidoctor.ast.Document;

import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

public class AsciiDoctorHandler extends AbstractAsciidoctorHandler {

	private static final Logger log = LoggerFactory.getLogger(AsciiDoctorHandler.class);

	private final FileSystem fs;
	private final String sourceDir;

	public AsciiDoctorHandler(Vertx vertx, String sourceDir) {
		this.fs = vertx.fileSystem();
		this.sourceDir = sourceDir;
	}

	@Override
	public void handle(RoutingContext rc) {
		String path = rc.request().path();
		String docPath = sourceDir + path.replaceAll("\\.html", ".adoc");
		if (docPath.endsWith("/")) {
			docPath += "index.adoc";
		}
		String convertedPath = docPath;
		log.info("Checking path {" + path + "} -> {" + convertedPath + "}");
		fs.exists(convertedPath, fe -> {
			if (fe.result()) {
				log.info("Found: " + convertedPath);
				fs.readFile(convertedPath, fh -> {
					String source = fh.result().toString();
					Document doc = doctor.load(source, getOptions());
					// Add the adoc variables to the handlebar context
					for (Entry<String, Object> entry : doc.getAttributes().entrySet()) {
						rc.put(entry.getKey(), entry.getValue());
					}
					String templateInfo = (String) doc.getAttr("template", "main");
					rc.put("tmplName", templateInfo);
					String rendered = doc.convert();
					JsonObject json = toJsonObject(doc);
					json.put("content", rendered);
					rc.put("page", json);
					rc.next();
				});
			} else {
				rc.put("tmplName", "404");
				rc.next();
			}
		});

	}

	public static AsciiDoctorHandler create(Vertx vertx, String sourceDir) {
		return new AsciiDoctorHandler(vertx, sourceDir);
	}

}
