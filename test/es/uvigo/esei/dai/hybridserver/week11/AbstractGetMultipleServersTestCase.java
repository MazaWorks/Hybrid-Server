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
import static es.uvigo.esei.dai.hybridserver.utils.matchers.EqualsToIgnoringSpacesMatcher.equalsToIgnoringSpaces;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

public abstract class AbstractGetMultipleServersTestCase
extends MultipleServersTestCase {
	protected abstract int getServerIndex();
	protected abstract String getResourceName();
	protected abstract String getContentType();

	protected void testMultipleGets(
		final String[] uuids, final String[] expectedContents
	) throws IOException {
		for (int i = 0; i < uuids.length; i++) {
			final String uuid = uuids[i];
			final String expectedContent = expectedContents[i];
			
			final String url = getResourceURL(uuid);
			final String content = getContentWithType(url, getContentType());
			
			assertThat(content, is(equalsToIgnoringSpaces(expectedContent)));
		}
	}
	
	protected String getResourceURL(String uuid) {
		return String.format("%s/%s?uuid=%s",
			serversHTTPURL[getServerIndex()], getResourceName(), uuid
		);
	}

	@Test
	public final void testInvalid() throws IOException {
		for (String uuid : generateInvalidUUIDs()) {
			final String url = getResourceURL(uuid);
			
			assertThat(getStatus(url), is(equalTo(404)));
		}
	}
}