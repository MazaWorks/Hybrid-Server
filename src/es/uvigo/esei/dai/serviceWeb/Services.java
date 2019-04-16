package es.uvigo.esei.dai.serviceWeb;

import es.uvigo.esei.dai.modelDAO.PageDAO;
import es.uvigo.esei.dai.modelDAO.PageDBDAOHTML;
import es.uvigo.esei.dai.modelDAO.PageDBDAOXML;
import es.uvigo.esei.dai.modelDAO.PageDBDAOXSD;
import es.uvigo.esei.dai.modelDAO.PageDBDAOXSLT;
import es.uvigo.esei.dai.modelDAO.PageNotFoundException;

import javax.jws.WebService;

@WebService(endpointInterface = "es.uvigo.esei.dai.serviceWeb.DBService", targetNamespace = "http://hybridserver.dai.esei.uvigo.es/", serviceName = "HybridServerService")
public class Services implements DBService {
	private String dbUrl;
	private String dbUser;
	private String dbPassword;

	public Services(String[] DBConfiguration) {
		this.dbUrl = DBConfiguration[0];
		this.dbUser = DBConfiguration[1];
		this.dbPassword = DBConfiguration[2];
	}

	@Override
	public String getHTMLList() throws PageNotFoundException {
		PageDAO dao = new PageDBDAOHTML(dbUrl, dbUser, dbPassword);
		return dao.getListUUID();
	}

	@Override
	public String getXMLList() throws PageNotFoundException {
		PageDAO dao = new PageDBDAOXML(dbUrl, dbUser, dbPassword);
		return dao.getListUUID();
	}

	@Override
	public String getXSDList() throws PageNotFoundException {
		PageDAO dao = new PageDBDAOXSD(dbUrl, dbUser, dbPassword);
		return dao.getListUUID();
	}

	@Override
	public String getXSLTList() throws PageNotFoundException {
		PageDBDAOXSLT dao = new PageDBDAOXSLT(dbUrl, dbUser, dbPassword);
		return dao.getListUUID();
	}

	@Override
	public String getHTMLContent(String uuid) throws PageNotFoundException {
		PageDAO dao = new PageDBDAOHTML(dbUrl, dbUser, dbPassword);
		return dao.get(uuid);
	}

	@Override
	public String getXMLContent(String uuid) throws PageNotFoundException {
		PageDAO dao = new PageDBDAOXML(dbUrl, dbUser, dbPassword);
		return dao.get(uuid);
	}

	@Override
	public String getXSDContent(String uuid) throws PageNotFoundException {
		PageDAO dao = new PageDBDAOXSD(dbUrl, dbUser, dbPassword);
		return dao.get(uuid);
	}

	@Override
	public String getXSLTContent(String uuid) throws PageNotFoundException {
		PageDBDAOXSLT dao = new PageDBDAOXSLT(dbUrl, dbUser, dbPassword);
		return dao.getContent(uuid);
	}

	@Override
	public String getXSLTxsd(String uuid) throws PageNotFoundException {
		PageDBDAOXSLT dao = new PageDBDAOXSLT(dbUrl, dbUser, dbPassword);
		return dao.getXSD(uuid);
	}
}
