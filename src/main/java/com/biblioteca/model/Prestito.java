package com.biblioteca.model;

import java.util.Date;

public class Prestito {

	// Attributes
	private int idPrestito;
	private Utente utente;
	private Libro libro;
	private Date dataPrestito;
	private Date dataRestituzione;
	private String stato;

	// Constructor
	public Prestito(int idPrestito, Utente utente, Libro libro, Date dataPrestito, Date dataRestituzione,
			String stato) {
		this.idPrestito = idPrestito;
		this.utente = utente;
		this.libro = libro;
		this.dataPrestito = dataPrestito;
		this.dataRestituzione = dataRestituzione;
		this.stato = stato;
	}

	// Methods
	@Override
	public String toString() {
		return "ID: " + idPrestito + " | " + utente + " | " + libro + " | " + dataPrestito + " | " + dataRestituzione
				+ " | " + stato;
	}

	// Getters and Setters
	public int getIdPrestito() {
		return idPrestito;
	}

	public void setIdPrestito(int idPrestito) {
		this.idPrestito = idPrestito;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Libro getLibro() {
		return libro;
	}

	public void setLibro(Libro libro) {
		this.libro = libro;
	}

	public Date getDataPrestito() {
		return dataPrestito;
	}

	public void setDataPrestito(Date dataPrestito) {
		this.dataPrestito = dataPrestito;
	}

	public Date getDataRestituzione() {
		return dataRestituzione;
	}

	public void setDataRestituzione(Date dataRestituzione) {
		this.dataRestituzione = dataRestituzione;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

}
