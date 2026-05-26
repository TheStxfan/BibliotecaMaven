package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.biblioteca.model.Docente;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestito;
import com.biblioteca.model.Studente;
import com.biblioteca.model.Utente;
import com.biblioteca.util.DatabaseConnection;

public class PrestitoDAO {

	private static final String INSERT_PRESTITO = "INSERT INTO prestiti (id_utente, id_libro, data_prestito, stato) VALUES (?, ?, ?, ?)";
	private static final String SELECT_PRESTITO_BY_ID = "SELECT * FROM prestiti p "
			+ "JOIN libri l on p.id_libro = l.id_libro " + "JOIN utenti u on p.id_utente = u.id_utente "
			+ "LEFT JOIN docenti d ON u.id_utente = d.id_utente " + "LEFT JOIN studenti s ON u.id_utente = s.id_utente "
			+ "WHERE p.id_prestito = ?";
	private static final String UPDATE_RESTITUZIONE_BY_ID = "UPDATE prestiti "
			+ "SET data_restituzione = ?, stato = 'RESTITUITO' " + "WHERE id_prestito = ?";
	private static final String SELECT_COUNT_PRESTITI_ATTIVI_BY_ID_UTENTE = "SELECT COUNT(*) as totale FROM prestiti "
			+ "WHERE stato = 'ATTIVO' and id_utente = ?";
	private static final String SELECT_LIBRO_PRESTITO_ATTIVO_BY_ID_LIBRO = "SELECT stato FROM prestiti "
			+ "WHERE stato = 'ATTIVO' and id_libro = ?";
	private static final String SELECT_PRESTITI_ATTIVI = "SELECT * FROM prestiti p "
			+ "JOIN libri l on p.id_libro = l.id_libro " + "JOIN utenti u on p.id_utente = u.id_utente "
			+ "LEFT JOIN docenti d ON u.id_utente = d.id_utente " + "LEFT JOIN studenti s ON u.id_utente = s.id_utente "
			+ "WHERE stato = 'ATTIVO'";
	private static final String SELECT_PRESTITI_BY_ID_UTENTE = "SELECT * FROM prestiti p "
			+ "JOIN libri l on p.id_libro = l.id_libro " + "JOIN utenti u on p.id_utente = u.id_utente "
			+ "LEFT JOIN docenti d ON u.id_utente = d.id_utente " + "LEFT JOIN studenti s ON u.id_utente = s.id_utente "
			+ "WHERE u.id_utente = ?";

	public void inserisciPrestito(int idUtente, int idLibro) {
		try (Connection connection = DatabaseConnection.getConnessione();
				PreparedStatement ps = connection.prepareStatement(INSERT_PRESTITO)) {
			ps.setInt(1, idUtente);
			ps.setInt(2, idLibro);
			ps.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
			ps.setString(4, "ATTIVO");
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public Prestito cercaPrestitoPerId(int idPrestito) {
		Utente utente = null;
		Libro libro = null;		
		Prestito prestito = null;

		try (Connection connection = DatabaseConnection.getConnessione();
				PreparedStatement ps = connection.prepareStatement(SELECT_PRESTITO_BY_ID)) {
			ps.setInt(1, idPrestito);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String tipo = rs.getString("tipo_utente");

					if (tipo.equals("STUDENTE")) {
						utente = new Studente(rs.getInt("id_utente"), rs.getString("nome"), rs.getString("cognome"),
								rs.getString("email"), rs.getString("classe"));

					} else if (tipo.equals("DOCENTE")) {
						utente = new Docente(rs.getInt("id_utente"), rs.getString("nome"), rs.getString("cognome"),
								rs.getString("email"), rs.getString("materia"));
					}

					libro = new Libro(rs.getInt("id_libro"), rs.getString("titolo"), rs.getString("autore"),
							rs.getInt("anno_pubblicazione"), rs.getBoolean("disponibile"));

					prestito = new Prestito(rs.getInt("id_prestito"), utente, libro, rs.getDate("data_prestito"),
							rs.getDate("data_restituzione"), rs.getString("stato"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return prestito;
	}

	public void restituisciLibro(int idPrestito) {

		try (Connection connection = DatabaseConnection.getConnessione();
				PreparedStatement ps = connection.prepareStatement(UPDATE_RESTITUZIONE_BY_ID)) {
			ps.setDate(1, java.sql.Date.valueOf(java.time.LocalDate.now()));
			ps.setInt(2, idPrestito);
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int contaPrestitiAttiviUtente(int idUtente) {
		int prestitiAttivi = 0;

		try (Connection connection = DatabaseConnection.getConnessione();
				PreparedStatement ps = connection.prepareStatement(SELECT_COUNT_PRESTITI_ATTIVI_BY_ID_UTENTE)) {
			ps.setInt(1, idUtente);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					prestitiAttivi = rs.getInt("totale");

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return prestitiAttivi;

	}

	public boolean libroHaPrestitoAttivo(int idLibro) {
		boolean prestitoAttivo = false;

		try (Connection connection = DatabaseConnection.getConnessione();
				PreparedStatement ps = connection.prepareStatement(SELECT_LIBRO_PRESTITO_ATTIVO_BY_ID_LIBRO)) {
			ps.setInt(1, idLibro);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					prestitoAttivo = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return prestitoAttivo;
	}

	public void stampaPrestitiAttivi() {
		Utente utente = null;
		Libro libro = null;
		Prestito prestito = null;

		try (Connection connection = DatabaseConnection.getConnessione();
				PreparedStatement ps = connection.prepareStatement(SELECT_PRESTITI_ATTIVI)) {

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String tipo = rs.getString("tipo_utente");

					if (tipo.equals("STUDENTE")) {
						utente = new Studente(rs.getInt("id_utente"), rs.getString("nome"), rs.getString("cognome"),
								rs.getString("email"), rs.getString("classe"));

					} else if (tipo.equals("DOCENTE")) {
						utente = new Docente(rs.getInt("id_utente"), rs.getString("nome"), rs.getString("cognome"),
								rs.getString("email"), rs.getString("materia"));
					}

					libro = new Libro(rs.getInt("id_libro"), rs.getString("titolo"), rs.getString("autore"),
							rs.getInt("anno_pubblicazione"), rs.getBoolean("disponibile"));

					prestito = new Prestito(rs.getInt("id_prestito"), utente, libro, rs.getDate("data_prestito"),
							rs.getDate("data_restituzione"), rs.getString("stato"));

					System.out.println(prestito.toString());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void stampaStoricoPrestitiUtente(int idUtente) {
		Utente utente = null;
		Libro libro = null;
		Prestito prestito = null;

		try (Connection connection = DatabaseConnection.getConnessione();
				PreparedStatement ps = connection.prepareStatement(SELECT_PRESTITI_BY_ID_UTENTE)) {
			ps.setInt(1, idUtente);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String tipo = rs.getString("tipo_utente");

					if (tipo.equals("STUDENTE")) {
						utente = new Studente(rs.getInt("id_utente"), rs.getString("nome"), rs.getString("cognome"),
								rs.getString("email"), rs.getString("classe"));

					} else if (tipo.equals("DOCENTE")) {
						utente = new Docente(rs.getInt("id_utente"), rs.getString("nome"), rs.getString("cognome"),
								rs.getString("email"), rs.getString("materia"));
					}

					libro = new Libro(rs.getInt("id_libro"), rs.getString("titolo"), rs.getString("autore"),
							rs.getInt("anno_pubblicazione"), rs.getBoolean("disponibile"));

					prestito = new Prestito(rs.getInt("id_prestito"), utente, libro, rs.getDate("data_prestito"),
							rs.getDate("data_restituzione"), rs.getString("stato"));

					System.out.println(prestito.toString());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Prestito> getAllPrestiti() {
		List<Prestito> prestiti = new ArrayList<>();
		Utente utente = null;
		Libro libro = null;
		Prestito prestito = null;

		try (Connection connection = DatabaseConnection.getConnessione();
				PreparedStatement ps = connection.prepareStatement(SELECT_PRESTITI_ATTIVI)) {

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String tipo = rs.getString("tipo_utente");

					if (tipo.equals("STUDENTE")) {
						utente = new Studente(rs.getInt("id_utente"), rs.getString("nome"), rs.getString("cognome"),
								rs.getString("email"), rs.getString("classe"));

					} else if (tipo.equals("DOCENTE")) {
						utente = new Docente(rs.getInt("id_utente"), rs.getString("nome"), rs.getString("cognome"),
								rs.getString("email"), rs.getString("materia"));
					}

					libro = new Libro(rs.getInt("id_libro"), rs.getString("titolo"), rs.getString("autore"),
							rs.getInt("anno_pubblicazione"), rs.getBoolean("disponibile"));

					prestito = new Prestito(rs.getInt("id_prestito"), utente, libro, rs.getDate("data_prestito"),
							rs.getDate("data_restituzione"), rs.getString("stato"));

					prestiti.add(prestito);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return prestiti;
	}

}
