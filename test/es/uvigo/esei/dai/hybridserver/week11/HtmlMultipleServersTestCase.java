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
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import es.uvigo.esei.dai.hybridserver.http.MIME;

@RunWith(Parameterized.class)
public class HtmlMultipleServersTestCase
extends MultipleServersTestCase {
	private final int serverIndex;
	
	public HtmlMultipleServersTestCase(
		String serverName, int serverIndex
	) {
		this.serverIndex = serverIndex;
	}

	@Test
	public final void testGetLocales() throws IOException {
		for (String uuid : getLocalHtmlUUIDs(this.serverIndex)) {
			final String url = String.format("%s/html?uuid=%s",
				serversHTTPURL[serverIndex], uuid);
			
			assertThat(getContentWithType(url, MIME.TEXT_HTML.getMime()), containsString(uuid));
		}
	}

	@Test
	public final void testGetRemotes() throws IOException {
		for (String uuid : getRemoteHtmlUUIDs(this.serverIndex)) {
			final String url = String.format("%s/html?uuid=%s",
				serversHTTPURL[serverIndex], uuid);
			
			assertThat(getContentWithType(url, MIME.TEXT_HTML.getMime()), containsString(uuid));
		}
	}

	@Test
	public final void testInvalid() throws IOException {
		for (String uuid : generateInvalidUUIDs()) {
			final String url = String.format("%s/html?uuid=%s",
				serversHTTPURL[serverIndex], uuid);

			assertThat(getStatus(url), is(equalTo(404)));
		}
	}
}
