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

public enum HTTPResponseStatus {
	S100("Continue"),
	S101("Switching Protocols"),
	S200("OK"),
	S201("Created"),
	S202("Accepted"),
	S203("Non-Authoritative Information"),
	S204("No Content"),
	S205("Reset Content"),
	S206("Partial Content"),
	S300("Multiple Choices"),
	S301("Moved Permanently"),
	S302("Found"),
	S303("See Other"),
	S304("Not Modified"),
	S305("Use Proxy"),
	S307("Temporary Redirect"),
	S400("Bad Request"),
	S401("Unauthorized"),
	S402("Payment Required"),
	S403("Forbidden"),
	S404("Not Found"),
	S405("Method Not Allowed"),
	S406("Not Acceptable"),
	S407("Proxy Authentication Required"),
	S408("Request Time-out"),
	S409("Conflict"),
	S410("Gone"),
	S411("Length Required"),
	S412("Precondition Failed"),
	S413("Request Entity Too Large"),
	S414("Request-URI Too Large"),
	S415("Unsupported Media Type"),
	S416("Requested range not satisfiable"),
	S417("Expectation Failed"),
	S500("Internal Server Error"),
	S501("Not Implemented"),
	S502("Bad Gateway"),
	S503("Service Unavailable"),
	S504("Gateway Time-out"),
	S505("HTTP Version not supported");
	
	private final int code;
	private final String status;
	
	private HTTPResponseStatus(String status) {
		this.code = Integer.parseInt(this.name().substring(1));
		this.status = status;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getStatus() {
		return status;
	}
	
	public static HTTPResponseStatus forCode(int code) {
		return HTTPResponseStatus.valueOf("S" + code);
	}
}
