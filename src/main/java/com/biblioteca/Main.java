package com.biblioteca;

import java.io.IOException;
import com.biblioteca.api.HttpServerApi;

public class Main {
	public static void main(String[] args) {
		System.out.println("===== GESTIONE BIBLIOTECA SCOLASTICA =====\n");
		
		// Avvia il server REST API
		try {
			HttpServerApi apiServer = new HttpServerApi();
			apiServer.start();
			System.out.println("\n✓ Server è in esecuzione e pronto a ricevere richieste");
			System.out.println("✓ Per utilizzare l'interfaccia web, visita: http://localhost:8080");
		} catch (IOException e) {
			System.err.println("Errore nell'avvio del server: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
