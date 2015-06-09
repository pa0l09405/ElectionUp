package com.example.electionup2;

public class Consigliere {
	private String id;
	private String nome;
	private String cognome;
	private String sesso;
	private int voti;

	public String getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public String getSesso() {
		return sesso;
	}

	public int getVoti() {
		return voti;
	}

	public void setNome(String nom) {
		nome = nom;
	}

	public void setCognome(String cog) {
		cognome = cog;
	}

	public void setSesso(String ses) {
		sesso = ses;
	}

	public void setVoti(int vot) {
		voti = vot;
	}

	public void setId(String i) {
		id = i;
	}
}
