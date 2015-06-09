package com.example.electionup2;

public class Lista {
	private String id;
	private String nome;
	private int voti;

	public String getNome() {
		return nome;
	}

	public int getVoti() {
		return voti;
	}

	public String getId() {
		return id;
	}

	public void setNome(String nom) {
		nome = nom;
	}

	public void setVoti(int vot) {
		voti = vot;
	}

	public void setId(String i) {
		id = i;
	}
}
