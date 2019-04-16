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

import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;

public class MySqlJdbcDatabaseTester extends JdbcDatabaseTester {
	private final static String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

	public MySqlJdbcDatabaseTester(String connectionUrl) throws ClassNotFoundException {
		super(MYSQL_DRIVER, connectionUrl);
	}

	public MySqlJdbcDatabaseTester(String connectionUrl, String username, String password)
			throws ClassNotFoundException {
		super(MYSQL_DRIVER, connectionUrl, username, password);
	}

	public MySqlJdbcDatabaseTester(String connectionUrl, String username, String password,
			String schema) throws ClassNotFoundException {
		super(MYSQL_DRIVER, connectionUrl, username, password, schema);
	}

	@Override
	public IDatabaseConnection getConnection() throws Exception {
		final IDatabaseConnection connection = super.getConnection();
		
		connection.getConfig().setProperty(
			DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
			new MySqlDataTypeFactory()
		);
		
		return connection;
	}
}
