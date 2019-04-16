package es.uvigo.esei.dai.hybridserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class Launcher {
	public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {
		if (args.length > 1) {
			System.err.println("Solo se acepta un fichero de configuración o ninguno(accede a la configuración previa)");
		} else if (args.length == 0) {
			HybridServer server = new HybridServer();
			server.start();
		} else if (args.length == 1) {
			File file = new File(args[0]);
			Properties properties = new Properties();
			properties.load(new FileInputStream(file));

			HybridServer server = new HybridServer(properties);
			server.start();
		}
	}
}