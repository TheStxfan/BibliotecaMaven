package com.biblioteca.service;

import com.biblioteca.dao.LibroDAO;
import com.biblioteca.dao.PrestitoDAO;
import com.biblioteca.dao.UtenteDAO;
import com.biblioteca.model.Docente;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestito;
import com.biblioteca.model.Studente;
import com.biblioteca.model.Utente;

public class BibliotecaService {

	// Attributes
	private final UtenteDAO utenteDao;
	private final LibroDAO libroDao;
	private final PrestitoDAO prestitoDao;

	// Constructor
	public BibliotecaService() {
		this.utenteDao = new UtenteDAO();
		this.libroDao = new LibroDAO();
		this.prestitoDao = new PrestitoDAO();
	}

	public void registraStudente(String nome, String cognome, String email, String classe) {

		// Check validita' campi
		if (nome == null || nome.trim().isEmpty() || cognome == null || cognome.trim().isEmpty() || email == null
				|| email.trim().isEmpty() || classe == null || classe.trim().isEmpty()) {
			System.out.println("Errore: Tutti i campi sono obbligatori per la registrazione.");
			return;
		}

		// Check validita' email
        if (!isEmailValida(email)) {
            System.out.println("Errore: L'indirizzo email inserito non è valido.");
            return;
        }

		// Check validita' nome
		if (!isLunghezzaValida(nome, 50)) {
			System.out.println("Errore: Il nome inserito è troppo lungo.");
			return;
		}

		// Check validita' cognome
		if (!isLunghezzaValida(cognome, 50)) {
			System.out.println("Errore: Il cognome inserito è troppo lungo.");
			return;
		}

		// Check validita' classe
		if (!isLunghezzaValida(classe, 10)) {
			System.out.println("Errore: Il formato della classe non è valido.");
			return;
		}

		// Check esistenza utente
		if (utenteDao.cercaUtentePerEmail(email) != null) {
			System.out.println("Errore: Un utente con questa email è già presente.");
			return;
		}

		// Inserimento utente nel DB
		Studente studente = new Studente(0, nome, cognome, email, classe.toUpperCase());
		utenteDao.inserisciUtente(studente);
		System.out.println("Studente " + nome + " " + cognome + " registrato con successo!");
	}

	public void registraDocente(String nome, String cognome, String email, String materia) {

		// Check validita' campi
		if (nome == null || nome.trim().isEmpty() || cognome == null || cognome.trim().isEmpty() || email == null
				|| email.trim().isEmpty() || materia == null || materia.trim().isEmpty()) {
			System.out.println("Errore: Tutti i campi sono obbligatori per la registrazione.");
			return;
		}

		// Check validita' email
        if (!isEmailValida(email)) {
            System.out.println("Errore: L'indirizzo email inserito non è valido.");
            return;
        }

		// Check validita' nome
		if (!isLunghezzaValida(nome, 50)) {
			System.out.println("Errore: Il nome inserito è troppo lungo.");
			return;
		}

		// Check validita' cognome
		if (!isLunghezzaValida(cognome, 50)) {
			System.out.println("Errore: Il cognome inserito è troppo lungo.");
			return;
		}

		// Check validita' materia
		if (!isLunghezzaValida(materia, 50)) {
			System.out.println("Errore: Il nome della materia è troppo lungo.");
			return;
		}

		// Check esistenza utente
		if (utenteDao.cercaUtentePerEmail(email) != null) {
			System.out.println("Errore: Un utente con questa email è già presente.");
			return;
		}

		// Inserimento utente nel DB
		Docente docente = new Docente(0, nome, cognome, email, materia);
		utenteDao.inserisciUtente(docente);
		System.out.println("Docente '" + nome + " " + cognome + "' registrato con successo!");
	}

	public void registraLibro(String titolo, String autore, int annoPubblicazione) {

		// Check validita' campi
		if (titolo == null || titolo.trim().isEmpty() || autore == null || autore.trim().isEmpty()) {
			System.out.println("Errore: Titolo e autore sono obbligatori per registrare un libro.");
			return;
		}

		// Check validita' titolo
		if (!isLunghezzaValida(titolo, 100)) {
			System.out.println("Errore: Il titolo inserito è troppo lungo.");
			return;
		}

		// Check validita' autore
		if (!isLunghezzaValida(autore, 100)) {
			System.out.println("Errore: L'autore inserito è troppo lungo.");
			return;
		}

		// Check validita' anno di pubblicazione
		int annoCorrente = java.time.LocalDate.now().getYear();

		if (annoPubblicazione < 0 || annoPubblicazione > annoCorrente) {
			System.out.println("Errore: L'anno di pubblicazione inserito non è valido.");
			return;
		}

		// Inserimento libro nel DB
		Libro libro = new Libro(0, titolo, autore, annoPubblicazione, true);
		libroDao.inserisciLibro(libro);
		System.out.println("Libro '" + titolo + "' registrato con successo nel catalogo!");
	}

	public void creaPrestito(int idUtente, int idLibro) {

		// Check esistenza utente e libro
		Utente utente = utenteDao.cercaUtentePerId(idUtente);
		Libro libro = libroDao.cercaLibroPerId(idLibro);

		if (utente == null) {
			System.out.println("Errore: Utente non trovato nel sistema.");
			return;
		}

		if (libro == null) {
			System.out.println("Errore: Libro non trovato nel catalogo.");
			return;
		}

		// Check disponibilità libro
		if (!libro.isDisponibile()) {
			System.out.println("Errore: Il libro '" + libro.getTitolo() + "' è già in prestito.");
			return;
		}

		// Check limite prestiti utente
		int prestitiAttivi = prestitoDao.contaPrestitiAttiviUtente(idUtente);
		int limiteMassimo = utente.getNumeroMassimoPrestiti();

		if (prestitiAttivi >= limiteMassimo) {
			System.out.println("Errore: L'utente " + utente.getNomeCompleto()
					+ " ha già raggiunto il suo limite massimo di " + limiteMassimo + " prestiti.");
			return;
		}

		// Inserimento prestito nel DB
		prestitoDao.inserisciPrestito(idUtente, idLibro);
		libroDao.aggiornaDisponibilita(idLibro, false);

		System.out.println("Prestito registrato! Il libro '" + libro.getTitolo() + "' è stato affidato a "
				+ utente.getNomeCompleto() + ".");
	}

	public void restituisciLibro(int idPrestito) {

		// Check esistenza prestito
		Prestito prestito = prestitoDao.cercaPrestitoPerId(idPrestito);

		if (prestito == null) {
			System.out.println("Errore: Impossibile trovare un prestito con questo ID.");
			return;
		}

		// Check restituzione prestito
		if ("RESTITUITO".equals(prestito.getStato())) {
			System.out.println("Errore: Questo libro è già stato restituito.");
			return;
		}

		// Aggiornamento restituzione nel DB
		prestitoDao.restituisciLibro(idPrestito);
		int idLibro = prestito.getLibro().getIdLibro();
		libroDao.aggiornaDisponibilita(idLibro, true);

		System.out.println(
				"La restituzione del libro '" + prestito.getLibro().getTitolo() + "' è stata registrata con successo!");
	}

	public void visualizzaTuttiGliUtenti() {
		utenteDao.stampaTuttiGliUtenti();
	}

	public void visualizzaTuttiILibri() {
		libroDao.stampaTuttiILibri();
	}

	public void visualizzaLibriDisponibili() {
		libroDao.stampaLibriDisponibili();
	}

	public void visualizzaPrestitiAttivi() {
		prestitoDao.stampaPrestitiAttivi();
	}

	public void visualizzaStoricoPrestitiUtente(int idUtente) {
		if (utenteDao.cercaUtentePerId(idUtente) == null) {
			System.out.println("Errore: Utente con ID '" + idUtente + "' non trovato nel sistema.");
			return;
		}
		prestitoDao.stampaStoricoPrestitiUtente(idUtente);
	}

	// Check email valida	
	private boolean isEmailValida(String email) {
	    email = email.trim();

		if (email.isEmpty()) {
	        return false;
	    }

	    int posChiocciola = email.indexOf("@");
	    int posUltimoPunto = email.lastIndexOf(".");

	    if (posChiocciola < 1 ||
	        posUltimoPunto <= posChiocciola + 1 ||
	        posUltimoPunto == email.length() - 1 ||
	        email.contains(" ")) {
	        return false;
	    }

	    if (email.indexOf("@") != email.lastIndexOf("@")) {
	        return false;
	    }
	    return true;
	}

	// Check stringa valida
	private boolean isLunghezzaValida(String testo, int max) {
		return testo != null && testo.trim().length() <= max;
	}

}
