package de.jotschi.vertx.asciidoctor;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class AsciiDoctorHandlerTest extends AbstractHandlerTest {

	@Test
	public void testHandler() {
		AsciiDoctorHandler handler = AsciiDoctorHandler.create(Vertx.vertx(), "src/test/resources/blog");
		RoutingContext rc = mockContext("/blog-post-a.html");
		handler.handle(rc);
		JsonObject json = new JsonObject(rc.data());
		System.out.println(json.encodePrettily());
		assertNotNull(json.getJsonObject("page").getString("content"));

	}

}
