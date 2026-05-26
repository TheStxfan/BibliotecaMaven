package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.biblioteca.model.Libro;
import com.biblioteca.util.DatabaseConnection;

public class LibroDAO {

	private static final String INSERT_LIBRO = "INSERT INTO libri (titolo, autore, anno_pubblicazione, disponibile) VALUES (?, ?, ?, ?)";
	private static final String SELECT_LIBRO_BY_ID = "SELECT * FROM libri l " + "WHERE l.id_libro = ?";
	private static final String SELECT_ALL_LIBRO = "SELECT * FROM libri";
	private static final String SELECT_LIBRO_BY_DISPONIBILE = "SELECT * FROM libri l " + "WHERE l.disponibile = ?";
	private static final String UPDATE_DISPONIBILE_BY_ID = "UPDATE libri " + "SET disponibile = ? "
			+ "WHERE id_libro = ?";

	public void inserisciLibro(Libro libro) {
		try (Connection connection = DatabaseConnection.getConnessione();
				PreparedStatement ps = connection.prepareStatement(INSERT_LIBRO)) {
			ps.setString(1, libro.getTitolo());
			ps.setString(2, libro.getAutore());
			ps.setInt(3, libro.getAnnoPubblicazione());
			ps.setBoolean(4, libro.isDisponibile());
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Libro cercaLibroPerId(int idLibro) {
		Libro libro = null;

		try (Connection connection = DatabaseConnection.getConnessione();
				PreparedStatement ps = connection.prepareStatement(SELECT_LIBRO_BY_ID)) {
			ps.setInt(1, idLibro);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					libro = new Libro(rs.getInt("id_libro"), rs.getString("titolo"), rs.getString("autore"),
							rs.getInt("anno_pubblicazione"), rs.getBoolean("disponibile"));

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return libro;

	}

	public void stampaTuttiILibri() {
		Libro libro = null;

		try (Connection connection = DatabaseConnection.getConnessione();
				PreparedStatement ps = connection.prepareStatement(SELECT_ALL_LIBRO)) {

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					libro = new Libro(rs.getInt("id_libro"), rs.getString("titolo"), rs.getString("autore"),
							rs.getInt("anno_pubblicazione"), rs.getBoolean("disponibile"));

					System.out.println(libro.toString());
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void stampaLibriDisponibili() {
		Libro libro = null;

		try (Connection connection = DatabaseConnection.getConnessione();
				PreparedStatement ps = connection.prepareStatement(SELECT_LIBRO_BY_DISPONIBILE)) {
			ps.setBoolean(1, true);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					libro = new Libro(rs.getInt("id_libro"), rs.getString("titolo"), rs.getString("autore"),
							rs.getInt("anno_pubblicazione"), rs.getBoolean("disponibile"));

					System.out.println(libro.toString());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void aggiornaDisponibilita(int idLibro, boolean disponibile) {

		try (Connection connection = DatabaseConnection.getConnessione();
				PreparedStatement ps = connection.prepareStatement(UPDATE_DISPONIBILE_BY_ID)) {
			ps.setBoolean(1, disponibile);
			ps.setInt(2, idLibro);
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public List<Libro> getAllLibri() {
		List<Libro> libri = new ArrayList<>();
		Libro libro = null;

		try (Connection connection = DatabaseConnection.getConnessione();
				PreparedStatement ps = connection.prepareStatement(SELECT_ALL_LIBRO)) {

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					libro = new Libro(rs.getInt("id_libro"), rs.getString("titolo"), rs.getString("autore"),
							rs.getInt("anno_pubblicazione"), rs.getBoolean("disponibile"));

					libri.add(libro);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return libri;
	}
}
