package es.uvigo.esei.dai.modelDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PageDBDAOXML implements PageDAO{
	String dbUrl, dbUser, dbPass;

	public PageDBDAOXML(String dbUrl, String dbUser, String dbPass) {
		this.dbUrl = dbUrl;
		this.dbUser = dbUser;
		this.dbPass = dbPass;
	}

	public void create(String uuid, String content) {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
				PreparedStatement statement = connection.prepareStatement(
						"INSERT INTO XML (uuid, content) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, uuid);
			statement.setString(2, content);

			if (statement.executeUpdate() != 1)
				throw new SQLException("Error insertando pagina");
			statement.close();
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void update(String uuid, String content) throws PageNotFoundException {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
				PreparedStatement statement = connection.prepareStatement("UPDATE XML SET content=? WHERE uuid=?")) {
			statement.setString(1, content);
			statement.setString(2, uuid);

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
				PreparedStatement statement = connection.prepareStatement("DELETE FROM XML WHERE uuid=?")) {
			statement.setString(1, uuid);

			if (statement.executeUpdate() != 1)
				throw new PageNotFoundException("No se ha encontrado la pagina");
			statement.close();
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public String get(String uuid) throws PageNotFoundException {
		String toret = "";
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM XML WHERE uuid=?")) {
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

	public boolean containsKey(String uuid) {
		boolean toret = false;
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM XML WHERE uuid=?")) {
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

	public int size() {
		int toret = 0;
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
				PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM XML AS count")) {
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
				PreparedStatement statement = connection.prepareStatement("SELECT uuid FROM XML")) {
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