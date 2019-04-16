package es.uvigo.esei.dai.serviceWeb;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import es.uvigo.esei.dai.hybridserver.ServerConfiguration;
import es.uvigo.esei.dai.modelDAO.PageNotFoundException;

public class WebServiceConnection {
	private URL[] url = new URL[4];
	private QName[] name = new QName[4];
	private String[] server = new String[4];

	public WebServiceConnection(List<ServerConfiguration> webConfiguration) throws MalformedURLException {
		Iterator<ServerConfiguration> listIterator = webConfiguration.iterator();
		ServerConfiguration server;
		server = listIterator.next();
		this.url[0] = new URL(server.getWsdl());
		this.name[0] = new QName(server.getNamespace(), server.getService());
		this.server[0] = "<b>" + server.getName() + "</b>";
		server = listIterator.next();
		this.url[1] = new URL(server.getWsdl());
		this.name[1] = new QName(server.getNamespace(), server.getService());
		this.server[1] = "<b>" + server.getName() + "</b>";
		server = listIterator.next();
		this.url[2] = new URL(server.getWsdl());
		this.name[2] = new QName(server.getNamespace(), server.getService());
		this.server[2] = "<b>" + server.getName() + "</b>";
		server = listIterator.next();
		this.url[3] = new URL(server.getWsdl());
		this.name[3] = new QName(server.getNamespace(), server.getService());
		this.server[3] = "<b>" + server.getName() + "</b>";
	}

	public String getHTMLList(int numServer) throws PageNotFoundException {
		Service service = Service.create(url[numServer], name[numServer]);
		DBService ts = service.getPort(DBService.class);
		return server[numServer] + ts.getHTMLList();
	}

	public String getXMLList(int numServer) throws PageNotFoundException {
		Service service = Service.create(url[numServer], name[numServer]);
		DBService ts = service.getPort(DBService.class);
		return server[numServer] + ts.getXMLList();
	}

	public String getXSDList(int numServer) throws PageNotFoundException {
		Service service = Service.create(url[numServer], name[numServer]);
		DBService ts = service.getPort(DBService.class);
		return server[numServer] + ts.getXSDList();
	}

	public String getXSLTList(int numServer) throws PageNotFoundException {
		Service service = Service.create(url[numServer], name[numServer]);
		DBService ts = service.getPort(DBService.class);
		return server[numServer] + ts.getXSLTList();
	}

	public String getHTMLContent(String uuid, int numServer) throws PageNotFoundException {
		Service service = Service.create(url[numServer], name[numServer]);
		DBService ts = service.getPort(DBService.class);
		return ts.getHTMLContent(uuid);
	}

	public String getXMLContent(String uuid, int numServer) throws PageNotFoundException {
		Service service = Service.create(url[numServer], name[numServer]);
		DBService ts = service.getPort(DBService.class);
		return ts.getXMLContent(uuid);
	}

	public String getXSDContent(String uuid, int numServer) throws PageNotFoundException {
		Service service = Service.create(url[numServer], name[numServer]);
		DBService ts = service.getPort(DBService.class);
		return ts.getXSDContent(uuid);
	}

	public String getXSLTContent(String uuid, int numServer) throws PageNotFoundException {
		Service service = Service.create(url[numServer], name[numServer]);
		DBService ts = service.getPort(DBService.class);
		return ts.getXSLTContent(uuid);
	}

	public String getXSLTxsd(String uuid, int numServer) throws PageNotFoundException {
		Service service = Service.create(url[numServer], name[numServer]);
		DBService ts = service.getPort(DBService.class);
		return ts.getXSLTxsd(uuid);
	}
}
