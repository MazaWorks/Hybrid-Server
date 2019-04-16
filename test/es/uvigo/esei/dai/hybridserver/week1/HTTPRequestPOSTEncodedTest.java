package es.uvigo.esei.dai.hybridserver.week1;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsArrayWithSize.emptyArray;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.collection.IsMapWithSize.aMapWithSize;
import static org.hamcrest.text.IsEmptyString.emptyString;
import static org.junit.Assert.assertThat;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequestMethod;

public class HTTPRequestPOSTEncodedTest {
	private String requestText;
	private String encodedRequestText;
	private HTTPRequest request;
	
	@Before
	public void setUp() throws Exception {
		this.requestText = "POST / HTTP/1.1\r\n" +
			"Host: localhost\r\n" +
			"Content-Type: application/x-www-form-urlencoded\r\n" + 
			"Content-Length: 116\r\n\r\n" +
			"message=Hello world!!&mensaje=¡¡Hola mundo!!&mensaxe=Ola mundo!!&mensagem=Olá mundo!!";
		
		this.encodedRequestText = "POST / HTTP/1.1\r\n" +
			"Host: localhost\r\n" +
			"Content-Type: application/x-www-form-urlencoded\r\n" + 
			"Content-Length: 116\r\n\r\n" +
			"message=Hello+world%21%21&mensaje=%C2%A1%C2%A1Hola+mundo%21%21&mensaxe=Ola+mundo%21%21&mensagem=Ol%C3%A1+mundo%21%21";
		
		this.request = new HTTPRequest(new StringReader(this.encodedRequestText));
	}

	@Test
	public final void testGetMethod() {
		assertThat(request.getMethod(), is(equalTo(HTTPRequestMethod.POST)));
	}

	@Test
	public final void testGetResourceChain() {
		assertThat(request.getResourceChain(), is(equalTo("/")));
	}

	@Test
	public final void testGetResourcePath() {
		assertThat(request.getResourcePath(), is(emptyArray()));
	}

	@Test
	public final void testGetResourceName() {
		assertThat(request.getResourceName(), is(emptyString()));
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
			hasEntry("Content-Type", "application/x-www-form-urlencoded"),
			hasEntry("Content-Length", "116")
		));
		assertThat(request.getHeaderParameters(), is(aMapWithSize(3)));
	}

	@Test
	public final void testGetContent() {
		assertThat(request.getContent(), is(equalTo("message=Hello world!!&mensaje=¡¡Hola mundo!!&mensaxe=Ola mundo!!&mensagem=Olá mundo!!")));
	}

	@Test
	public final void testGetContentLength() {
		assertThat(request.getContentLength(), is(equalTo(116)));
	}

	@Test
	public final void testToString() {
		assertThat(request.toString(), is(equalTo(requestText)));
	}

}
