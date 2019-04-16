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
package es.uvigo.esei.dai.hybridserver.utils.matchers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hamcrest.TypeSafeMatcher;

public abstract class TableMatcher extends TypeSafeMatcher<Connection> {
	protected final Table table;
	
	TableMatcher(Table table) {
		this.table = table;
	}

	@Override
	protected boolean matchesSafely(Connection connection) {
		try (Statement statement = connection.createStatement()) {
			try (ResultSet results = statement.executeQuery(this.toSQL())) {
				return checkResults(results);
			}
		} catch (SQLException e) {
			return false;
		}
	}

	protected abstract String toSQL();
	protected abstract boolean checkResults(ResultSet results) throws SQLException;

	public static Table hasTable(String name) {
		return new Table(name);
	}
	
	public Column andColumn(String name) {
		return new Column(this.table, name);
	}
}
