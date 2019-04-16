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
package es.uvigo.esei.dai.hybridserver.week9;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import es.uvigo.esei.dai.hybridserver.XMLConfigurationLoader;

@RunWith(Parameterized.class)
public class InvalidXMLConfigurationLoaderTest {
	private XMLConfigurationLoader xmlConfiguration;

	private final String xmlFilePath;
	
	public InvalidXMLConfigurationLoaderTest(
		String name, String xmlFilePath
	) {
		this.xmlFilePath = xmlFilePath;
		this.xmlConfiguration = new XMLConfigurationLoader();
	}

	@Parameters(name = "{0}")
	public static Collection<Object[]> parameters() {
		return Arrays.asList(
			new Object[] { "Missing http parameter", "test/invalid-configuration1.xml" },
			new Object[] { "Missing database configuration", "test/invalid-configuration2.xml" },
			new Object[] { "Invalid http port number", "test/invalid-configuration3.xml" },
			new Object[] { "Invalid number of clients", "test/invalid-configuration4.xml" },
			new Object[] { "Missing attributes in server", "test/invalid-configuration5.xml" }
		);
	}
	
	@Test(expected = Exception.class)
	public final void testLoad()
	throws Exception {
		xmlConfiguration.load(new File(xmlFilePath));
	}
}
