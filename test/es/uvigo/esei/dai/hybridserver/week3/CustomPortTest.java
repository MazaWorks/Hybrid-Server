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

import static es.uvigo.esei.dai.hybridserver.utils.TestUtils.getContent;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import es.uvigo.esei.dai.hybridserver.HybridServer;

@RunWith(Parameterized.class)
public class CustomPortTest {
	@Rule public final TestRule globalTimeout = new Timeout(5, TimeUnit.SECONDS);
	
	protected HybridServer server;
	protected String url;
	protected int port;
	
	public CustomPortTest(Integer port) {
		this.port = port;
	}
	
	@Parameters
	public static Collection<Object[]> portsToTest() {
		// Si algún otro programa del sistema está utilizando estos puertos
		// entonces saltará una excepción diciendo que el puerto ya está ocupado
		return Arrays.asList(
			new Object[] { 1234 },
			new Object[] { 4242 },
			new Object[] { 7315 },
			new Object[] { 8833 },
			new Object[] { 10201 },
			new Object[] { 21386 },
			new Object[] { 33217 },
			new Object[] { 45450 },
			new Object[] { 55881 },
			new Object[] { 60000 }
		);
	}
	
	@Before
	public void startServer() throws SQLException {
		final Properties properties = new Properties();
		properties.setProperty("port", Integer.toString(this.port));
		properties.setProperty("numClients", "50");
		properties.setProperty("db.url", "jdbc:mysql://localhost/hstestdb");
		properties.setProperty("db.user", "dai");
		properties.setProperty("db.password", "daipassword");
		
		this.server = new HybridServer(properties);
		this.url = String.format("http://localhost:%d/", this.port);
		
		this.server.start();
	}
	
	@After
	public void stopServer() {
		this.server.stop();
	}
	
	@Test
	public void testWelcome() throws IOException {
		assertThat(getContent(url), containsString("Hybrid Server"));
	}
	
	@Test
	public void testMultipleWelcome() throws IOException {
		for (int i = 0; i < 10; i++) {
			assertThat(getContent(url), containsString("Hybrid Server"));
		}
	}
}
