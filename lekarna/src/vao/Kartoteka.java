package vao;

public class Kartoteka {

	private int id;
	private String ime;
	private String priimek;
	private String email;
	
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

	@Override
	public String toString() {
		return id + " - " + ime + " " + priimek;
	}


}