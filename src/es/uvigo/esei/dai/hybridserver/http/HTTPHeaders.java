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
package es.uvigo.esei.dai.hybridserver.http;

public enum HTTPHeaders {
	CONTENT_LENGTH("Content-Length"),
	CONTENT_TYPE("Content-Type"),
	HTTP_1_1("HTTP/1.1"), 
	CONNECTION("Connection");
	
	private String header;
	
	private HTTPHeaders(String header) {
		this.header = header;
	}
	
	public String getHeader() {
		return header;
	}
}
