

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "zrno")
@SessionScoped
public class Model {

	private Dopolnilo novoDopolnilo = new Dopolnilo();
	private Zapis novZapis = new Zapis();
	private Zapis_dopolnilo novZapisDopolnila = new Zapis_dopolnilo();
	private ArrayList<Kartoteka> kartoteke = new ArrayList<Kartoteka>();
	private ArrayList<Dopolnilo> dopolnila = new ArrayList<Dopolnilo>();
	private ArrayList<String> izbranaDopolnila = new ArrayList<String>();
	private ArrayList<Zapis> izbraniZapisi = new ArrayList<Zapis>();
	private int izbranID;

	private String pacientIme;

//	public ArrayList<Zapis> izbraniZapisi(String ime) throws Exception {
//		String idPacienta = this.getPacientIme().substring(0, this.getPacientIme().indexOf(" -"));
//		int idKartoteke = Integer.parseInt(idPacienta);
//		System.out.println("imeeee je " + idKartoteke);
//		izbraniZapisi = (ArrayList<Zapis>) ZapisDAO.getInstance().vrniVse(idKartoteke);
//		System.out.println("dolzina: "+ izbraniZapisi.size());
//		return izbraniZapisi;
//
//	}
	
	public ArrayList<Zapis> izbraniZapisi(String ime) throws Exception {
		String idPacienta = this.getPacientIme().substring(0, this.getPacientIme().indexOf(" -"));
		int idKartoteke = Integer.parseInt(idPacienta);
		System.out.println("imeeee je " + idKartoteke);
		izbraniZapisi = (ArrayList<Zapis>) ZapisDAO.getInstance().vrniVse(idKartoteke);
		System.out.println("dolzina: "+ izbraniZapisi.size());
		return izbraniZapisi;

	}

	public ArrayList<Zapis> getIzbraniZapisi() {
		return izbraniZapisi;
	}

	public void setIzbraniZapisi(ArrayList<Zapis> izbraniZapisi) {
		this.izbraniZapisi = izbraniZapisi;
	}

	public ArrayList<String> getIzbranaDopolnila() {
		return izbranaDopolnila;
	}

	public void setIzbranaDopolnila(ArrayList<String> izbranaDopolnila) {
		this.izbranaDopolnila = izbranaDopolnila;
	}

	public Zapis getNovZapis() {
		return novZapis;
	}

	public void setNovZapis(Zapis novZapis) {
		this.novZapis = novZapis;
	}

	public int getIzbranID() {
		return izbranID;
	}

	public void setIzbranID(int izbranID) {
		this.izbranID = izbranID;
	}

	public String getPacientIme() {
		return pacientIme;
	}

	public void setPacientIme(String pacientIme) {
		this.pacientIme = pacientIme;
	}

	public Dopolnilo getNovoDopolnilo() {
		return novoDopolnilo;
	}

	public void setNovoDopolnilo(Dopolnilo novoDopolnilo) {
		this.novoDopolnilo = novoDopolnilo;
	}

	public Zapis_dopolnilo getNovZapisDopolnila() {
		return novZapisDopolnila;
	}

	public void setNovZapisDopolnila(Zapis_dopolnilo novZapisDopolnila) {
		this.novZapisDopolnila = novZapisDopolnila;
	}

	public ArrayList<Kartoteka> getKartoteke() throws Exception {
		kartoteke = (ArrayList) KartotekaDAO.getInstance().vrniVse();
		return kartoteke;
	}

	public void setKartoteke(ArrayList<Kartoteka> kartoteke) {
		this.kartoteke = kartoteke;
	}

	public ArrayList<Dopolnilo> getDopolnila() throws Exception {
		dopolnila = (ArrayList) DopolniloDAO.getInstance().vrniVse();
		return dopolnila;
	}

	public void setDopolnila(ArrayList<Dopolnilo> dopolnila) {
		this.dopolnila = dopolnila;
	}

	public void dodajDopolnilo() {
		try {
			if (DopolniloDAO.getInstance().najdiDopolnilo(novoDopolnilo.getId()) == null) {
				DopolniloDAO.getInstance().shraniDopolnilo(novoDopolnilo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dodajPredpis(String avtor) {
		try {

			System.out.println("naše ime je: " + this.getPacientIme());
			String idPacienta = this.getPacientIme().substring(0, this.getPacientIme().indexOf(" -"));
			int idKartoteke = Integer.parseInt(idPacienta);
			novZapis.setKartoteka_id(idKartoteke);
			System.out.println(novZapis.getKartoteka_id());
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			Date cas = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
			System.out.println("èas:" + cas);
			novZapis.setCas(cas);
			novZapis.setTip_id(1);
			System.out.println("hahaha: " + novZapis.getCas() + "in " + novZapis.getTip_id());
			novZapis.setAvtor(avtor);
			System.out.println("avtor je: " + novZapis.getAvtor());
				ZapisDAO.getInstance().shraniZapis(novZapis);

				System.out.println("ID zapisa: " + novZapis.getId());
				System.out.println(izbranaDopolnila.size());

				novZapisDopolnila.setZapis_id(novZapis.getId());

				for (int i = 0; i < izbranaDopolnila.size(); i++) {
					Dopolnilo izbrano = DopolniloDAO.getInstance().najdiDopolnilo(izbranaDopolnila.get(i));
					novZapisDopolnila.setDopolnilo_id(izbrano.getId());
					Zapis_dopolniloDAO.getInstance().shraniZapis_dopolnilo(novZapisDopolnila);

				}

				//novZapis.setDopolnila(izbranaDopolnila);
				novZapis = new Zapis();
				novZapisDopolnila = new Zapis_dopolnilo();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void vrniKartoteke() {
		try {
			kartoteke = (ArrayList) KartotekaDAO.getInstance().vrniVse();
			for (int i = 0; i < kartoteke.size(); i++)
				System.out.println(kartoteke.get(i).getIme());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void odjava() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	}
}