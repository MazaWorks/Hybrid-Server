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

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import es.uvigo.esei.dai.hybridserver.http.MIME;

@RunWith(Parameterized.class)
public class XsdMultipleServersTestCase
extends AbstractGetMultipleServersTestCase {
	private final int serverIndex;
	
	public XsdMultipleServersTestCase(
		String serverName, int serverIndex
	) {
		this.serverIndex = serverIndex;
	}

	@Test
	public final void testGetLocales() throws IOException {
		final String[] uuids = getLocalXsdUUIDs(this.serverIndex);
		final String[] contents = getLocalXsdFiles(this.serverIndex);
		
		testMultipleGets(uuids, contents);
	}

	@Test
	public final void testGetRemotes() throws IOException {
		final String[] uuids = getRemoteXsdUUIDs(this.serverIndex);
		final String[] contents = getRemoteXsdFiles(this.serverIndex);
		
		testMultipleGets(uuids, contents);
	}

	@Override
	protected String getContentType() {
		return MIME.APPLICATION_XML.getMime();
	}

	@Override
	protected String getResourceName() {
		return "xsd";
	}

	@Override
	protected int getServerIndex() {
		return this.serverIndex;
	}
}
