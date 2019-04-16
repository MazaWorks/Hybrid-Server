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

public class Column {
	private final Table table;
	private final String name;
	private String value;
	
	Column(Table table, String name) {
		this.table = table;
		this.name = name;
	}
	
	public TableHas withValue(String value) {
		this.value = value;
		this.table.addColumn(this);
		
		return new TableHas(table);
	}
	
	public TableHasNot withoutValue(String value) {
		this.value = value;
		this.table.addColumn(this);
		
		return new TableHasNot(table);
	}
	
	String getName() {
		return name;
	}
	
	String getValue() {
		return value;
	}
}
