/**
 *  HybridServer
 *  Copyright (C) 2017 Miguel Reboiro-Jato
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
package es.uvigo.esei.dai.hybridserver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLConfigurationLoader {
	public Configuration load(File xmlFile)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {

		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(new File("configuration.xsd"));

		// Construcción del parser del documento. Se establece el esquema y se
		// activa la validación y comprobación de namespaces
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);
		factory.setSchema(schema);
		// Se añade el manejador de errores
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new SimpleErrorHandler());

		// Parsing del documento
		Document doc = builder.parse(xmlFile);
		Configuration config = new Configuration();
		List<ServerConfiguration> serverConfigList= new ArrayList<ServerConfiguration>();
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("connections");
		Node nNode = nList.item(0);
		Element eElement = (Element) nNode;
		config.setHttpPort(Integer.parseInt(eElement.getElementsByTagName("http").item(0).getTextContent()));
		config.setWebServiceURL(eElement.getElementsByTagName("webservice").item(0).getTextContent());
		config.setNumClients(Integer.parseInt(eElement.getElementsByTagName("numClients").item(0).getTextContent()));

		nList = doc.getElementsByTagName("database");
		nNode = nList.item(0);
		eElement = (Element) nNode;
		config.setDbUser(eElement.getElementsByTagName("user").item(0).getTextContent());
		config.setDbPassword(eElement.getElementsByTagName("password").item(0).getTextContent());
		config.setDbURL(eElement.getElementsByTagName("url").item(0).getTextContent());

		nList = doc.getElementsByTagName("servers");
		nNode = nList.item(0);
		eElement = (Element) nNode;
		NodeList nList2 = eElement.getElementsByTagName("server");
		for (int temp = 0; temp < nList2.getLength(); temp++) {
			Node nNode2 = nList2.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement2 = (Element) nNode2;
				ServerConfiguration serverConfig = new ServerConfiguration();
				serverConfig.setName(eElement2.getAttribute("name"));
				serverConfig.setWsdl(eElement2.getAttribute("wsdl"));
				serverConfig.setNamespace(eElement2.getAttribute("namespace"));
				serverConfig.setService(eElement2.getAttribute("service"));
				serverConfig.setHttpAddress(eElement2.getAttribute("httpAddress"));
				serverConfigList.add(serverConfig);
			}
		}
		config.setServers(serverConfigList);
		return config;
	}
}
