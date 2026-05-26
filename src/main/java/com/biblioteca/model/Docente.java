package com.biblioteca.model;

public class Docente extends Utente {

	// Attributes
	private String materia;

	// Constructor
	public Docente(int idUtente, String nome, String cognome, String email, String materia) {
		super(idUtente, nome, cognome, email);
		this.materia = materia;
	}

	// Overrides
	@Override
	public int getNumeroMassimoPrestiti() {
		return 5;
	}

	@Override
	public String getTipoUtente() {
		return "DOCENTE";
	}

	@Override
	public String toString() {
		return super.toString() + "Materia: " + materia;
	}

	// Getters and Setters
	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

}
