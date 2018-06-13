package vao;

public class Kombinacije {
	
	private int prvo_zdravilo;
	private int drugo_zdravilo;
	private String efekt;
	private int id;

	public Kombinacije() {
	}
	public Kombinacije(int id, int prvo_zdravilo, int drugo_zdravilo, String efekt) {
		this.id=id;
		this.prvo_zdravilo=prvo_zdravilo;
		this.drugo_zdravilo=drugo_zdravilo;
		this.efekt=efekt;
	}
	public Kombinacije(int prvo_zdravilo, int drugo_zdravilo, String efekt) {
		this.prvo_zdravilo=prvo_zdravilo;
		this.drugo_zdravilo=drugo_zdravilo;
		this.efekt=efekt;
	}


	public int getprvo_zdravilo() {
		return prvo_zdravilo;
	}


	public void setprvo_zdravilo(int prvo_zdravilo) {
		this.prvo_zdravilo = prvo_zdravilo;
	}


	public int getdrugo_zdravilo() {
		return drugo_zdravilo;
	}


	public void setdrugo_zdravilo(int drugo_zdravilo) {
		this.drugo_zdravilo = drugo_zdravilo;
	}


	public String getEfekt() {
		return efekt;
	}


	public void setEfekt(String efekt) {
		this.efekt = efekt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}