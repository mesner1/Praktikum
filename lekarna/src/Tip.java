

public class Tip {

	private int id;
	private String naziv;
	
	public Tip() {
	}
	
	public Tip(String naziv) {
		this.naziv = naziv;
	}
	
	public Tip(int id, String naziv) {
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