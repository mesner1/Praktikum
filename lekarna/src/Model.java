package si.feri.praktikum;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

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
	private ArrayList<Dopolnilo> dopolnilaBrezRecepta = new ArrayList<Dopolnilo>();
	private ArrayList<String> izbranaDopolnila = new ArrayList<String>();
	private ArrayList<Zapis> izbraniZapisi = new ArrayList<Zapis>();
	private int izbranID;

	private int kolicina;
	private ArrayList<Integer> kolicine = new ArrayList<Integer>();

	private String pacientIme;

	public void poglejmo(String kolicine) {
		System.out.println("POGLEJMO ZDAJ: " + kolicine);
	}

	public ArrayList<Zapis> izbraniZapisi(String ime) throws Exception {
		String idPacienta = this.getPacientIme().substring(0, this.getPacientIme().indexOf(" -"));
		int idKartoteke = Integer.parseInt(idPacienta);
		izbraniZapisi = (ArrayList<Zapis>) ZapisDAO.getInstance().vrniVse(idKartoteke);
		// izbraniZapisi.toString().replace("[","");
		for (int i = 0; i < izbraniZapisi.size(); i++) {
			izbraniZapisi.get(i).getDopolnila().toString().replace("[", "").replace("]", "");
		}
		return izbraniZapisi;

	}
	
	public Dopolnilo najdiZdravilo(String dopolnilo) throws Exception {
		Dopolnilo najdeno = DopolniloDAO.getInstance().najdiDopolnilo(dopolnilo);
		return najdeno;
	}

	public ArrayList<Zapis> izbraniZapisiLekarnar(String ime) throws Exception {
		String idPacienta = this.getPacientIme().substring(0, this.getPacientIme().indexOf(" -"));
		int idKartoteke = Integer.parseInt(idPacienta);
		izbraniZapisi = (ArrayList<Zapis>) ZapisDAO.getInstance().vrniVseNeizdane(idKartoteke);
		System.out.println("dolzinaLekarnar: " + izbraniZapisi.size());
		return izbraniZapisi;

	}

	public ArrayList<Zapis> vsiIzdani(String ime) throws Exception {
		String idPacienta = this.getPacientIme().substring(0, this.getPacientIme().indexOf(" -"));
		int idKartoteke = Integer.parseInt(idPacienta);
		izbraniZapisi = (ArrayList<Zapis>) ZapisDAO.getInstance().vrniVseIzdane(idKartoteke);
		System.out.println("dolzinaLekarnar: " + izbraniZapisi.size());
		return izbraniZapisi;

	}

	public void izdaj(String avtor, int idZapis, ArrayList<Dopolnilo> dopolnila) throws Exception {
		try {
			String idPacienta = this.getPacientIme().substring(0, this.getPacientIme().indexOf(" -"));
			int idKartoteke = Integer.parseInt(idPacienta);
			novZapis.setKartoteka_id(idKartoteke);
			System.out.println(novZapis.getKartoteka_id());
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			Date cas = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
			System.out.println("èas:" + cas);

			novZapis.setCas(cas);
			novZapis.setTip("izdaja");
			novZapis.setAvtor(avtor);
			System.out.println("avtor je: " + novZapis.getAvtor());
			ZapisDAO.getInstance().shraniZapis(novZapis);

			System.out.println("ID zapisa: " + novZapis.getId());

			novZapisDopolnila.setZapis_id(novZapis.getId());

			System.out.println("ID ZAPISA: " + idZapis);
			ZapisDAO.getInstance().posodobiIzdano(idZapis);

			for (int i = 0; i < dopolnila.size(); i++) {
				izbranaDopolnila.add(dopolnila.get(i).getNaziv());
			}

			System.out.println("dolžina izbranih2: " + izbranaDopolnila.size());
			System.out.println("PA TO:" + novZapisDopolnila.getZapis_id());

			for (int i = 0; i < izbranaDopolnila.size(); i++) {
				Dopolnilo izbrano = DopolniloDAO.getInstance().najdiDopolnilo(izbranaDopolnila.get(i));
				System.out.println("poglejmo izbranoooo " + izbrano.getId());
				Zapis_dopolnilo najden = Zapis_dopolniloDAO.getInstance().najdiDoloceno(izbrano.getId(), idZapis);
				novZapisDopolnila.setDopolnilo_id(izbrano.getId());
				novZapisDopolnila.setKolicina(najden.getKolicina());
				Zapis_dopolniloDAO.getInstance().shraniZapis_dopolnilo(novZapisDopolnila);

			}

			// novZapis.setDopolnila(izbranaDopolnila);
			novZapis = new Zapis();
			novZapisDopolnila = new Zapis_dopolnilo();
			izbranaDopolnila = new ArrayList<String>();

			// IZRAÈUN ZAUŽITJA

			idPacienta = this.getPacientIme().substring(0, this.getPacientIme().indexOf(" -"));
			idKartoteke = Integer.parseInt(idPacienta);
			novZapis.setKartoteka_id(idKartoteke);
			System.out.println(novZapis.getKartoteka_id());
			int najdaljse = dopolnila.get(0).getTrajanje();
			for (int i = 0; i < dopolnila.size(); i++) {
				if (dopolnila.get(i).getTrajanje() > najdaljse) {
					najdaljse = dopolnila.get(i).getTrajanje();
				}
			}
			dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			now = LocalDateTime.now();
			cas = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

			Calendar c = Calendar.getInstance();
			c.setTime(cas);
			c.add(Calendar.DATE, najdaljse);

			Date koncniCas = c.getTime();

			System.out.println("èas:" + koncniCas);

			novZapis.setCas(koncniCas);
			novZapis.setTip("zadnje_zaužitje");

			ZapisDAO.getInstance().shraniZapis(novZapis);

			System.out.println("ID zapisa: " + novZapis.getId());

			novZapisDopolnila.setZapis_id(novZapis.getId());

			System.out.println("ID ZAPISA: " + idZapis);
			ZapisDAO.getInstance().posodobiIzdano(idZapis);

			for (int i = 0; i < dopolnila.size(); i++) {
				izbranaDopolnila.add(dopolnila.get(i).getNaziv());
			}

			System.out.println("dolžina izbranih2: " + izbranaDopolnila.size());
			System.out.println("PA TO:" + novZapisDopolnila.getZapis_id());

			for (int i = 0; i < izbranaDopolnila.size(); i++) {
				Dopolnilo izbrano = DopolniloDAO.getInstance().najdiDopolnilo(izbranaDopolnila.get(i));
				Zapis_dopolnilo najden = Zapis_dopolniloDAO.getInstance().najdiDoloceno(izbrano.getId(), idZapis);
				novZapisDopolnila.setDopolnilo_id(izbrano.getId());
				novZapisDopolnila.setKolicina(najden.getKolicina());
				Zapis_dopolniloDAO.getInstance().shraniZapis_dopolnilo(novZapisDopolnila);

			}

			// novZapis.setDopolnila(izbranaDopolnila);
			novZapis = new Zapis();
			novZapisDopolnila = new Zapis_dopolnilo();
			izbranaDopolnila = new ArrayList<String>();

		} catch (Exception e) {
			e.printStackTrace();
		}
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
		kartoteke = (ArrayList<Kartoteka>) KartotekaDAO.getInstance().vrniVse();
		return kartoteke;
	}

	public void setKartoteke(ArrayList<Kartoteka> kartoteke) {
		this.kartoteke = kartoteke;
	}

	public ArrayList<Dopolnilo> getDopolnila() throws Exception {
		dopolnila = (ArrayList<Dopolnilo>) DopolniloDAO.getInstance().vrniVse();
		return dopolnila;
	}
	
	

	public ArrayList<Dopolnilo> getDopolnilaBrezRecepta() throws Exception {
		dopolnilaBrezRecepta = (ArrayList<Dopolnilo>) DopolniloDAO.getInstance().vrniVseBrezRecepta();
		return dopolnilaBrezRecepta;
	}

	public void setDopolnilaBrezRecepta(ArrayList<Dopolnilo> dopolnilaBrezRecepta) {
		this.dopolnilaBrezRecepta = dopolnilaBrezRecepta;
	}

	public void setDopolnila(ArrayList<Dopolnilo> dopolnila) {
		this.dopolnila = dopolnila;
	}

	public int getKolicina() {
		return kolicina;
	}

	public void setKolicina(int kolicina) {
		System.out.println(kolicina);
		kolicine.add(kolicina);
		for (int i = 0; i < kolicine.size(); i++) {
			System.out.println("kolicine:" + kolicine.get(i));
		}
		this.kolicina = kolicina;
	}

	public ArrayList<Integer> getKolicine() {
		return kolicine;
	}

	public void setKolicine(ArrayList<Integer> kolicine) {
		this.kolicine = kolicine;
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

			System.out.println("kolicine:" + kolicine.size());
			System.out.println("naše ime je: " + this.getPacientIme());
			String idPacienta = this.getPacientIme().substring(0, this.getPacientIme().indexOf(" -"));
			int idKartoteke = Integer.parseInt(idPacienta);
			novZapis.setKartoteka_id(idKartoteke);
			novZapis.setIzdan(0);
			System.out.println(novZapis.getKartoteka_id());
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();

			Date cas = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
			System.out.println("èas:" + cas);
			novZapis.setCas(cas);
			novZapis.setTip("predpis");
			novZapis.setAvtor(avtor);
			System.out.println("avtor je: " + novZapis.getAvtor());
			ZapisDAO.getInstance().shraniZapis(novZapis);

			System.out.println("ID zapisa: " + novZapis.getId());
			System.out.println(izbranaDopolnila.size());

			novZapisDopolnila.setZapis_id(novZapis.getId());

			for (int i = 0; i < izbranaDopolnila.size(); i++) {
				Dopolnilo izbrano = DopolniloDAO.getInstance().najdiDopolnilo(izbranaDopolnila.get(i));
				novZapisDopolnila.setDopolnilo_id(izbrano.getId());
				novZapisDopolnila.setKolicina(kolicine.get(i));
				Zapis_dopolniloDAO.getInstance().shraniZapis_dopolnilo(novZapisDopolnila);

			}

			// novZapis.setDopolnila(izbranaDopolnila);
			novZapis = new Zapis();
			novZapisDopolnila = new Zapis_dopolnilo();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void novaIzdajaLekarnar(String avtor) {
		try {
			System.out.println("kolicine:" + kolicine.size());
			System.out.println("naše ime je: " + this.getPacientIme());
			String idPacienta = this.getPacientIme().substring(0, this.getPacientIme().indexOf(" -"));
			int idKartoteke = Integer.parseInt(idPacienta);
			novZapis.setKartoteka_id(idKartoteke);
			//novZapis.setIzdan(1);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();

			Date cas = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
			System.out.println("èas:" + cas);
			novZapis.setCas(cas);
			novZapis.setTip("izdaja");
			novZapis.setAvtor(avtor);
			System.out.println("avtor je: " + novZapis.getAvtor());
			ZapisDAO.getInstance().shraniZapis(novZapis);

			System.out.println("ID zapisa: " + novZapis.getId());
			System.out.println(izbranaDopolnila.size());

			novZapisDopolnila.setZapis_id(novZapis.getId());

			for (int i = 0; i < izbranaDopolnila.size(); i++) {
				Dopolnilo izbrano = DopolniloDAO.getInstance().najdiDopolnilo(izbranaDopolnila.get(i));
				novZapisDopolnila.setDopolnilo_id(izbrano.getId());
				novZapisDopolnila.setKolicina(kolicine.get(i));
				Zapis_dopolniloDAO.getInstance().shraniZapis_dopolnilo(novZapisDopolnila);

			}

			// novZapis.setDopolnila(izbranaDopolnila);
			novZapis = new Zapis();
			novZapisDopolnila = new Zapis_dopolnilo();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void vrniKartoteke() {
		try {
			kartoteke = (ArrayList<Kartoteka>) KartotekaDAO.getInstance().vrniVse();
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
