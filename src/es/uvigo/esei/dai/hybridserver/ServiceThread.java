package es.uvigo.esei.dai.hybridserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

import javax.xml.ws.WebServiceException;

import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequestMethod;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.http.MIME;
import es.uvigo.esei.dai.modelDAO.PageDAO;
import es.uvigo.esei.dai.modelDAO.PageDBDAOHTML;
import es.uvigo.esei.dai.modelDAO.PageDBDAOXML;
import es.uvigo.esei.dai.modelDAO.PageDBDAOXSD;
import es.uvigo.esei.dai.modelDAO.PageDBDAOXSLT;
import es.uvigo.esei.dai.modelDAO.PageNotFoundException;
import es.uvigo.esei.dai.serviceWeb.WebServiceConnection;

public class ServiceThread implements Runnable {
	private final Socket socket;
	String dbUrl, dbUser, dbPassword;
	private List<ServerConfiguration> webConfiguration = null;
	private int numServers;

	public ServiceThread(Socket clientSocket, String[] dao, List<ServerConfiguration> webConfiguration)
			throws IOException {
		socket = clientSocket;
		this.dbUrl = dao[0];
		this.dbUser = dao[1];
		this.dbPassword = dao[2];
		this.webConfiguration = webConfiguration;
		this.numServers = webConfiguration.size();
	}

	public ServiceThread(Socket clientSocket, String[] dao) throws IOException {
		socket = clientSocket;
		this.dbUrl = dao[0];
		this.dbUser = dao[1];
		this.dbPassword = dao[2];
	}

	public void run() {
		try (Socket socket = this.socket) {
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			final InputStreamReader input = new InputStreamReader(socket.getInputStream());
			HTTPRequest request = null;
			HTTPResponseStatus status = HTTPResponseStatus.S400;
			HTTPResponse response = new HTTPResponse();
			try {
				request = new HTTPRequest(input);
				String content = "";
				if (request.getResourceName().equals("")) {
					content = "<html> <b>Hybrid Server</b> <br> Autores: <br>Maza Espinosa, Jorge Carlos <br> Arce Sabin, Abraham <br>"
							+ " <a href=\"html\">Listado de paginas</a></html>";
					status = HTTPResponseStatus.S200;
				} else if (request.getResourceName().equals("xslt")) {
					String par = "xslt";
					PageDBDAOXSLT dao = new PageDBDAOXSLT(dbUrl, dbUser, dbPassword);
					response.putParameter("Content-Type", MIME.APPLICATION_XML.getMime());
					if (request.getMethod() == HTTPRequestMethod.DELETE) {
						if (request.getResourceParameters().containsKey("uuid")) {
							dao.delete(request.getResourceParameters().get("uuid"));
							status = HTTPResponseStatus.S200;
						}
					} else if (request.getMethod() == HTTPRequestMethod.GET) {
						if (request.getResourceParameters().containsKey("uuid")) {
							if (dao.containsKey(request.getResourceParameters().get("uuid"))) {
								content = dao.getContent(request.getResourceParameters().get("uuid"));
								status = HTTPResponseStatus.S200;
							} else if (!(dao.containsKey(request.getResourceParameters().get("uuid")))) {
								if (webConfiguration != null) {
									WebServiceConnection webServices = new WebServiceConnection(this.webConfiguration);
									boolean encontrado = false;
									int i = 0;
									while (!encontrado && i < numServers) {
										String lista = webServices.getXSLTList(i);
										if ((lista.indexOf(request.getResourceParameters().get("uuid")) != -1)) {
											content = webServices
													.getXSLTContent(request.getResourceParameters().get("uuid"), i);
											encontrado = true;
											status = HTTPResponseStatus.S200;
										}
										i++;
									}
									if (!encontrado) {
										status = HTTPResponseStatus.S404;
									}
								} else
									status = HTTPResponseStatus.S404;
							}
						} else {
							content += "<html>";
							content += "<b> Listado de Paginas </b>";
							content += "<b> LOCAL SERVER </b>";
							content += dao.getListUUID();
							if (webConfiguration != null) {
								WebServiceConnection webServices = new WebServiceConnection(this.webConfiguration);
								for (int i = 0; i < numServers; i++) {
									try {
										content += webServices.getXSLTList(i);
									} catch (WebServiceException e) {
										e.printStackTrace();
									}
								}
							}
							content += "</html>";
							status = HTTPResponseStatus.S200;
						}
					} else if (request.getMethod() == HTTPRequestMethod.POST) {
						if (!request.getResourceParameters().containsKey("xsd")) {
							status = HTTPResponseStatus.S400;
						} else {
							if (dao.xsdExist(request.getResourceParameters().get("xsd"))) {
								UUID randomUuid = UUID.randomUUID();
								String uuid = randomUuid.toString();
								content = "<a href=\"" + par + "?uuid=" + uuid + "\">" + uuid + "</a>";
								if (request.getResourceParameters().containsKey(par)) {
									dao.create(uuid, request.getResourceParameters().get(par),
											request.getResourceParameters().get("xsd"));
									status = HTTPResponseStatus.S200;
								} else {
									status = HTTPResponseStatus.S400;
								}
							} else {
								status = HTTPResponseStatus.S404;
							}
						}
					}
				} else if (request.getResourceName().equals("html") || request.getResourceName().equals("xml")
						|| request.getResourceName().equals("xsd")) {
					PageDAO dao = null;
					String par = "html";
					if (request.getResourceName().equals("html")) {
						par = "html";
						dao = new PageDBDAOHTML(dbUrl, dbUser, dbPassword);
						response.putParameter("Content-Type", MIME.TEXT_HTML.getMime());
					} else if (request.getResourceName().equals("xml")) {
						par = "xml";
						dao = new PageDBDAOXML(dbUrl, dbUser, dbPassword);
						response.putParameter("Content-Type", MIME.APPLICATION_XML.getMime());
					} else if (request.getResourceName().equals("xsd")) {
						par = "xsd";
						dao = new PageDBDAOXSD(dbUrl, dbUser, dbPassword);
						response.putParameter("Content-Type", MIME.APPLICATION_XML.getMime());
					}
					if (request.getMethod() == HTTPRequestMethod.DELETE) {
						if (request.getResourceParameters().containsKey("uuid")) {
							dao.delete(request.getResourceParameters().get("uuid"));
							status = HTTPResponseStatus.S200;
						}
					} else if (request.getMethod() == HTTPRequestMethod.GET) {
						if (request.getResourceParameters().containsKey("uuid")) {
							if (dao.containsKey(request.getResourceParameters().get("uuid"))) {
								if (request.getResourceParameters().containsKey("xslt")) {
									PageDBDAOXSLT daoXSLT = new PageDBDAOXSLT(dbUrl, dbUser, dbPassword);
									PageDBDAOXSD daoXSD = new PageDBDAOXSD(dbUrl, dbUser, dbPassword);
									if (daoXSLT.containsKey(request.getResourceParameters().get("xslt"))) {
										String xsd = daoXSLT.getXSD(request.getResourceParameters().get("xslt"));
										if (daoXSD.containsKey(xsd)) {
											String strxsd = daoXSD.get(xsd);
											String strxslt = daoXSLT
													.getContent(request.getResourceParameters().get("xslt"));
											String strxml = dao.get(request.getResourceParameters().get("uuid"));
											if (XMLUtils.validate(strxml, strxsd)) {
												content = XMLUtils.transform(strxml, strxslt);
												status = HTTPResponseStatus.S200;
												response.putParameter("Content-Type", MIME.TEXT_HTML.getMime());
											} else
												status = HTTPResponseStatus.S400;
										} else
											status = HTTPResponseStatus.S400;
									} else {
										if (webConfiguration != null) {
											WebServiceConnection webServices = new WebServiceConnection(
													this.webConfiguration);
											boolean encontrado = false;
											int i = 0;
											String lista;
											String strxsd = null;
											String strxslt = null;
											while (!encontrado && i < numServers) {
												try {
													lista = webServices.getXSLTList(i);
													if ((lista.indexOf(
															request.getResourceParameters().get("xslt")) != -1)) {
														String xsd = webServices.getXSLTxsd(
																request.getResourceParameters().get("xslt"), i);
														strxsd = webServices.getXSDContent(xsd, i);
														strxslt = webServices.getXSLTContent(
																request.getResourceParameters().get("xslt"), i);
														encontrado = true;
													}
													i++;
												} catch (WebServiceException e) {
													i++;
													e.printStackTrace();
												}
											}
											if (encontrado) {
												String strxml = dao.get(request.getResourceParameters().get("uuid"));
												if (XMLUtils.validate(strxml, strxsd)) {
													content = XMLUtils.transform(strxml, strxslt);
													status = HTTPResponseStatus.S200;
													response.putParameter("Content-Type", MIME.TEXT_HTML.getMime());
												} else {
													status = HTTPResponseStatus.S400;
												}
											} else {
												status = HTTPResponseStatus.S404;
											}
										} else
											status = HTTPResponseStatus.S404;
									}
								} else {
									content = dao.get(request.getResourceParameters().get("uuid"));
									status = HTTPResponseStatus.S200;
								}
							} else if (!(dao.containsKey(request.getResourceParameters().get("uuid")))) {
								if (webConfiguration != null) {
									WebServiceConnection webServices = new WebServiceConnection(this.webConfiguration);
									boolean encontrado = false;
									int i = 0;
									String lista;
									while (!encontrado && i < numServers) {
										try {
											switch (par) {
											case "html":
												lista = webServices.getHTMLList(i);
												break;
											case "xml":
												lista = webServices.getXMLList(i);
												break;
											default:
												lista = webServices.getXSDList(i);
												break;
											}
											if ((lista.indexOf(request.getResourceParameters().get("uuid")) != -1)) {
												encontrado = true;
												if (request.getResourceParameters().containsKey("xslt")) {
													String strxml = webServices.getXMLContent(
															request.getResourceParameters().get("uuid"), i);
													PageDBDAOXSLT daoXSLT = new PageDBDAOXSLT(dbUrl, dbUser,
															dbPassword);
													PageDBDAOXSD daoXSD = new PageDBDAOXSD(dbUrl, dbUser, dbPassword);
													if (daoXSLT
															.containsKey(request.getResourceParameters().get("xslt"))) {
														String xsd = daoXSLT
																.getXSD(request.getResourceParameters().get("xslt"));
														if (daoXSD.containsKey(xsd)) {
															String strxsd = daoXSD.get(xsd);
															String strxslt = daoXSLT.getContent(
																	request.getResourceParameters().get("xslt"));
															if (XMLUtils.validate(strxml, strxsd)) {
																content = XMLUtils.transform(strxml, strxslt);
																status = HTTPResponseStatus.S200;
																response.putParameter("Content-Type",
																		MIME.TEXT_HTML.getMime());
															} else {
																status = HTTPResponseStatus.S400;
															}
														} else
															status = HTTPResponseStatus.S400;
													} else {
														WebServiceConnection webServices2 = new WebServiceConnection(
																this.webConfiguration);
														boolean encontrado2 = false;
														int i2 = 0;
														String lista2;
														String strxsd = null;
														String strxslt = null;
														while (!encontrado2 && i2 < numServers) {
															try {
																lista2 = webServices2.getXSLTList(i2);
																if ((lista2.indexOf(request.getResourceParameters()
																		.get("xslt")) != -1)) {
																	encontrado2 = true;
																	String xsd = webServices2.getXSLTxsd(
																			request.getResourceParameters().get("xslt"),
																			i2);
																	strxsd = webServices2.getXSDContent(xsd, i2);
																	strxslt = webServices2.getXSLTContent(
																			request.getResourceParameters().get("xslt"),
																			i2);
																}
																i2++;
															} catch (WebServiceException e) {
																i2++;
																e.printStackTrace();
															}
														}
														if (encontrado2) {
															if (XMLUtils.validate(strxml, strxsd)) {
																content = XMLUtils.transform(strxml, strxslt);
																status = HTTPResponseStatus.S200;
																response.putParameter("Content-Type",
																		MIME.TEXT_HTML.getMime());
															} else {
																status = HTTPResponseStatus.S400;
															}
														} else {
															status = HTTPResponseStatus.S404;
														}
													}
												} else {
													switch (par) {
													case "html":
														content = webServices.getHTMLContent(
																request.getResourceParameters().get("uuid"), i);
														break;
													case "xml":
														content = webServices.getXMLContent(
																request.getResourceParameters().get("uuid"), i);
														break;
													case "xsd":
														content = webServices.getXSDContent(
																request.getResourceParameters().get("uuid"), i);
														break;
													}
													status = HTTPResponseStatus.S200;
												}
											}
											i++;
										} catch (WebServiceException e) {
											i++;
											e.printStackTrace();
										}
									}
									if (!encontrado) {
										status = HTTPResponseStatus.S404;
									}
								} else {
									status = HTTPResponseStatus.S404;
								}
							}
						} else {
							content += "<html>";
							content += "<b> Listado de Paginas </b>";
							content += "<b> LOCAL SERVER </b>";
							content += dao.getListUUID();
							if (webConfiguration != null) {
								WebServiceConnection webServices = new WebServiceConnection(this.webConfiguration);
								for (int i = 0; i < numServers; i++) {
									try {
										switch (par) {
										case "html":
											content += webServices.getHTMLList(i);
											break;
										case "xml":
											content += webServices.getXMLList(i);
											break;
										default:
											content += webServices.getXSDList(i);
											break;
										}
									} catch (WebServiceException e) {
										e.printStackTrace();
									}
								}
							}
							content += "</html>";
							status = HTTPResponseStatus.S200;
						}
					} else if (request.getMethod() == HTTPRequestMethod.POST) {
						UUID randomUuid = UUID.randomUUID();
						String uuid = randomUuid.toString();
						content = "<a href=\"" + par + "?uuid=" + uuid + "\">" + uuid + "</a>";
						if (request.getResourceParameters().containsKey(par)) {
							dao.create(uuid, request.getResourceParameters().get(par));
							status = HTTPResponseStatus.S200;
						} else {
							status = HTTPResponseStatus.S400;
						}
					}
				} else {
					status = HTTPResponseStatus.S400;
				}
				response.setContent(content);
			} catch (HTTPParseException e) {
				e.printStackTrace();
				status = HTTPResponseStatus.S400;
			} catch (PageNotFoundException e) {
				e.printStackTrace();
				status = HTTPResponseStatus.S404;
			} catch (WebServiceException e) {
				e.printStackTrace();
				status = HTTPResponseStatus.S404;
			} catch (Exception e) {
				e.printStackTrace();
				status = HTTPResponseStatus.S500;
			}
			response.setStatus(status);
			response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
			output.write(response.toString().getBytes());
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
