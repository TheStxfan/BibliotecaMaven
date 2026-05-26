package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.biblioteca.model.Docente;
import com.biblioteca.model.Studente;
import com.biblioteca.model.Utente;
import com.biblioteca.util.DatabaseConnection;

public class UtenteDAO {

	private static final String INSERT_UTENTE = "INSERT INTO utenti (nome, cognome, email, tipo_utente) VALUES (?, ?, ?, ?)";
	private static final String INSERT_STUDENTE = "INSERT INTO studenti (id_utente, classe) VALUES (?, ?)";
	private static final String INSERT_DOCENTE = "INSERT INTO docenti (id_utente, materia) VALUES (?, ?)";
	private static final String SELECT_UTENTE_BY_ID = "SELECT * FROM utenti u "
			+ "LEFT JOIN docenti d ON u.id_utente = d.id_utente " + "LEFT JOIN studenti s ON u.id_utente = s.id_utente "
			+ "WHERE u.id_utente = ?";
	private static final String SELECT_UTENTE_BY_EMAIL = "SELECT * FROM utenti u "
			+ "LEFT JOIN docenti d ON u.id_utente = d.id_utente " + "LEFT JOIN studenti s ON u.id_utente = s.id_utente "
			+ "WHERE u.email = ?";
	private static final String SELECT_ALL_UTENTE = "SELECT * FROM utenti u "
			+ "LEFT JOIN docenti d ON u.id_utente = d.id_utente "
			+ "LEFT JOIN studenti s ON u.id_utente = s.id_utente";

	public void inserisciUtente(Utente utente) {

		try (Connection connection = DatabaseConnection.getConnessione();
				PreparedStatement psUtente = connection.prepareStatement(INSERT_UTENTE,
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			psUtente.setString(1, utente.getNome());
			psUtente.setString(2, utente.getCognome());
			psUtente.setString(3, utente.getEmail());
			psUtente.setString(4, utente.getTipoUtente());
			psUtente.executeUpdate();

			try (ResultSet rs = psUtente.getGeneratedKeys()) {
				if (rs.next()) {
					int idGenerato = rs.getInt(1);

					if (utente instanceof Studente) {
						try (PreparedStatement psStudente = connection.prepareStatement(INSERT_STUDENTE)) {
							Studente studente = (Studente) utente;
							psStudente.setInt(1, idGenerato);
							psStudente.setString(2, studente.getClasse());
							psStudente.executeUpdate();

						} catch (SQLException e) {
							e.printStackTrace();
						}

					} else if (utente instanceof Docente) {
						try (PreparedStatement psDocente = connection.prepareStatement(INSERT_DOCENTE)) {
							Docente docente = (Docente) utente;
							psDocente.setInt(1, idGenerato);
							psDocente.setString(2, docente.getMateria());
							psDocente.executeUpdate();

						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Utente cercaUtentePerId(int idUtente) {
		Utente utente = null;

		try (Connection connection = DatabaseConnection.getConnessione();
				PreparedStatement ps = connection.prepareStatement(SELECT_UTENTE_BY_ID)) {
			ps.setInt(1, idUtente);

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
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return utente;
	}

	public Utente cercaUtentePerEmail(String email) {
		Utente utente = null;

		try (Connection connection = DatabaseConnection.getConnessione();
				PreparedStatement ps = connection.prepareStatement(SELECT_UTENTE_BY_EMAIL)) {
			ps.setString(1, email);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String tipo = rs.getString("tipo_utente");

					if (tipo.equals("STUDENTE")) {
						utente = new Studente(rs.getInt("id_utente"), rs.getString("nome"), rs.getString("cognome"),
								email, rs.getString("classe"));

					} else if (tipo.equals("DOCENTE")) {
						utente = new Docente(rs.getInt("id_utente"), rs.getString("nome"), rs.getString("cognome"),
								email, rs.getString("materia"));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return utente;

	}

	public void stampaTuttiGliUtenti() {
		Utente utente = null;

		try (Connection connection = DatabaseConnection.getConnessione();
				PreparedStatement ps = connection.prepareStatement(SELECT_ALL_UTENTE)) {

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
					
					System.out.println(utente.toString());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public List<Utente> getAllUtenti() {
		List<Utente> utenti = new ArrayList<>();
		Utente utente = null;

		try (Connection connection = DatabaseConnection.getConnessione();
				PreparedStatement ps = connection.prepareStatement(SELECT_ALL_UTENTE)) {

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
					
					if (utente != null) {
						utenti.add(utente);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return utenti;
	}
}
