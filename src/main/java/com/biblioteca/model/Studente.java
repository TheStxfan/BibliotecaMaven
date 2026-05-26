package com.biblioteca.model;

public class Studente extends Utente {

	// Attributes
	private String classe;

	// Constructor
	public Studente(int idUtente, String nome, String cognome, String email, String classe) {
		super(idUtente, nome, cognome, email);
		this.classe = classe;
	}

	// Overrides
	@Override
	public int getNumeroMassimoPrestiti() {
		return 3;
	}

	@Override
	public String getTipoUtente() {
		return "STUDENTE";
	}

	@Override
	public String toString() {
		return super.toString() + "Classe: " + classe;
	}

	// Getters and Setters
	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

}
