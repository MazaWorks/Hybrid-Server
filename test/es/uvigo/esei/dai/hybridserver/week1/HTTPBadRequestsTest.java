/**
 *  HybridServer
 *  Copyright (C) 2018 Miguel Reboiro-Jato
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.uvigo.esei.dai.hybridserver.week1;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;

@RunWith(Parameterized.class)
public class HTTPBadRequestsTest {
	private String requestText;
	
	public HTTPBadRequestsTest(String testName, String requestText) {
		this.requestText = requestText;
	}
	
	@Parameters(name = "{0}")
	public static Collection<String[]> parameters() {
		return asList(
			new String[] {
				"Missing method", 
				"/hello HTTP/1.1\r\n" +
				"Host: localhost\r\n" +
				"Accept: text/html\r\n" +
				"Accept-Encoding: gzip,deflate\r\n"
			},
			new String[] {
				"Missing resource", 
				"GET HTTP/1.1\r\n" +
				"Host: localhost\r\n" +
				"Accept: text/html\r\n" +
				"Accept-Encoding: gzip,deflate\r\n"
			},
			new String[] {
				"Missing version", 
				"GET /hello\r\n" +
				"Host: localhost\r\n" +
				"Accept: text/html\r\n" +
				"Accept-Encoding: gzip,deflate\r\n"
			},
			new String[] {
				"Missing first line",
				"Host: localhost\r\n" +
				"Accept: text/html\r\n" +
				"Accept-Encoding: gzip,deflate\r\n"
			},
			new String[] {
				"Invalid header",
				"GET /hello/world.html?country=Spain&province=Ourense&city=Ourense HTTP/1.1\r\n" +
				"Host\r\n" +
				"Accept: text/html\r\n" +
				"Accept-Encoding: gzip,deflate\r\n"
			},
			new String[] {
				"Missing new line after header",
				"GET /hello/world.html?country=Spain&province=Ourense&city=Ourense HTTP/1.1\r\n" +
				"Host\r\n" +
				"Accept: text/html\r\n" +
				"Accept-Encoding: gzip,deflate\r\n"
			}
		);
	}
	
	@Test(expected = HTTPParseException.class)
	public void test() throws IOException, HTTPParseException {
		new HTTPRequest(new StringReader(requestText));
	}
}
