package vao;

import java.util.Date;

public class Kartoteka {

	private int id;
	private String ime;
	private String priimek;
	private String email;
	private int starost;
	private int spol;
	
	public Kartoteka() {
	}
	
	public Kartoteka(int id, String ime, String priimek, String email) {
		this.id = id;
		this.ime = ime;
		this.priimek = priimek;
		this.email=email;
	}
	
	public Kartoteka(String ime, String priimek, String email) {
		this.ime = ime;
		this.priimek = priimek;
		this.email=email;
	}
	
	public Kartoteka(int id, String ime, String priimek, String email, int datumRojstva, int spol) {
		this.id = id;
		this.ime = ime;
		this.priimek = priimek;
		this.email=email;
		this.starost = datumRojstva;
		this.spol=spol;
	}
	
	public Kartoteka(String ime, String priimek, String email, int datumRojstva, int spol) {
		this.ime = ime;
		this.priimek = priimek;
		this.email=email;
		this.starost = datumRojstva;
		this.spol=spol;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPriimek() {
		return priimek;
	}
	public void setPriimek(String priimek) {
		this.priimek = priimek;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

	public int getStarost() {
		return starost;
	}

	public void setStarost(int starost) {
		this.starost = starost;
	}

	public int getSpol() {
		return spol;
	}

	public void setSpol(int spol) {
		this.spol = spol;
	}

	@Override
	public String toString() {
		return ime + " " + priimek + " (" + id + ")";
	}


}