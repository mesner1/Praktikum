package vao;


import java.util.List;

public class Dopolnilo {
	

	private int id;
	private String naziv;
	private int naRecept;
	private int trajanje;
	private int kolicina;
	private String opis;
	private String embalaza;

	public Dopolnilo() {
	}

	public Dopolnilo(String naziv, int naRecept, int trajanje) {
		this.naziv = naziv;
		this.naRecept = naRecept;
		this.trajanje = trajanje;
	}
	
	public Dopolnilo(String naziv, int naRecept, int trajanje, String opis) {
		this.naziv = naziv;
		this.naRecept = naRecept;
		this.trajanje = trajanje;
		this.opis = opis;
	}

	public Dopolnilo(String naziv, int naRecept, int trajanje, int kolicina) {
		this.naziv = naziv;
		this.naRecept = naRecept;
		this.trajanje = trajanje;
		this.kolicina = kolicina;
	}
	
	public Dopolnilo(int id, String naziv, int naRecept, int trajanje) {
		this.id = id;
		this.naziv = naziv;
		this.naRecept = naRecept;
		this.trajanje = trajanje;
	}

	public Dopolnilo(String naziv, int naRecept, int trajanje, int kolicina, String opis) {
		this.naziv = naziv;
		this.naRecept = naRecept;
		this.trajanje = trajanje;
		this.kolicina = kolicina;
		this.opis = opis;
	}
	
	public Dopolnilo(int id, String naziv, int naRecept, int trajanje, String opis) {
		this.id = id;
		this.naziv = naziv;
		this.naRecept = naRecept;
		this.trajanje = trajanje;
		this.opis = opis;
	}
	
	public Dopolnilo(int id, String naziv, int naRecept, int trajanje, String opis, String embalaza) {
		this.id = id;
		this.naziv = naziv;
		this.naRecept = naRecept;
		this.trajanje = trajanje;
		this.opis = opis;
		this.embalaza = embalaza;
	}
	
	public Dopolnilo(String naziv, int naRecept, int trajanje, String opis, String embalaza) {
		this.naziv = naziv;
		this.naRecept = naRecept;
		this.trajanje = trajanje;
		this.opis = opis;
		this.embalaza = embalaza;
	}
	
	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public int getNaRecept() {
		return naRecept;
	}

	public void setNaRecept(int naRecept) {
		this.naRecept = naRecept;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

	public int getTrajanje() {
		return trajanje;
	}

	public void setTrajanje(int trajanje) {
		this.trajanje = trajanje;
	}
	
	

	public int getKolicina() {
		return kolicina;
	}

	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}
	
	
	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	
	

//	// private static SimpleDateFormat sdf=new SimpleDateFormat("dd. MM. yyyy");
//	@Override
//	public String toString() {
//		return naziv + " (" + kolicina + "x)";
//	}


	public String getEmbalaza() {
		return embalaza;
	}

	public void setEmbalaza(String embalaza) {
		this.embalaza = embalaza;
	}

	// private static SimpleDateFormat sdf=new SimpleDateFormat("dd. MM. yyyy");
	@Override
	public String toString() {
		return naziv;
	}
}