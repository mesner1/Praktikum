

public class Tip_zapis {

	private int id;
	private String naziv;
	
	public Tip_zapis() {
	}
	
	public Tip_zapis(String naziv) {
		this.naziv = naziv;
	}
	
	public Tip_zapis(int id, String naziv) {
		this.id = id;
		this.naziv = naziv;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
}