package vao;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;


public class Nasvet {

	private int id;
	private String nasvet;
	private String avtor;
	private String hash;
	private int zapis_id;
	private int kartoteka_id;

	public Nasvet() {
	}

	public Nasvet(int id, String nasvet, String avtor, String hash, int zapis_id, int kartoteka_id) {
		this.id = id;
		this.nasvet = nasvet;
		this.avtor = avtor;
		this.hash = hash;
		this.zapis_id = zapis_id;
	}

	public Nasvet(String nasvet, String avtor, String hash, int zapis_id, int kartoteka_id) {
		this.nasvet = nasvet;
		this.avtor = avtor;
		this.hash = hash;
		this.zapis_id = zapis_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNasvet() {
		return nasvet;
	}

	public void setNasvet(String nasvet) {
		this.nasvet = nasvet;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getAvtor() {
		return avtor;
	}

	public void setAvtor(String avtor) {
		this.avtor = avtor;
	}

	public int getZapis_id() {
		return zapis_id;
	}

	public void setZapis_id(int zapis_id) {
		this.zapis_id = zapis_id;
	}

	public int getKartoteka_id() {
		return kartoteka_id;
	}

	public void setKartoteka_id(int kartoteka_id) {
		this.kartoteka_id = kartoteka_id;
	}


	@Override
	public String toString() {
		return nasvet;
	}

}