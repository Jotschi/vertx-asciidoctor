package de.jotschi.vertx.asciidoctor;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
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

	private Comparator<Document> docCreationDateComparator = (doc1, doc2) -> {
		String created1 = (String) doc1.getAttr("created");
		String created2 = (String) doc2.getAttr("created");
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
				.map(f -> doctor.loadFile(f.toFile(), getOptions()))
				.filter(doc -> doc.getAttr("collections") != null)
				.sorted(docCreationDateComparator)
				.forEach(doc -> {
					String name = (String) doc.getAttr("collections");
					JsonArray array = collectionsMap.computeIfAbsent(name, key -> {
						return new JsonArray();
					});
					array.add(toJsonObject(doc));
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

}
