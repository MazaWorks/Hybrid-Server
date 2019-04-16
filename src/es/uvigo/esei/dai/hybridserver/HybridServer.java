package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.xml.ws.Endpoint;

import es.uvigo.esei.dai.serviceWeb.Services;

public class HybridServer {
	private Endpoint endpoint;
	private Thread serverThread;
	private String webService=null;
	private boolean stop;
	private int port = 8888, numClients = 50;
	private String DAOConfiguration[] = { "jdbc:mysql://localhost:3306/hstestdb", "hsdb", "hsdbpass" };
	private final ExecutorService threadPool = Executors.newFixedThreadPool(numClients);
	private List<ServerConfiguration> webConfiguration;

	public HybridServer() {
		this.start();
	}

	public HybridServer(Properties properties) throws SQLException {
		this.port = Integer.parseInt(properties.getProperty("port"));
		this.numClients = Integer.parseInt(properties.getProperty("numClients"));
		String dbUrl = properties.getProperty("db.url");
		String dbUser = properties.getProperty("db.user");
		String dbPassword = properties.getProperty("db.password");
		this.DAOConfiguration = new String[] { dbUrl, dbUser, dbPassword };
	}

	public HybridServer(Configuration configuration) {
		this.port = configuration.getHttpPort();
		this.numClients = configuration.getNumClients();
		String dbUrl = configuration.getDbURL();
		String dbUser = configuration.getDbUser();
		String dbPassword = configuration.getDbPassword();
		this.DAOConfiguration = new String[] { dbUrl, dbUser, dbPassword };
		this.webService = configuration.getWebServiceURL();
		this.webConfiguration = configuration.getServers();
	}

	public String[] getDBConfiguration() {
		return DAOConfiguration;
	}

	public int getPort() {
		return port;
	}

	public void start() {
		if (this.webService!=null) {
			this.endpoint = Endpoint.publish(this.webService, new Services(DAOConfiguration));
			endpoint.setExecutor(Executors.newFixedThreadPool(20));
		}
		this.serverThread = new Thread() {
			@Override
			public void run() {
				try (final ServerSocket serverSocket = new ServerSocket(getPort())) {
					while (true) {
						Socket socket = serverSocket.accept();
						if (stop)
							break;
						if (webService!=null) {
							threadPool.execute(new ServiceThread(socket, DAOConfiguration, webConfiguration));
						} else {
							threadPool.execute(new ServiceThread(socket, DAOConfiguration));
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		this.stop = false;
		this.serverThread.start();
	}

	public void stop() {
		this.stop = true;

		try (Socket socket = new Socket("localhost", getPort())) {
			// Esta conexión se hace, simplemente, para "despertar" el hilo servidor
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		try {
			this.serverThread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		threadPool.shutdownNow();

		try {
			threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.serverThread = null;
		
		if (webService!=null) {
			this.endpoint.stop();
		}
	}
}
