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

import static es.uvigo.esei.dai.hybridserver.utils.TestUtils.getContent;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class WelcomeAndListsMultipleServersTestCase
extends MultipleServersTestCase {
	private final int serverIndex;
	
	public WelcomeAndListsMultipleServersTestCase(
		String serverName, int serverIndex
	) {
		this.serverIndex = serverIndex;
	}

	@Test
	public final void testWelcome() throws IOException {
		final String url = serversHTTPURL[serverIndex];

		assertThat(getContent(url), containsString("Hybrid Server"));
	}

	@Test
	public final void testHtmlList() throws IOException {
		testList("html", getAllHtmlUUIDs());
	}
	
	@Test
	public final void testXmlList() throws IOException {
		testList("xml", getAllXmlUUIDs());
	}
	
	@Test
	public final void testXsdList() throws IOException {
		testList("xsd", getAllXsdUUIDs());
	}
	
	@Test
	public final void testXsltList() throws IOException {
		testList("xslt", getAllXsltUUIDs());
	}

	protected void testList(final String resource, final String[] uuids)
	throws IOException {
		final String url = serversHTTPURL[serverIndex] + resource;
		
		final String content = getContent(url);
		
		for (String uuid : uuids) {
			assertThat(content, containsString(uuid));
		}
	}
}
