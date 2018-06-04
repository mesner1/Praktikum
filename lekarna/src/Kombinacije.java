

public class Kombinacije {
	
	private String prvo_zdravilo;
	private String drugo_zdravilo;
	private String efekt;
	private int id;

	public Kombinacije() {
	}
	public Kombinacije(int id, String prvo_zdravilo, String drugo_zdravilo, String efekt) {
		this.id=id;
		this.prvo_zdravilo=prvo_zdravilo;
		this.drugo_zdravilo=drugo_zdravilo;
		this.efekt=efekt;
	}
	public Kombinacije(String prvo_zdravilo, String drugo_zdravilo, String efekt) {
		this.prvo_zdravilo=prvo_zdravilo;
		this.drugo_zdravilo=drugo_zdravilo;
		this.efekt=efekt;
	}


	public String getprvo_zdravilo() {
		return prvo_zdravilo;
	}


	public void setprvo_zdravilo(String prvo_zdravilo) {
		this.prvo_zdravilo = prvo_zdravilo;
	}


	public String getdrugo_zdravilo() {
		return drugo_zdravilo;
	}


	public void setdrugo_zdravilo(String drugo_zdravilo) {
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