package com.biblioteca.model;

public class Libro {

	// Attributes
	private int idLibro;
	private String titolo;
	private String autore;
	private int annoPubblicazione;
	private boolean disponibile;

	// Constructor
	public Libro(int idLibro, String titolo, String autore, int annoPubblicazione, boolean disponibile) {
		this.idLibro = idLibro;
		this.titolo = titolo;
		this.autore = autore;
		this.annoPubblicazione = annoPubblicazione;
		this.disponibile = disponibile;
	}

	// Methods
	@Override
	public String toString() {
		return "ID: " + idLibro + " | " + titolo + " | " + autore + " | " + annoPubblicazione + " | " + "Disponibile: "
				+ disponibile;
	}

	// Getters and Setters
	public int getIdLibro() {
		return idLibro;
	}

	public void setIdLibro(int idLibro) {
		this.idLibro = idLibro;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getAutore() {
		return autore;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public int getAnnoPubblicazione() {
		return annoPubblicazione;
	}

	public void setAnnoPubblicazione(int annoPubblicazione) {
		this.annoPubblicazione = annoPubblicazione;
	}

	public boolean isDisponibile() {
		return disponibile;
	}

	public void setDisponibile(boolean disponibile) {
		this.disponibile = disponibile;
	}

}
