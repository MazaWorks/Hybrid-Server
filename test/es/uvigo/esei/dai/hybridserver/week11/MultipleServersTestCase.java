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

import static es.uvigo.esei.dai.hybridserver.utils.TestUtils.readToString;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.runners.Parameterized.Parameters;

import es.uvigo.esei.dai.hybridserver.HybridServer;
import es.uvigo.esei.dai.hybridserver.XMLConfigurationLoader;
import es.uvigo.esei.dai.hybridserver.utils.MySqlJdbcDatabaseTester;

public abstract class MultipleServersTestCase {
	private static final int NUMBER_OF_SERVERS = 4;
	
	private static final String[] WILDCARD_HTML_UUIDS = new String[] {
		"6df1047e-cf19-XXXX-8cf3-38f5e53f7725",
		"79e01232-5ea4-XXXX-9331-1c1880a1d3c2",
		"a35b6c5e-22d6-XXXX-98b4-462482e26c9e",
		"3aff2f9c-0c7f-XXXX-99ad-27a0cf1af137",
		"77ec1d68-84e1-XXXX-be8e-066e02f4e373"
	};
	
	private static final String[] WILDCARD_XML_UUIDS = new String[] {
		"ddcab7d0-636c-XXXX-8db3-685b35c84fb4",
		"ea118888-6908-XXXX-9620-685b35c84fb4"
	};
	
	private static final String[] WILDCARD_XML_FILES = new String[] {
		"files/sample1_X.xml",
		"files/sample2_X.xml"
	};
	
	private static final String[] WILDCARD_XSD_UUIDS = new String[] {
		"e5b64c34-636c-XXXX-b729-685b35c84fb4",
		"05b88faa-6909-XXXX-aadc-685b35c84fb4"
	};
	
	private static final String[] WILDCARD_XSD_FILES = new String[] {
		"files/sample1_X.xsd",
		"files/sample2_X.xsd"
	};
	
	private static final String[] WILDCARD_XSLT_UUIDS = new String[] {
		"f260dfee-636c-XXXX-bbdd-685b35c84fb4",
		"1fd26c94-6909-XXXX-9a75-685b35c84fb4"
	};
	
	private static final String[] WILDCARD_XSLT_FILES = new String[] {
		"files/sample1_X.xslt",
		"files/sample2_X.xslt"
	};
	
	private static final String[] WILDCARD_TRANSFORMED_FILES = new String[] {
		"files/sample1_X.html",
		"files/sample2_X.html"
	};
	
	protected IDatabaseTester[] testers;
	protected HybridServer[] servers;
	protected String[] serversHTTPURL;
	
	@Parameters(name = "{0}")
	public static Collection<Object[]> getIndexesList() {
		final List<Object[]> list = new ArrayList<>();
		
		for (int i = 0; i < NUMBER_OF_SERVERS; i++) {
			list.add(new Object[] { "Hybrid Server " + (i + 1), i });
		}
		
		return list;
	}
	
	@Before
	public void setUpJdbc() throws Exception {
		this.testers = new IDatabaseTester[4];
		
		for (int i = 0; i < testers.length; i++) {
			this.testers[i] = this.createDatabaseTester(i + 1);
			this.testers[i].setDataSet(this.createDataSet(i + 1));
			this.testers[i].onSetup();
		}
	}
	
	@Before
	public void setUpServers() throws Exception {
		this.servers = new HybridServer[NUMBER_OF_SERVERS];
		this.serversHTTPURL = new String[NUMBER_OF_SERVERS];
		
		final XMLConfigurationLoader loader = 
			new XMLConfigurationLoader();
		
		for (int i = 0; i < 4; i++) {
			this.servers[i] = new HybridServer(
				loader.load(new File("test", "configuration" + (i+1) + ".xml"))
			);
			this.serversHTTPURL[i] = "http://localhost:1000" + (i+1) + "/";
			
			this.servers[i].start();
		}
	}

	@After
	public void tearDownJdbc() throws Exception {
		for (IDatabaseTester tester : this.testers) {
			tester.onTearDown();
		}
	}
	
	@After
	public void tearDownServers() throws Exception {
		for (HybridServer server : this.servers) {
			server.stop();
		}
	}

	protected IDataSet createDataSet(int index)
	throws Exception {
		return new FlatXmlDataSetBuilder()
			.setMetaDataSetFromDtd(getClass().getResourceAsStream("files/dataset.dtd"))
			.setCaseSensitiveTableNames(false)
			.setColumnSensing(true)
		.build(getClass().getResourceAsStream("files/dataset" + index + ".xml"));
	}

	protected JdbcDatabaseTester createDatabaseTester(int index)
	throws ClassNotFoundException {
		return new MySqlJdbcDatabaseTester(
			getConnectionUrl(index),
			getUsername(),
			getPassword()
		);
	}
	
	protected String getConnectionUrl(int index) {
		// Esta base de datos debe existir con las tablas creadas 
		// y el usuario debe tener acceso.
		return "jdbc:mysql://localhost/hstestdb" + index;
	}
	
	protected String getUsername() {
		return "hsdb";
	}
	
	protected String getPassword() {
		return "hsdbpass";
	}
	
	private static String[] getLocalResources(int serverIndex, String[] wildcardResources) {
		final String[] uuids = new String[wildcardResources.length];
		
		for (int i = 0; i < wildcardResources.length; i++) {
			uuids[i] = wildcardResources[i]
				.replaceAll("X", Integer.toString(serverIndex + 1));
		}
		
		return uuids;
	}
	
	private static String[] getRemoteResources(int serverIndex, String[] wildcardResources) {
		final String[] uuids = new String[wildcardResources.length * (NUMBER_OF_SERVERS - 1)];

		int index = 0;
		for (int i = 0; i < NUMBER_OF_SERVERS; i++) {
			if (i != serverIndex) {
				for (int j = 0; j < wildcardResources.length; j++) {
					uuids[index++] = wildcardResources[j]
						.replaceAll("X", Integer.toString(i + 1));
				}
			}
		}
		
		return uuids;
	}
	
	private static String[] getAllResources(String[] wildcardResources) {
		final String[] uuids = new String[wildcardResources.length * NUMBER_OF_SERVERS];

		int index = 0;
		for (int i = 0; i < NUMBER_OF_SERVERS; i++) {
			for (int j = 0; j < wildcardResources.length; j++) {
				uuids[index++] = wildcardResources[j]
					.replaceAll("X", Integer.toString(i + 1));
			}
		}
		
		return uuids;
	}
	
	protected static String[] getLocalHtmlUUIDs(int serverIndex) {
		return getLocalResources(serverIndex, WILDCARD_HTML_UUIDS);
	}
	
	protected static String[] getLocalXmlUUIDs(int serverIndex) {
		return getLocalResources(serverIndex, WILDCARD_XML_UUIDS);
	}
	
	protected static String[] getLocalXsdUUIDs(int serverIndex) {
		return getLocalResources(serverIndex, WILDCARD_XSD_UUIDS);
	}
	
	protected static String[] getLocalXsltUUIDs(int serverIndex) {
		return getLocalResources(serverIndex, WILDCARD_XSLT_UUIDS);
	}
	
	protected static String[] getRemoteHtmlUUIDs(int serverIndex) {
		return getRemoteResources(serverIndex, WILDCARD_HTML_UUIDS);
	}
	
	protected static String[] getRemoteXmlUUIDs(int serverIndex) {
		return getRemoteResources(serverIndex, WILDCARD_XML_UUIDS);
	}
	
	protected static String[] getRemoteXsdUUIDs(int serverIndex) {
		return getRemoteResources(serverIndex, WILDCARD_XSD_UUIDS);
	}
	
	protected static String[] getRemoteXsltUUIDs(int serverIndex) {
		return getRemoteResources(serverIndex, WILDCARD_XSLT_UUIDS);
	}
	
	protected static String[] getAllHtmlUUIDs() {
		return getAllResources(WILDCARD_HTML_UUIDS);
	}
	
	protected static String[] getAllXmlUUIDs() {
		return getAllResources(WILDCARD_XML_UUIDS);
	}
	
	protected static String[] getAllXsdUUIDs() {
		return getAllResources(WILDCARD_XSD_UUIDS);
	}
	
	protected static String[] getAllXsltUUIDs() {
		return getAllResources(WILDCARD_XSLT_UUIDS);
	}
	
	protected static String[] getFileContents(String[] fileNames) {
		final String[] contents = new String[fileNames.length];
		
		final Class<MultipleServersTestCase> clazz = MultipleServersTestCase.class;
		
		for (int i = 0; i < fileNames.length; i++) {
			contents[i] = readToString(clazz.getResourceAsStream(
				fileNames[i]
			));
		}
		
		return contents;
	}
	
	protected static String[] getLocalTransformedFiles(int serverIndex) {
		return getFileContents(getLocalResources(serverIndex, WILDCARD_TRANSFORMED_FILES));
	}
	
	protected static String[] getLocalXmlFiles(int serverIndex) {
		return getFileContents(getLocalResources(serverIndex, WILDCARD_XML_FILES));
	}
	
	protected static String[] getLocalXsdFiles(int serverIndex) {
		return getFileContents(getLocalResources(serverIndex, WILDCARD_XSD_FILES));
	}
	
	protected static String[] getLocalXsltFiles(int serverIndex) {
		return getFileContents(getLocalResources(serverIndex, WILDCARD_XSLT_FILES));
	}
	
	protected static String[] getRemoteTransformedFiles(int serverIndex) {
		return getFileContents(getRemoteResources(serverIndex, WILDCARD_TRANSFORMED_FILES));
	}
	
	protected static String[] getRemoteXmlFiles(int serverIndex) {
		return getFileContents(getRemoteResources(serverIndex, WILDCARD_XML_FILES));
	}
	
	protected static String[] getRemoteXsdFiles(int serverIndex) {
		return getFileContents(getRemoteResources(serverIndex, WILDCARD_XSD_FILES));
	}
	
	protected static String[] getRemoteXsltFiles(int serverIndex) {
		return getFileContents(getRemoteResources(serverIndex, WILDCARD_XSLT_FILES));
	}
	
	protected static String[] getAllTransformedFiles() {
		return getFileContents(getAllResources(WILDCARD_TRANSFORMED_FILES));
	}
	
	protected static String[] getAllXmlFiles() {
		return getFileContents(getAllResources(WILDCARD_XML_FILES));
	}
	
	protected static String[] getAllXsdFiles() {
		return getFileContents(getAllResources(WILDCARD_XSD_FILES));
	}
	
	protected static String[] getAllXsltFiles() {
		return getFileContents(getAllResources(WILDCARD_XSLT_FILES));
	}
	
	protected static String[] generateInvalidUUIDs() {
		return generateInvalidUUIDs(10);
	}
	
	protected static String[] generateInvalidUUIDs(int howMany) {
		final String[] uuids = new String[howMany];
		
		for (int i = 0; i < howMany; i++) {
			uuids[i] = UUID.randomUUID().toString();
		}
		
		return uuids;
	}
}
