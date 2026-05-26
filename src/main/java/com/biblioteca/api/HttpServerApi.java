package com.biblioteca.api;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.biblioteca.dao.LibroDAO;
import com.biblioteca.dao.PrestitoDAO;
import com.biblioteca.dao.UtenteDAO;
import com.biblioteca.model.Docente;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestito;
import com.biblioteca.model.Studente;
import com.biblioteca.model.Utente;
import com.biblioteca.service.BibliotecaService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
@SuppressWarnings("restriction")

public class HttpServerApi {

	private static final int PORT = 8080;
	private final BibliotecaService service;
	private final Gson gson;
	private final UtenteDAO utenteDao;
	private final LibroDAO libroDao;
	private final PrestitoDAO prestitoDao;

	public HttpServerApi() {
		this.service = new BibliotecaService();
		this.gson = new Gson();
		this.utenteDao = new UtenteDAO();
		this.libroDao = new LibroDAO();
		this.prestitoDao = new PrestitoDAO();
	}

	public void start() throws IOException {
		HttpServer server = HttpServer.create(new java.net.InetSocketAddress(PORT), 0);

		// Root - serve il frontend
		server.createContext("/", new RootHandler());

		// API endpoints
		server.createContext("/api/utenti", new UtentiHandler());
		server.createContext("/api/libri", new LibriHandler());
		server.createContext("/api/prestiti", new PrestitiHandler());

		server.start();
		System.out.println("✓ Server API avviato su http://localhost:" + PORT);
		System.out.println("✓ Accedi al frontend su http://localhost:" + PORT);
	}

	// ==================== HANDLER ROOT ====================
	private class RootHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			String path = exchange.getRequestURI().getPath();

			try {
				if (path.equals("/")) {
					serveFile(exchange, "index.html", "text/html");
				} else if (path.endsWith(".css")) {
					serveFile(exchange, path.substring(1), "text/css");
				} else if (path.endsWith(".js")) {
					serveFile(exchange, path.substring(1), "application/javascript");
				} else {
					sendError(exchange, 404, "File non trovato");
				}
			} catch (Exception e) {
				sendError(exchange, 500, "Errore interno del server");
			}
		}

		private void serveFile(HttpExchange exchange, String filename, String contentType) throws IOException {
			try (InputStream is = getClass().getClassLoader().getResourceAsStream(filename)) {
				if (is == null) {
					// File non trovato in risorse, invia error
					sendError(exchange, 404, "File non trovato: " + filename);
					return;
				}

				byte[] response = is.readAllBytes();
				exchange.getResponseHeaders().set("Content-Type", contentType);
				exchange.sendResponseHeaders(200, response.length);
				exchange.getResponseBody().write(response);
				exchange.close();
			}
		}
	}

	// ==================== HANDLER UTENTI ====================
	private class UtentiHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			enableCORS(exchange);

			if ("OPTIONS".equals(exchange.getRequestMethod())) {
				exchange.sendResponseHeaders(204, -1);
				return;
			}

			try {
				String method = exchange.getRequestMethod();
				String path = exchange.getRequestURI().getPath();

				if ("GET".equals(method)) {
					if (path.equals("/api/utenti")) {
						getAllUtenti(exchange);
					} else {
						sendError(exchange, 404, "Endpoint non trovato");
					}
				} else if ("POST".equals(method)) {
					String body = readBody(exchange);
					JsonObject json = gson.fromJson(body, JsonObject.class);

					if (json.has("tipo")) {
						String tipo = json.get("tipo").getAsString();
						if ("STUDENTE".equals(tipo)) {
							registraStudente(exchange, json);
						} else if ("DOCENTE".equals(tipo)) {
							registraDocente(exchange, json);
						} else {
							sendError(exchange, 400, "Tipo utente non valido");
						}
					} else {
						sendError(exchange, 400, "Tipo utente mancante");
					}
				} else {
					sendError(exchange, 405, "Metodo non consentito");
				}
			} catch (Exception e) {
				sendError(exchange, 500, "Errore: " + e.getMessage());
			}
		}

		private void getAllUtenti(HttpExchange exchange) throws IOException {
			List<Utente> utenti = utenteDao.getAllUtenti();
			JsonArray jsonArray = new JsonArray();
			
			for (Utente utente : utenti) {
				JsonObject obj = new JsonObject();
				obj.addProperty("idUtente", utente.getIdUtente());
				obj.addProperty("nome", utente.getNome());
				obj.addProperty("cognome", utente.getCognome());
				obj.addProperty("email", utente.getEmail());
				obj.addProperty("tipo", utente.getTipoUtente());
				
				if (utente instanceof Studente) {
					Studente s = (Studente) utente;
					obj.addProperty("classe", s.getClasse());
				} else if (utente instanceof Docente) {
					Docente d = (Docente) utente;
					obj.addProperty("materia", d.getMateria());
				}
				
				// Aggiungi numero di prestiti attivi e massimi
				int prestitiAttivi = prestitoDao.contaPrestitiAttiviUtente(utente.getIdUtente());
				int prestitiMassimi = utente.getNumeroMassimoPrestiti();
				obj.addProperty("prestitiAttivi", prestitiAttivi);
				obj.addProperty("prestitiMassimi", prestitiMassimi);
				
				jsonArray.add(obj);
			}
			sendResponse(exchange, 200, jsonArray.toString());
		}

		private void registraStudente(HttpExchange exchange, JsonObject json) throws IOException {
			try {
				String nome = json.get("nome").getAsString();
				String cognome = json.get("cognome").getAsString();
				String email = json.get("email").getAsString();
				String classe = json.get("classe").getAsString();

				// Usa BibliotecaService per registrare (include tutte le validazioni)
				service.registraStudente(nome, cognome, email, classe);

				JsonObject response = new JsonObject();
				response.addProperty("success", true);
				response.addProperty("message", "Studente registrato con successo");
				sendResponse(exchange, 201, response.toString());
			} catch (IllegalArgumentException e) {
				sendError(exchange, 400, e.getMessage());
			} catch (Exception e) {
				sendError(exchange, 400, "Errore nella registrazione: " + e.getMessage());
			}
		}

		private void registraDocente(HttpExchange exchange, JsonObject json) throws IOException {
			try {
				String nome = json.get("nome").getAsString();
				String cognome = json.get("cognome").getAsString();
				String email = json.get("email").getAsString();
				String materia = json.get("materia").getAsString();

				// Usa BibliotecaService per registrare (include tutte le validazioni)
				service.registraDocente(nome, cognome, email, materia);

				JsonObject response = new JsonObject();
				response.addProperty("success", true);
				response.addProperty("message", "Docente registrato con successo");
				sendResponse(exchange, 201, response.toString());
			} catch (IllegalArgumentException e) {
				sendError(exchange, 400, e.getMessage());
			} catch (Exception e) {
				sendError(exchange, 400, "Errore nella registrazione: " + e.getMessage());
			}
		}
	}

	// ==================== HANDLER LIBRI ====================
	private class LibriHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			enableCORS(exchange);

			if ("OPTIONS".equals(exchange.getRequestMethod())) {
				exchange.sendResponseHeaders(204, -1);
				return;
			}

			try {
				String method = exchange.getRequestMethod();

				if ("GET".equals(method)) {
					getAllLibri(exchange);
				} else if ("POST".equals(method)) {
					String body = readBody(exchange);
					JsonObject json = gson.fromJson(body, JsonObject.class);
					registraLibro(exchange, json);
				} else {
					sendError(exchange, 405, "Metodo non consentito");
				}
			} catch (Exception e) {
				sendError(exchange, 500, "Errore: " + e.getMessage());
			}
		}

		private void getAllLibri(HttpExchange exchange) throws IOException {
			List<Libro> libri = libroDao.getAllLibri();
			String response = gson.toJson(libri);
			sendResponse(exchange, 200, response);
		}

		private void registraLibro(HttpExchange exchange, JsonObject json) throws IOException {
			try {
				String titolo = json.get("titolo").getAsString();
				String autore = json.get("autore").getAsString();
				int annoPubblicazione = json.get("annoPubblicazione").getAsInt();

				// Usa BibliotecaService per registrare (include tutte le validazioni)
				service.registraLibro(titolo, autore, annoPubblicazione);

				JsonObject response = new JsonObject();
				response.addProperty("success", true);
				response.addProperty("message", "Libro registrato con successo");
				sendResponse(exchange, 201, response.toString());
			} catch (IllegalArgumentException e) {
				sendError(exchange, 400, e.getMessage());
			} catch (Exception e) {
				sendError(exchange, 400, "Errore nella registrazione: " + e.getMessage());
			}
		}
	}

	// ==================== HANDLER PRESTITI ====================
	private class PrestitiHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			enableCORS(exchange);

			if ("OPTIONS".equals(exchange.getRequestMethod())) {
				exchange.sendResponseHeaders(204, -1);
				return;
			}

			try {
				String method = exchange.getRequestMethod();

				if ("GET".equals(method)) {
					getAllPrestiti(exchange);
				} else if ("POST".equals(method)) {
					String body = readBody(exchange);
					JsonObject json = gson.fromJson(body, JsonObject.class);

					if (json.has("azione")) {
						String azione = json.get("azione").getAsString();
						if ("CREA".equals(azione)) {
							creaPrestito(exchange, json);
						} else if ("RESTITUISCI".equals(azione)) {
							restituisciLibro(exchange, json);
						} else {
							sendError(exchange, 400, "Azione non valida");
						}
					} else {
						sendError(exchange, 400, "Azione mancante");
					}
				} else {
					sendError(exchange, 405, "Metodo non consentito");
				}
			} catch (Exception e) {
				sendError(exchange, 500, "Errore: " + e.getMessage());
			}
		}

		private void getAllPrestiti(HttpExchange exchange) throws IOException {
			List<Prestito> prestiti = prestitoDao.getAllPrestiti();
			JsonArray jsonArray = new JsonArray();
			
			for (Prestito prestito : prestiti) {
				JsonObject obj = new JsonObject();
				obj.addProperty("idPrestito", prestito.getIdPrestito());
				obj.addProperty("dataPrestito", prestito.getDataPrestito().getTime());
				obj.addProperty("stato", prestito.getStato());
				
				if (prestito.getDataRestituzione() != null) {
					obj.addProperty("dataRestituzione", prestito.getDataRestituzione().getTime());
				}
				
				// Serializza l'utente correttamente
				if (prestito.getUtente() != null) {
					JsonObject utenteObj = new JsonObject();
					Utente u = prestito.getUtente();
					utenteObj.addProperty("idUtente", u.getIdUtente());
					utenteObj.addProperty("nome", u.getNome());
					utenteObj.addProperty("cognome", u.getCognome());
					utenteObj.addProperty("email", u.getEmail());
					utenteObj.addProperty("tipo", u.getTipoUtente());
					
					if (u instanceof Studente) {
						utenteObj.addProperty("classe", ((Studente) u).getClasse());
					} else if (u instanceof Docente) {
						utenteObj.addProperty("materia", ((Docente) u).getMateria());
					}
					obj.add("utente", utenteObj);
				}
				
				// Serializza il libro
				if (prestito.getLibro() != null) {
					JsonObject libroObj = new JsonObject();
					Libro l = prestito.getLibro();
					libroObj.addProperty("idLibro", l.getIdLibro());
					libroObj.addProperty("titolo", l.getTitolo());
					libroObj.addProperty("autore", l.getAutore());
					libroObj.addProperty("annoPubblicazione", l.getAnnoPubblicazione());
					libroObj.addProperty("disponibile", l.isDisponibile());
					obj.add("libro", libroObj);
				}
				
				jsonArray.add(obj);
			}
			sendResponse(exchange, 200, jsonArray.toString());
		}

		private void creaPrestito(HttpExchange exchange, JsonObject json) throws IOException {
			try {
				int idUtente = json.get("idUtente").getAsInt();
				int idLibro = json.get("idLibro").getAsInt();

				// Usa BibliotecaService per creare il prestito (include tutte le validazioni)
				service.creaPrestito(idUtente, idLibro);

				JsonObject response = new JsonObject();
				response.addProperty("success", true);
				response.addProperty("message", "Prestito creato con successo");
				sendResponse(exchange, 201, response.toString());
			} catch (IllegalArgumentException e) {
				sendError(exchange, 400, e.getMessage());
			} catch (Exception e) {
				sendError(exchange, 400, "Errore: " + e.getMessage());
			}
		}

		private void restituisciLibro(HttpExchange exchange, JsonObject json) throws IOException {
			try {
				int idPrestito = json.get("idPrestito").getAsInt();

				// Usa BibliotecaService per restituire il libro (include tutte le validazioni)
				service.restituisciLibro(idPrestito);

				JsonObject response = new JsonObject();
				response.addProperty("success", true);
				response.addProperty("message", "Libro restituito con successo");
				sendResponse(exchange, 200, response.toString());
			} catch (IllegalArgumentException e) {
				sendError(exchange, 400, e.getMessage());
			} catch (Exception e) {
				sendError(exchange, 400, "Errore: " + e.getMessage());
			}
		}
	}

	// ==================== UTILITY ====================
	private void enableCORS(HttpExchange exchange) {
		exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
		exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
	}

	private String readBody(HttpExchange exchange) throws IOException {
		try (InputStream is = exchange.getRequestBody()) {
			return new String(is.readAllBytes(), StandardCharsets.UTF_8);
		}
	}

	protected void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
		byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
		exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
		exchange.sendResponseHeaders(statusCode, responseBytes.length);
		exchange.getResponseBody().write(responseBytes);
		exchange.close();
	}

	protected void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
		JsonObject error = new JsonObject();
		error.addProperty("success", false);
		error.addProperty("error", message);
		sendResponse(exchange, statusCode, error.toString());
	}
}
