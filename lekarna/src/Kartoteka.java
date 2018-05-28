

public class Kartoteka {

	private int id;
	private String ime;
	private String priimek;
	
	public Kartoteka() {
	}
	
	public Kartoteka(int id, String ime, String priimek) {
		this.id = id;
		this.ime = ime;
		this.priimek = priimek;
	}
	
	public Kartoteka(String ime, String priimek) {
		this.ime = ime;
		this.priimek = priimek;
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
	
	@Override
	public String toString() {
		return id + " - " + ime + " " + priimek;
	}

}