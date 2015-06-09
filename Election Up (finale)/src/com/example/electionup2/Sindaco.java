package com.example.electionup2;

public class Sindaco {
	private String nome;
	private String cognome;
	private String id;
	private int voti = 0;

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public int getVoti() {
		return voti;
	}

	public String getId() {
		return id;
	}

	public void setNome(String nom) {
		this.nome = nom;
	}

	public void setCognome(String cog) {
		this.cognome = cog;
	}

	public void setVoti(int vot) {
		this.voti = vot;
	}

	public void setId(String i) {
		id = i;
	}
}
