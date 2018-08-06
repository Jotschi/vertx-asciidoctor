package de.jotschi.vertx.asciidoctor;

import static org.asciidoctor.AttributesBuilder.attributes;
import static org.asciidoctor.OptionsBuilder.options;

import java.util.Map;
import java.util.Map.Entry;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Placement;
import org.asciidoctor.ast.Document;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public abstract class AbstractAsciidoctorHandler implements Handler<RoutingContext> {

	protected final static Asciidoctor doctor = Asciidoctor.Factory.create();

	/**
	 * Convert the {@link Document} into a {@link JsonObject}
	 * 
	 * @param doc
	 * @return
	 */
	public JsonObject toJsonObject(Document doc) {
		JsonObject obj = new JsonObject();
		obj.put("title", doc.getTitle());
		for (Entry<String, Object> entry : doc.getAttributes().entrySet()) {
			obj.put("attr_" + entry.getKey(), entry.getValue());
		}
		obj.put("doctitle", doc.doctitle());
		obj.put("style", doc.getStyle());
		obj.put("level", doc.getLevel());
		obj.put("reftext", doc.getReftext());
		JsonObject a = new JsonObject();
		a.put("test", "1234");
		obj.put("test",a);
		return obj;
	}

	public Map<String, Object> getOptions() {
		Map<String, Object> attributes = attributes()
			.backend("html")
			.docType("book")
			.icons("font")
			.tableOfContents(Placement.LEFT)
			.asMap();

		Map<String, Object> options = options()
			.inPlace(true)
			.attributes(attributes)
			.asMap();
		return options;
	}

}
