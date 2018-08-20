package de.jotschi.vertx.asciidoctor;

import java.util.Collections;

import org.mockito.Mockito;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.RouterImpl;
import io.vertx.ext.web.impl.RoutingContextImpl;

public abstract class AbstractHandlerTest {

	public RoutingContext mockContext(String path) {
		HttpServerRequest request = Mockito.mock(HttpServerRequest.class);
		HttpServerResponse response = Mockito.mock(HttpServerResponse.class);
		Mockito.when(request.response()).thenReturn(response);
		Mockito.when(request.path()).thenReturn(path);
		RouterImpl router = Mockito.mock(RouterImpl.class);
		RoutingContext rc = new RoutingContextImpl(null, router, request, Collections.emptySet());
		RoutingContext rcMock = Mockito.spy(rc);
		Mockito.doNothing().when(rcMock).next();
		return rcMock;
	}
}
