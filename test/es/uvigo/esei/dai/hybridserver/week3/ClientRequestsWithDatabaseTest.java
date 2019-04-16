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
package es.uvigo.esei.dai.hybridserver.week3;

import static es.uvigo.esei.dai.hybridserver.utils.TestUtils.deleteStatus;
import static es.uvigo.esei.dai.hybridserver.utils.TestUtils.extractUUIDFromText;
import static es.uvigo.esei.dai.hybridserver.utils.TestUtils.getContent;
import static es.uvigo.esei.dai.hybridserver.utils.TestUtils.getStatus;
import static es.uvigo.esei.dai.hybridserver.utils.TestUtils.postContent;
import static es.uvigo.esei.dai.hybridserver.utils.TestUtils.postStatus;
import static es.uvigo.esei.dai.hybridserver.utils.matchers.TableMatcher.hasTable;
import static java.util.Collections.singletonMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uvigo.esei.dai.hybridserver.HybridServer;
import es.uvigo.esei.dai.hybridserver.utils.JdbcTestCase;

public class ClientRequestsWithDatabaseTest extends JdbcTestCase {
	private HybridServer server;
	private String url;
	private String invalidUUID;
	private String[][] pages;
	
	@Before
	public void initAttributes() {
		this.invalidUUID = "12345678-abcd-1234-ab12-9876543210ab";
		
		// Estas páginas se insertan en la base de datos al inicio del test.
		this.pages = new String[][] {
		//  { "uuid",                                 "texto contenido por la página" }
			{ "6df1047e-cf19-4a83-8cf3-38f5e53f7725", "This is the html page 6df1047e-cf19-4a83-8cf3-38f5e53f7725." },
			{ "79e01232-5ea4-41c8-9331-1c1880a1d3c2", "This is the html page 79e01232-5ea4-41c8-9331-1c1880a1d3c2." },
			{ "a35b6c5e-22d6-4707-98b4-462482e26c9e", "This is the html page a35b6c5e-22d6-4707-98b4-462482e26c9e." },
			{ "3aff2f9c-0c7f-4630-99ad-27a0cf1af137", "This is the html page 3aff2f9c-0c7f-4630-99ad-27a0cf1af137." },
			{ "77ec1d68-84e1-40f4-be8e-066e02f4e373", "This is the html page 77ec1d68-84e1-40f4-be8e-066e02f4e373." },
			{ "8f824126-0bd1-4074-b88e-c0b59d3e67a3", "This is the html page 8f824126-0bd1-4074-b88e-c0b59d3e67a3." },
			{ "c6c80c75-b335-4f68-b7a7-59434413ce6c", "This is the html page c6c80c75-b335-4f68-b7a7-59434413ce6c." },
			{ "f959ecb3-6382-4ae5-9325-8fcbc068e446", "This is the html page f959ecb3-6382-4ae5-9325-8fcbc068e446." },
			{ "2471caa8-e8df-44d6-94f2-7752a74f6819", "This is the html page 2471caa8-e8df-44d6-94f2-7752a74f6819." },
			{ "fa0979ca-2734-41f7-84c5-e40e0886e408", "This is the html page fa0979ca-2734-41f7-84c5-e40e0886e408." }
		};
	}
	
	@Before
	public void startServer() throws Exception {
		final Properties properties = new Properties();
		properties.setProperty("port", Integer.toString(8888));
		properties.setProperty("numClients", "50");
		properties.setProperty("db.url", getConnectionUrl());
		properties.setProperty("db.user", getUsername());
		properties.setProperty("db.password", getPassword());
		
		this.server = new HybridServer(properties);
		this.url = String.format("http://localhost:%d/", this.server.getPort());
		
		this.server.start();
	}
	
	@After
	public void stopServer() {
		this.server.stop();
	}

	@Test
	public void testGetHtmlList() throws IOException {
		final String pageURL = url + "html";
		final String content = getContent(pageURL);
		
		for (String[] page : pages) {
			final String uuid = page[0];
			
			assertThat(content, containsString(uuid));
		}
	}
	
	@Test
	public void testPost() throws Exception {
		final String content = "<html><body>Testing POST</body></html>";
		
		// Envío de la página y extracción del uuid de la nueva página
		final String responseContent = postContent(url + "html", singletonMap("html", content));
		final String uuid = extractUUIDFromText(responseContent);
		assertThat(uuid, is(notNullValue()));
		
		// Verificación de que la página de respuesta contiene un enlace a la nueva página
		final String uuidHyperlink = "<a href=\"html?uuid=" + uuid + "\">" + uuid + "</a>";
		assertThat(responseContent, containsString(uuidHyperlink));
		
		// Recuperación de la nueva página
		final String url = this.url + "html?uuid=" + uuid;
		assertThat("The new page couldn't be retrieved", getContent(url), is(equalTo(content)));
		
		// Comprobación de la inserción en la base de datos
		assertThat(getConnection(), hasTable("HTML")
			.withColumn("uuid").withValue(uuid)
		);
	}
	
	@Test
	public void testDelete() throws Exception {
		final String uuid = pages[4][0];
		final String url = this.url + "html?uuid=" + uuid;

		// Eliminación de la página
		assertThat("The page couldn't be deleted", deleteStatus(url), is(equalTo(200)));
		
		// Comprobación de la eliminación en la base de datos
		assertThat(getConnection(), hasTable("HTML")
			.withColumn("uuid").withoutValue(uuid)
		);

		// Comprobación de la eliminación vía web
		assertThat("The page already exists", getStatus(url), is(equalTo(404)));
	}
	
	
	// Ejercicio 5
	@Test
	public void testGetInvalidHtmlPage() throws IOException {
		final String pageURL = url + "html?uuid=" + invalidUUID;

		assertThat(getStatus(pageURL), is(equalTo(404)));
	}
	
	@Test
	public void testGetInvalidResource() throws IOException {
		final String pageURL = url + "xxx?uuid=" + pages[0];

		assertThat(getStatus(pageURL), is(equalTo(400)));
	}
	
	@Test
	public void testDeleteNonexistentPage() throws IOException {
		final String pageURL = this.url + "html?uuid=" + invalidUUID;

		assertThat(getStatus(pageURL), is(equalTo(404)));
	}
	
	@Test
	public void testPostInvalidContent() throws IOException {
		final String content = "<html><body>Testing POST</body></html>";

		assertThat( postStatus(url + "html", singletonMap("xxx", content)), is(equalTo(400)));
	}
}
