package es.uvigo.esei.dai.hybridserver.http;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HTTPResponse {
	private HTTPResponseStatus status;
	private String version;
	private String content=new String();
	Map<String, String> parameters = new HashMap<String, String>();

	private String delimiter = "\r\n";
	private String space = " ";
	
	public HTTPResponse() {
	}

	public HTTPResponseStatus getStatus() {
		return status;
	}

	public void setStatus(HTTPResponseStatus status) {
		this.status=status;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version=version;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content=content;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public String putParameter(String name, String value) {
		parameters.put(name, value);
		return name + ": " + value;
	}

	public boolean containsParameter(String name) {
		if(parameters.containsKey(name))
			return true;
		else
			return false;
	}

	public String removeParameter(String name) {
		parameters.remove(name);
		return name;
	}

	public void clearParameters() {
		Iterator<String> keys = parameters.keySet().iterator();
		while(keys.hasNext()) {
			parameters.remove(keys.next());
		}
	}

	public List<String> listParameters() {
		List<String> list = new ArrayList<String>(parameters.keySet());
		return list;
	}

	public void print(Writer writer) throws IOException {
		writer.write(version + space + status.getCode() + space + status.getStatus());
		if (!(content.isEmpty())) {
			writer.write(delimiter + "Content-Length:" + space + content.length());
		}
		if(parameters.size()!=0) {
			Iterator<String> iterator= parameters.keySet().iterator();
			while(iterator.hasNext()) {
				String key=iterator.next();
				writer.write(delimiter + key + ": " + parameters.get(key));
			}
		}
		writer.write(delimiter + delimiter);
		if (!(content.isEmpty())) {
			writer.write(content);
		}
	}

	@Override
	public String toString() {
		final StringWriter writer = new StringWriter();

		try {
			this.print(writer);
		} catch (IOException e) {
		}

		return writer.toString();
	}
}
