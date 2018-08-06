package de.jotschi.vertx.asciidoctor;

public final class PathUtil {

	private PathUtil() {
	}

	public static String normalizePath(String basedir, String path) {

		path = path.replaceAll("\\.html", ".adoc");
		if (path.endsWith("/")) {
			path += "index.adoc";
		}
		if (!path.endsWith(".adoc")) {
			return normalizePath(basedir, path + "/index.adoc");
		}
		return basedir + path;
	}

}
