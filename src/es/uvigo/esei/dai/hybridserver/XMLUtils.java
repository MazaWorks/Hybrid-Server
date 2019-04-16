package es.uvigo.esei.dai.hybridserver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

public class XMLUtils {
	public static String transform(String strxml, String strxslt)
			throws TransformerException, UnsupportedEncodingException {
		InputStream xml = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
		InputStream xslt = new ByteArrayInputStream(strxslt.getBytes("UTF-8"));
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer(new StreamSource(xslt));

		StringWriter writer = new StringWriter();

		transformer.transform(new StreamSource(xml), new StreamResult(writer));

		return writer.toString();
	}

	public static boolean validate(String strxml, String schema) {
		try {
			StreamSource xsdSchema = new StreamSource(new ByteArrayInputStream(schema.getBytes("UTF-8")));
			InputStream xml = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
			// Construcción del schema
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema2 = schemaFactory.newSchema(xsdSchema);
			// Construcción del parser del documento. Se establece el esquema y se activa
			// la validación y comprobación de namespaces
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);
			factory.setSchema(schema2);

			// Se añade el manejador de errores
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setErrorHandler(new SimpleErrorHandler());
			builder.parse(xml);
			return true;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return false;
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}