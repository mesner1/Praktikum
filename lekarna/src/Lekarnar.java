
public class Lekarnar {
	
	private String ime;
	private String priimek;
	private String sifra;
	
	public Lekarnar() {
		this("", "","");
	}
	
	public Lekarnar(String ime, String priimek,String sifra) {
		
		this.ime = ime;
		this.priimek = priimek;
		
		this.sifra = sifra;
		
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

	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	} 
	

}
