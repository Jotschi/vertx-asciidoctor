package de.jotschi.vertx.asciidoctor;

import static de.jotschi.vertx.asciidoctor.PathUtil.normalizePath;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PathUtilTest {

	@Test
	public void testNormalizePath() {
		assertEquals("site/Blog/index.adoc", normalizePath("site", "/Blog/index.html"));
		assertEquals("site/Blog/index.adoc", normalizePath("site", "/Blog/"));
		assertEquals("site/test.adoc", normalizePath("site", "/test.html"));
		assertEquals("site/Blog/index.adoc", normalizePath("site", "/Blog"));
	}
}
