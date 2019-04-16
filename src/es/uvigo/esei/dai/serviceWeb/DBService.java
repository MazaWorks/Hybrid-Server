package es.uvigo.esei.dai.serviceWeb;

import javax.jws.WebMethod;
import javax.jws.WebService;

import es.uvigo.esei.dai.modelDAO.PageNotFoundException;

@WebService
public interface DBService {

	@WebMethod
	public String getHTMLList() throws PageNotFoundException;

	@WebMethod
	public String getXMLList() throws PageNotFoundException;

	@WebMethod
	public String getXSDList() throws PageNotFoundException;

	@WebMethod
	public String getXSLTList() throws PageNotFoundException;

	@WebMethod
	public String getHTMLContent(String uuid) throws PageNotFoundException;

	@WebMethod
	public String getXMLContent(String uuid) throws PageNotFoundException;

	@WebMethod
	public String getXSDContent(String uuid) throws PageNotFoundException;

	@WebMethod
	public String getXSLTContent(String uuid) throws PageNotFoundException;

	@WebMethod
	public String getXSLTxsd(String uuid) throws PageNotFoundException;
}
