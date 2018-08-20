package de.jotschi.vertx.asciidoctor;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class AsciiDoctorCollectionsHandlerTest extends AbstractHandlerTest {

	@Test
	public void testHandler() {
		AsciiDoctorCollectionsHandler handler = AsciiDoctorCollectionsHandler.create("src/test/resources/blog", "*.adoc");
		RoutingContext rc = mockContext("/test.html");
		handler.handle(rc);
		JsonObject json = new JsonObject(rc.data());
		assertEquals(2, json.getJsonObject("collections").getJsonArray("dummy").size());
		assertEquals(1, json.getJsonObject("collections").getJsonArray("dummy2").size());
	}

}
