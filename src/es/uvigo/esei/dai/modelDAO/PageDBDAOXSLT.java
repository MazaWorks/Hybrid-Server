package es.uvigo.esei.dai.modelDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PageDBDAOXSLT{
	String dbUrl, dbUser, dbPass;

	public PageDBDAOXSLT(String dbUrl, String dbUser, String dbPass) {
		this.dbUrl = dbUrl;
		this.dbUser = dbUser;
		this.dbPass = dbPass;
	}

	public void create(String uuid, String content, String xsd) {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
				PreparedStatement statement = connection.prepareStatement(
						"INSERT INTO XSLT (uuid, content, xsd) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, uuid);
			statement.setString(2, content);
			statement.setString(3, xsd);

			if (statement.executeUpdate() != 1)
				throw new SQLException("Error insertando pagina");
			statement.close();
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void update(String uuid, String content, String xsd) throws PageNotFoundException {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
				PreparedStatement statement = connection.prepareStatement("UPDATE XSLT SET content=?, xsd=? WHERE uuid=?")) {
			statement.setString(1, content);
			statement.setString(2, xsd);
			statement.setString(3, uuid);

			if (statement.executeUpdate() != 1)
				throw new SQLException("Error actualizando pagina");
			statement.close();
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void delete(String uuid) throws PageNotFoundException {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
				PreparedStatement statement = connection.prepareStatement("DELETE FROM XSLT WHERE uuid=?")) {
			statement.setString(1, uuid);

			if (statement.executeUpdate() != 1)
				throw new PageNotFoundException("No se ha encontrado la pagina");
			statement.close();
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public String getContent(String uuid) throws PageNotFoundException {
		String toret = "";
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM XSLT WHERE uuid=?")) {
			statement.setString(1, uuid);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					toret = result.getString("content");
				} else {
					throw new PageNotFoundException(uuid);
				}
			}
			statement.close();
			connection.close();
			return toret;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getXSD(String uuid) throws PageNotFoundException {
		String toret = "";
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM XSLT WHERE uuid=?")) {
			statement.setString(1, uuid);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					toret = result.getString("xsd");
				} else {
					throw new PageNotFoundException(uuid);
				}
			}
			statement.close();
			connection.close();
			return toret;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean containsKey(String uuid) {
		boolean toret = false;
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM XSLT WHERE uuid=?")) {
			statement.setString(1, uuid);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					toret = true;
				}
			}
			statement.close();
			connection.close();
			return toret;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean xsdExist(String xsd) {
		boolean toret = false;
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM XSLT WHERE xsd=?")) {
			statement.setString(1, xsd);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					toret = true;
				}
			}
			statement.close();
			connection.close();
			return toret;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public int size() {
		int toret = 0;
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
				PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM XSLT AS count")) {
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					toret = result.getInt("count");
				}
			}
			statement.close();
			connection.close();
			return toret;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public String getListUUID() {
		String toret = "";
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
				PreparedStatement statement = connection.prepareStatement("SELECT uuid FROM XSLT")) {
			try (ResultSet result = statement.executeQuery()) {
				toret += "<ul>";
				while (result.next()) {
					toret += "<li>";
					toret += "<a href=\"xml?uuid=" + result.getString("uuid") + "\">" + result.getString("uuid")
							+ "</a>";
				}
				toret += "</ul>";
			}
			statement.close();
			connection.close();
			return toret;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}