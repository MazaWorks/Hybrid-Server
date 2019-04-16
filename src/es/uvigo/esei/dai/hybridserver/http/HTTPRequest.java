package es.uvigo.esei.dai.hybridserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class HTTPRequest {
	String resourceChain;
	String[] resourcePath = new String[0];
	String resourceName;
	String httpVersion;
	String content;
	int contentLength;
	HTTPRequestMethod method;
	Map<String, String> headerParameters = new LinkedHashMap<String, String>();
	Map<String, String> resourceParameters = new LinkedHashMap<String, String>();

	public HTTPRequest(Reader reader) throws IOException, HTTPParseException {
		try {
			final BufferedReader input = new BufferedReader(reader);
			String line;
			String request = "";
			while (!(line = input.readLine()).equals("")) {
				request += line;
				request += "\r\n";
			}
			
			String[] split1 = request.split("\r\n");
			String[] split2 = split1[0].split(" ");

			method = HTTPRequestMethod.valueOf(split2[0]);
			resourceChain = split2[1];
			String[] split3 = resourceChain.split("\\?");
			if (!resourceChain.equals("/")) {
				String[] split4 = split3[0].substring(1).split("/");
				resourcePath = split4;
			}
			resourceName = split3[0].substring(1);
			httpVersion = split2[2];

			for (int i = 1; i < split1.length; i++) {
				String[] split5 = split1[i].split(" ");
				String[] split6 = split5[0].split(":");
				headerParameters.put(split6[0], split5[1]);
			}

			if (headerParameters.containsKey("Content-Length")) {
				contentLength = Integer.parseInt(headerParameters.get("Content-Length"));
				char[] buffer=new char[contentLength];
				input.read(buffer,0,contentLength);
				content=String.valueOf(buffer);
			}

			String type = headerParameters.get("Content-Type");
			if (type != null && type.startsWith("application/x-www-form-urlencoded")) {
				content = URLDecoder.decode(content, "UTF-8");
			}

			if (method == HTTPRequestMethod.POST) {
				String[] split7 = content.split("&");
				for (int i = 0; i < split7.length; i++) {
					String[] split8 = split7[i].split("=");
					resourceParameters.put(split8[0], split8[1]);
				}
			} else if (method != HTTPRequestMethod.POST) {
				if (split3.length > 1) {
					String[] split7 = split3[1].split("&");
					for (int i = 0; i < split7.length; i++) {
						String[] split8 = split7[i].split("=");
						resourceParameters.put(split8[0], split8[1]);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			throw new HTTPParseException();
		}
	}

	public HTTPRequestMethod getMethod() {
		return method;
	}

	public String getResourceChain() {
		return resourceChain;
	}

	public String[] getResourcePath() {
		return resourcePath;
	}

	public String getResourceName() {
		return resourceName;
	}

	public Map<String, String> getResourceParameters() {
		return resourceParameters;
	}

	public String getHttpVersion() {
		return httpVersion;
	}

	public Map<String, String> getHeaderParameters() {
		return headerParameters;
	}

	public String getContent() {
		return content;
	}

	public int getContentLength() {
		return contentLength;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(this.getMethod().name()).append(' ').append(this.getResourceChain())
				.append(' ').append(this.getHttpVersion()).append("\r\n");

		for (Map.Entry<String, String> param : this.getHeaderParameters().entrySet()) {
			sb.append(param.getKey()).append(": ").append(param.getValue()).append("\r\n");
		}

		if (this.getContentLength() > 0) {
			sb.append("\r\n").append(this.getContent());
		}

		return sb.toString();
	}
}
