package es.uvigo.esei.dai.hybridserver.week1;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;

public class HTTPResponseNoContentTest {
	private HTTPResponse response;
	
	@Before
	public void setUp() throws Exception {
		this.response = new HTTPResponse();
		
		this.response.setStatus(HTTPResponseStatus.S200);
		this.response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
	}

	@Test
	public final void testPrint() throws IOException {
		try (final StringWriter writer = new StringWriter()) {
			this.response.print(writer);
			
			assertThat(writer.toString(), is(equalTo("HTTP/1.1 200 OK\r\n\r\n")));
		}
	}
}
