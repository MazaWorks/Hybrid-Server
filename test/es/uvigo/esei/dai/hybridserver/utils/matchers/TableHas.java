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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hamcrest.Description;


public class TableHas extends TableMatcher {
	TableHas(Table table) {
		super(table);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("No row matching query: " + this.toSQL());
	}
	
	@Override
	protected String toSQL() {
		final StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT * FROM ").append(table.getName());
		
		final List<Column> columns = table.getColumns();
		if (!columns.isEmpty()) {
			sb.append(" WHERE 1=1");
			for (Column column : columns) {
				sb.append(" AND ").append(column.getName()).append(" = \"").append(column.getValue()).append('"');
			}
		}
		
		return sb.toString();
	}

	@Override
	protected boolean checkResults(ResultSet results) throws SQLException {
		return results.next();
	}
}
