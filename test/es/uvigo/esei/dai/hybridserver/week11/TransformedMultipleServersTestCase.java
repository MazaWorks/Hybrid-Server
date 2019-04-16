/**
 *  HybridServer
 *  Copyright (C) 2017 Miguel Reboiro-Jato
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
package es.uvigo.esei.dai.hybridserver.week11;

import static es.uvigo.esei.dai.hybridserver.utils.TestUtils.getContentWithType;
import static es.uvigo.esei.dai.hybridserver.utils.TestUtils.getStatus;
import static es.uvigo.esei.dai.hybridserver.utils.matchers.EqualsToIgnoringSpacesAndCaseMatcher.equalsToIgnoringSpacesAndCase;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import es.uvigo.esei.dai.hybridserver.http.MIME;

@RunWith(Parameterized.class)
public class TransformedMultipleServersTestCase
extends MultipleServersTestCase {
	private static final String[] CT_XMLS = new String[] {
		"ddcab7d0-636c-1111-8db3-685b35c84fb4",
		"ddcab7d0-636c-2222-8db3-685b35c84fb4",
		"ea118888-6908-3333-9620-685b35c84fb4",
		"ea118888-6908-4444-9620-685b35c84fb4"
	};
	private static final String[] CT_XSLTS = new String[] {
		"f260dfee-636c-3333-1111-685b35c84fb4",
		"f260dfee-636c-4444-2222-685b35c84fb4",
		"1fd26c94-6909-2222-3333-685b35c84fb4",
		"1fd26c94-6909-1111-4444-685b35c84fb4"
	};
	private static final String[] CT_INVALID_XSLTS = new String[] {
		"1fd26c94-6909-2222-9a75-685b35c84fb4",
		"1fd26c94-6909-1111-9a75-685b35c84fb4",
		"f260dfee-636c-4444-bbdd-685b35c84fb4",
		"f260dfee-636c-3333-bbdd-685b35c84fb4"
	};
	private static final String[] CT_RESULTS = new String[] {
		"files/sample1_3_1.html",
		"files/sample1_4_2.html",
		"files/sample2_2_3.html",
		"files/sample2_1_4.html"
	};
	
	private final int serverIndex;
	
	public TransformedMultipleServersTestCase(
		String serverName, int serverIndex
	) {
		this.serverIndex = serverIndex;
	}

	@Test
	public final void testGetLocales() throws IOException {
		final String[] xmls = getLocalXmlUUIDs(this.serverIndex);
		final String[] xslts = getLocalXsltUUIDs(this.serverIndex);
		final String[] contents = getLocalTransformedFiles(this.serverIndex);
		
		testMultipleGets(xmls, xslts, contents);
	}

	@Test
	public final void testGetRemotes() throws IOException {
		final String[] xmls = getRemoteXmlUUIDs(this.serverIndex);
		final String[] xslts = getRemoteXsltUUIDs(this.serverIndex);
		final String[] contents = getRemoteTransformedFiles(this.serverIndex);
		
		testMultipleGets(xmls, xslts, contents);
	}

	@Test
	public final void testCrossTransformations() throws IOException {
		final String[] xmls = CT_XMLS;
		final String[] xslts = CT_XSLTS;
		final String[] contents = getFileContents(CT_RESULTS);
		
		testMultipleGets(xmls, xslts, contents);
	}

	@Test
	public final void testBadXmlTransformations() throws IOException {
		final String[] xslts = getAllXsltUUIDs();
		final String[] xmls = generateInvalidUUIDs(xslts.length);
		
		testMultipleErrorGets(xmls, xslts, 404);
	}

	@Test
	public final void testBadXsltTransformations() throws IOException {
		final String[] xmls = getAllXmlUUIDs();
		final String[] xslts = generateInvalidUUIDs(xmls.length);
		
		testMultipleErrorGets(xmls, xslts, 404);
	}
	
	@Test
	public final void testInvalidXsltTransformations() throws IOException {
		final String[] xmls = CT_XMLS;
		final String[] xslts = CT_INVALID_XSLTS;
		
		testMultipleErrorGets(xmls, xslts, 400);
	}

	protected void testMultipleGets(
		final String[] xmls, final String[] xslts, final String[] expectedContents
	) throws IOException {
		for (int i = 0; i < xmls.length; i++) {
			final String xmlUuid = xmls[i];
			final String xsltUuid = xslts[i];
			final String expectedContent = expectedContents[i];
			
			final String url = getResourceURL(xmlUuid, xsltUuid);
			final String content = getContentWithType(url, MIME.TEXT_HTML.getMime());
			
			assertThat(expectedContent, is(equalsToIgnoringSpacesAndCase(content)));
		}
	}

	protected void testMultipleErrorGets(
		final String[] xmls, final String[] xslts, final int expectedError
	) throws IOException {
		for (int i = 0; i < xmls.length; i++) {
			final String xmlUuid = xmls[i];
			final String xsltUuid = xslts[i];
			
			final String url = getResourceURL(xmlUuid, xsltUuid);

			assertThat(getStatus(url), is(equalTo(expectedError)));
		}
	}
	
	protected String getResourceURL(String xmlUuid, String xsltUuid) {
		return String.format("%s/xml?uuid=%s&xslt=%s",
			serversHTTPURL[this.serverIndex], xmlUuid, xsltUuid
		);
	}
}
