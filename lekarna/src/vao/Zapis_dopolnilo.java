package vao;


import java.util.ArrayList;

public class Zapis_dopolnilo {

	private int id;
	private int dopolnilo_id;
	private int zapis_id;
	private int kolicina;
	
	public Zapis_dopolnilo() {
	}
	
	
	public Zapis_dopolnilo(int id, int dopolnilo_id, int zapis_id, int kolicina) {
		this.id = id;
		this.dopolnilo_id = dopolnilo_id;
		this.zapis_id = zapis_id;
		this.kolicina = kolicina;
	}
	
	public Zapis_dopolnilo(int dopolnilo_id, int zapis_id, int kolicina) {
		this.dopolnilo_id = dopolnilo_id;
		this.zapis_id = zapis_id;
		this.kolicina = kolicina;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDopolnilo_id() {
		return dopolnilo_id;
	}
	public void setDopolnilo_id(int dopolnilo_id) {
		this.dopolnilo_id = dopolnilo_id;
	}
	public int getZapis_id() {
		return zapis_id;
	}
	public void setZapis_id(int zapis_id) {
		this.zapis_id = zapis_id;
	}


	public int getKolicina() {
		return kolicina;
	}


	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}




	@Override
	public String toString() {
		return "Zapis_dopolnilo [id=" + id + ", dopolnilo_id=" + dopolnilo_id + ", zapis_id=" + zapis_id + ", kolicina="
				+ kolicina + ", polje=" + "]";
	}
	
	
	
	
}