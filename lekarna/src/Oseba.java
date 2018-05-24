

public class Oseba {
	
private String ime;
private String priimek;
private String naziv;
private String sifra;

public Oseba() {
	this("", "","","");
}

public Oseba(String ime, String priimek,String naziv,String sifra) {
	
	this.ime = ime;
	this.priimek = priimek;
	this.naziv = naziv;
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

public String getNaziv() {
	return naziv;
}

public void setNaziv(String naziv) {
	this.naziv = naziv;
}

public String getSifra() {
	return sifra;
}

public void setSifra(String sifra) {
	this.sifra = sifra;
}




}
