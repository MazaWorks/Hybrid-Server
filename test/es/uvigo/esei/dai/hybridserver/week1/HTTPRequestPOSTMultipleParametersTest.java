package es.uvigo.esei.dai.hybridserver.week1;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsArrayContainingInOrder.arrayContaining;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.collection.IsMapWithSize.aMapWithSize;
import static org.junit.Assert.assertThat;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequestMethod;

public class HTTPRequestPOSTMultipleParametersTest {
	private String requestText;
	private HTTPRequest request;
	
	@Before
	public void setUp() throws Exception {
		this.requestText = "POST /resource HTTP/1.1\r\n" +
			"Host: localhost\r\n" +
			"Content-Length: 85\r\n\r\n" +
			"message=Hello world!!&mensaje=¡¡Hola mundo!!&mensaxe=Ola mundo!!&mensagem=Olá mundo!!";
		
		this.request = new HTTPRequest(new StringReader(this.requestText));
	}

	@Test
	public final void testGetMethod() {
		assertThat(request.getMethod(), is(equalTo(HTTPRequestMethod.POST)));
	}

	@Test
	public final void testGetResourceChain() {
		assertThat(request.getResourceChain(), is(equalTo("/resource")));
	}

	@Test
	public final void testGetResourcePath() {
		assertThat(request.getResourcePath(), is(arrayContaining("resource")));
	}

	@Test
	public final void testGetResourceName() {
		assertThat(request.getResourceName(), is(equalTo("resource")));
	}

	@Test
	public final void testGetHttpVersion() {
		assertThat(request.getHttpVersion(), is(equalTo(HTTPHeaders.HTTP_1_1.getHeader())));
	}

	@Test
	public final void testGetResourceParameters() {
		assertThat(request.getResourceParameters(), allOf(
			hasEntry("message", "Hello world!!"),
			hasEntry("mensaje", "¡¡Hola mundo!!"),
			hasEntry("mensaxe", "Ola mundo!!"),
			hasEntry("mensagem", "Olá mundo!!")
		));
		assertThat(request.getResourceParameters(), is(aMapWithSize(4)));
	}

	@Test
	public final void testGetHeaderParameters() {
		assertThat(request.getHeaderParameters(), allOf(
			hasEntry("Host", "localhost"),
			hasEntry("Content-Length", "85")
		));
		assertThat(request.getHeaderParameters(), is(aMapWithSize(2)));
	}

	@Test
	public final void testGetContent() {
		assertThat(request.getContent(), is(equalTo("message=Hello world!!&mensaje=¡¡Hola mundo!!&mensaxe=Ola mundo!!&mensagem=Olá mundo!!")));
	}

	@Test
	public final void testGetContentLength() {
		assertThat(request.getContentLength(), is(equalTo(85)));
	}

	@Test
	public final void testToString() {
		assertThat(request.toString(), is(equalTo(requestText)));
	}

}
