package es.uvigo.esei.dai.hybridserver.week1;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsArrayContainingInOrder.arrayContaining;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.collection.IsMapWithSize.aMapWithSize;
import static org.hamcrest.collection.IsMapWithSize.anEmptyMap;
import static org.junit.Assert.assertThat;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequestMethod;

public class HTTPRequestGETResourceTest {
	private String requestText;
	private HTTPRequest request;
	
	@Before
	public void setUp() throws Exception {
		this.requestText = "GET /hello HTTP/1.1\r\n" +
			"Host: localhost\r\n" +
			"Accept: text/html\r\n" +
			"Accept-Encoding: gzip,deflate\r\n";
		
		this.request = new HTTPRequest(new StringReader(
			this.requestText + "\r\n"
		));
	}

	@Test
	public final void testGetMethod() {
		assertThat(request.getMethod(), is(equalTo(HTTPRequestMethod.GET)));
	}

	@Test
	public final void testGetResourceChain() {
		assertThat(request.getResourceChain(), is(equalTo("/hello")));
	}

	@Test
	public final void testGetResourcePath() {
		assertThat(request.getResourcePath(), is(arrayContaining("hello")));
	}

	@Test
	public final void testGetResourceName() {
		assertThat(request.getResourceName(), is(equalTo("hello")));
	}

	@Test
	public final void testGetHttpVersion() {
		assertThat(request.getHttpVersion(), is(equalTo(HTTPHeaders.HTTP_1_1.getHeader())));
	}

	@Test
	public final void testGetResourceParameters() {
		assertThat(this.request.getResourceParameters(), is(anEmptyMap()));
	}

	@Test
	public final void testGetHeaderParameters() {
		assertThat(request.getHeaderParameters(), allOf(
			hasEntry("Host", "localhost"),
			hasEntry("Accept", "text/html"),
			hasEntry("Accept-Encoding", "gzip,deflate")
		));
		assertThat(request.getHeaderParameters(), is(aMapWithSize(3)));
	}

	@Test
	public final void testGetContent() {
		assertThat(request.getContent(), is(nullValue()));
	}

	@Test
	public final void testGetContentLength() {
		assertThat(request.getContentLength(), is(equalTo(0)));
	}

	@Test
	public final void testToString() {
		assertThat(request.toString(), is(equalTo(requestText)));
	}

}
