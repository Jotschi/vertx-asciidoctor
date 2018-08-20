package de.jotschi.vertx.asciidoctor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.asciidoctor.ast.Document;

import io.github.azagniotov.matcher.AntPathMatcher;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class AsciiDoctorCollectionsHandler extends AbstractAsciidoctorHandler {

	private final String sourceDir;
	private final String pattern;
	private final static AntPathMatcher d = new AntPathMatcher.Builder().build();

	private Comparator<DocumentEntry> docCreationDateComparator = (e1, e2) -> {
		String created1 = (String) e1.doc.getAttr("created");
		String created2 = (String) e2.doc.getAttr("created");
		return -1;
	};

	public AsciiDoctorCollectionsHandler(String sourceDir, String pattern) {
		this.sourceDir = sourceDir;
		this.pattern = pattern;

	}

	@Override
	public void handle(RoutingContext rc) {
		Map<String, JsonArray> collectionsMap = new HashMap<>();
		try {
			Files.walk(Paths.get(sourceDir))
				.filter(Files::isRegularFile)
				.filter(f -> f.getFileName().toString().endsWith(".adoc"))
				.filter(f -> d.isMatch(pattern, f.toString()))
				.map(f -> {
					Document doc = doctor.loadFile(f.toFile(), getOptions());
					return new DocumentEntry(f, doc);
				})
				.filter(e -> e.doc.getAttr("collections") != null)
				.sorted(docCreationDateComparator)
				.forEach(e -> {
					String name = (String) e.doc.getAttr("collections");
					JsonArray array = collectionsMap.computeIfAbsent(name, key -> {
						return new JsonArray();
					});
					JsonObject element = toJsonObject(e.doc);
					String path = e.path.toString();
					System.out.println(path);
					path = path.replaceAll("\\.adoc", ".html");
					path = path.substring(sourceDir.length() + 1);
					element.put("path", path);
					array.add(element);
				});

			// Now add the individual collections
			JsonObject collections = new JsonObject();
			for (Entry<String, JsonArray> entry : collectionsMap.entrySet()) {
				collections.put(entry.getKey(), entry.getValue());
			}
			rc.put("collections", collections);
			rc.next();
		} catch (Exception e) {
			rc.fail(e);
		}
	}

	public static AsciiDoctorCollectionsHandler create(String sourceDir, String regex) {
		return new AsciiDoctorCollectionsHandler(sourceDir, regex);
	}

	class DocumentEntry {

		public Document doc;
		public Path path;

		public DocumentEntry(Path path, Document doc) {
			this.path = path;
			this.doc = doc;
		}
	}

}
