package com.biblioteca.model;

public abstract class Utente {

	// Attributes
	private int idUtente;
	private String nome;
	private String cognome;
	private String email;

	// Constructor
	public Utente(int idUtente, String nome, String cognome, String email) {
		this.idUtente = idUtente;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
	}

	// Methods
	public String getNomeCompleto() {
		return nome + " " + cognome;
	}

	public String toString() {
		return "ID: " + idUtente + " | " + getNomeCompleto() + " | " + email + " | " + getTipoUtente() + " | ";
	}

	public abstract int getNumeroMassimoPrestiti();

	public abstract String getTipoUtente();

	// Getters and Setters
	public int getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(int idUtente) {
		this.idUtente = idUtente;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
