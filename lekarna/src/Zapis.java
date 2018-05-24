

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Zapis {
	private int id;
	private Date cas;
	private int kartoteka_id;
	private int tip_id;
	private String avtor;
	private ArrayList<Dopolnilo> dopolnila;

	public Zapis() {
	}



	public Zapis(int id, Date cas, int kartoteka_id, int tip_id, String avtor) {
		this.id = id;
		this.cas = cas;
		this.kartoteka_id = kartoteka_id;
		this.tip_id = tip_id;
		this.avtor = avtor;
	}
	
	public Zapis(Date cas, int kartoteka_id, int tip_id, String avtor) {
		this.cas = cas;
		this.kartoteka_id = kartoteka_id;
		this.tip_id = tip_id;
		this.avtor = avtor;
	}

	public Zapis(Date cas, int kartoteka_id, int tip_id, String avtor, ArrayList<Dopolnilo> dopolnila) {
		this.cas = cas;
		this.kartoteka_id = kartoteka_id;
		this.tip_id = tip_id;
		this.avtor = avtor;
		this.dopolnila = dopolnila;
	}
	
	public Zapis(int kartoteka_id) {
		this.kartoteka_id = kartoteka_id;
	}


	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public Date getCas() {
		return cas;
	}



	public void setCas(Date cas) {
		this.cas = cas;
	}






	public int getKartoteka_id() {
		return kartoteka_id;
	}



	public void setKartoteka_id(int kartoteka_id) {
		this.kartoteka_id = kartoteka_id;
	}



	public int getTip_id() {
		return tip_id;
	}



	public void setTip_id(int tip_id) {
		this.tip_id = tip_id;
	}
	
	



	public String getAvtor() {
		return avtor;
	}



	public void setAvtor(String avtor) {
		this.avtor = avtor;
	}

	


	public ArrayList<Dopolnilo> getDopolnila() {
		return dopolnila;
	}



	public void setDopolnila(ArrayList<Dopolnilo> izbranaDopolnila) {
		this.dopolnila = izbranaDopolnila;
	}
	
	
	



	// private static SimpleDateFormat sdf=new SimpleDateFormat("dd. MM. yyyy");
	@Override
	public String toString() {
		return cas + " " + kartoteka_id + " " + tip_id;
	}
}