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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

public final class TestUtils {
	private TestUtils() {}
	
	/**
	 * Realiza una petición por GET y devuelve el contenido de la respuesta.
	 * 
	 * La petición se hace en UTF-8 y solicitando el cierre de la conexión.
	 * 
	 * Este método comprueba que el código de respuesta HTTP sea 200 OK.
	 * 
	 * @param url URL de la página solicitada.
	 * @return contenido de la respuesta HTTP.
	 * @throws IOException en el caso de que se produzca un error de conexión.
	 */
	public static String getContent(String url) throws IOException {
		final HttpResponse response = Request.Get(url)
			.addHeader("Connection", "close")
			.addHeader("Content-encoding", "UTF-8")
			.execute()
		.returnResponse();
		
		assertEquals(200, response.getStatusLine().getStatusCode());
		
		return EntityUtils.toString(response.getEntity());
	}
	
	/**
	 * Realiza una petición por GET y devuelve el contenido de la respuesta.
	 * 
	 * La petición se hace en UTF-8 y solicitando el cierre de la conexión.
	 * 
	 * Este método comprueba que el código de respuesta HTTP sea 200 OK.
	 * 
	 * @param url URL de la página solicitada.
	 * @return contenido de la respuesta HTTP.
	 * @throws IOException en el caso de que se produzca un error de conexión.
	 */
	public static String getContentWithType(String url, String contentType)
	throws IOException {
		final HttpResponse response = Request.Get(url)
			.addHeader("Connection", "close")
			.addHeader("Content-encoding", "UTF-8")
			.execute()
		.returnResponse();
		
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals(contentType, response.getEntity().getContentType().getValue());
		
		return EntityUtils.toString(response.getEntity());
	}
	
	/**
	 * Realiza una petición por GET y devuelve el código de la respuesta HTTP.
	 * 
	 * La petición se hace en UTF-8 y solicitando el cierre de la conexión.
	 * 
	 * Este método comprueba que el código de respuesta HTTP sea 200 OK.
	 * 
	 * @param url URL de la página solicitada.
	 * @return código de la respuesta HTTP.
	 * @throws IOException en el caso de que se produzca un error de conexión.
	 */
	public static int getStatus(String url) throws IOException {
		return Request.Get(url)
			.addHeader("Connection", "close")
			.addHeader("Content-encoding", "UTF-8")
			.execute()
			.returnResponse()
			.getStatusLine()
		.getStatusCode();
	}

	/**
	 * Realiza una petición por POST y devuelve el contenido de la respuesta.
	 * 
	 * La petición se hace en UTF-8 y solicitando el cierre de la conexión.
	 * 
	 * Este método comprueba que el código de respuesta HTTP sea 200 OK.
	 * 
	 * @param url URL de la página solicitada.
	 * @param content parámetros que se incluirán en la petición HTTP.
	 * @return contenido de la respuesta HTTP.
	 * @throws IOException en el caso de que se produzca un error de conexión.
	 */
	public static String postContent(String url, Map<String, String> content)
	throws IOException {
		final HttpResponse response = Request.Post(url)
			.addHeader("Connection", "close")
			.addHeader("Content-encoding", "UTF-8")
			.bodyForm(mapToNameValuePair(content))
			.execute()
		.returnResponse();
		
		assertEquals(200, response.getStatusLine().getStatusCode());
		
		return EntityUtils.toString(response.getEntity());
	}

	/**
	 * Realiza una petición por POST y devuelve el código de la respuesta.
	 * 
	 * La petición se hace en UTF-8 y solicitando el cierre de la conexión.
	 * 
	 * Este método comprueba que el código de respuesta HTTP sea 200 OK.
	 * 
	 * @param url URL de la página solicitada.
	 * @param content parámetros que se incluirán en la petición HTTP.
	 * @return código de la respuesta HTTP.
	 * @throws IOException en el caso de que se produzca un error de conexión.
	 */
	public static int postStatus(String url, Map<String, String> content)
	throws IOException {
		return Request.Post(url)
			.addHeader("Connection", "close")
			.addHeader("Content-encoding", "UTF-8")
			.bodyForm(mapToNameValuePair(content))
			.execute()
			.returnResponse()
			.getStatusLine()
		.getStatusCode();
	}
	
	/**
	 * Realiza una petición por DELETE y devuelve el código de la respuesta HTTP.
	 * 
	 * La petición se hace en UTF-8 y solicitando el cierre de la conexión.
	 * 
	 * Este método comprueba que el código de respuesta HTTP sea 200 OK.
	 * 
	 * @param url URL de la página solicitada.
	 * @return código de la respuesta HTTP.
	 * @throws IOException en el caso de que se produzca un error de conexión.
	 */
	public static int deleteStatus(String url) throws IOException {
		return Request.Delete(url)
			.addHeader("Connection", "close")
			.addHeader("Content-encoding", "UTF-8")
			.execute()
			.returnResponse()
			.getStatusLine()
		.getStatusCode();
	}
	
	/**
	 * Extrae un UUID de un texto. En el caso de que existan varios UUID en el
	 * texto se devolverá, únicamente, el primero de ellos.
	 * 
	 * @param text texto del cual se quiere extraer el UUID.
	 * @return UUID encontrado en el texto o <code>null</code> en el caso de 
	 * que no exista ninguno.
	 */
	public static String extractUUIDFromText(String text) {
		final String patternString = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
		
		final Pattern pattern = Pattern.compile(patternString);
		final Matcher matcher = pattern.matcher(text);
		
		return matcher.find() ? matcher.group() : null;
	}
	
	/**
	 * Lee por completo un InputStream y devuelve su contenido en forma de 
	 * cadena de texto.
	 * 
	 * @param is flujo de entrada del que se va a leer.
	 * @return contenido del flujo de entrada.
	 */
	public static String readToString(InputStream is) {
		try (final Reader reader = new InputStreamReader(is)) {
			final char[] buffer = new char[4096];
			final StringBuilder sb = new StringBuilder();
			
			int read;
			while ((read = reader.read(buffer, 0, buffer.length)) != -1) {
				sb.append(buffer, 0, read);
			}
			
			return sb.toString();
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}
	
	private static List<NameValuePair> mapToNameValuePair(Map<String, String> map) {
		final Form form = Form.form();
		
		for (Map.Entry<String, String> entry : map.entrySet()) {
			form.add(entry.getKey(), entry.getValue());
		}
		
		return form.build();
	}
}
