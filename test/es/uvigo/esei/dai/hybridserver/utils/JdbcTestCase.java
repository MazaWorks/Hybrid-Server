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
package es.uvigo.esei.dai.hybridserver.utils;

import java.sql.Connection;

import org.dbunit.IDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.After;
import org.junit.Before;

public abstract class JdbcTestCase {
	private IDatabaseTester tester;
	private IDatabaseConnection connection;
	
	@Before
	public void setUpJdbc() throws Exception {
		this.tester = this.createDatabaseTester();
		
		this.connection = this.tester.getConnection();

		this.tester.setDataSet(getDataSet());
		
		this.tester.onSetup();
	}
	
	@After
	public void tearDownJdbc() throws Exception {
		try {
			this.tester.onTearDown();
			this.connection.close();
		} finally {
			this.connection = null;
		}
	}

	protected IDatabaseTester createDatabaseTester()
	throws ClassNotFoundException {
		return new MySqlJdbcDatabaseTester(getConnectionUrl(), getUsername(), getPassword());
	}
	
	protected String getConnectionUrl() {
		// Esta base de datos debe existir con las tablas creadas 
		// y el usuario debe tener acceso.
		return "jdbc:mysql://localhost/hstestdb";
	}
	
	protected String getUsername() {
		return "hsdb";
	}
	
	protected String getPassword() {
		return "hsdbpass";
	}

	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSetBuilder()
			.setMetaDataSetFromDtd(getClass().getResourceAsStream("dataset.dtd"))
			.setCaseSensitiveTableNames(false)
			.setColumnSensing(true)
		.build(getClass().getResourceAsStream("dataset.xml"));
	}
	
	protected Connection getConnection() throws Exception {
		return this.connection.getConnection();
	}
}
