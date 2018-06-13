package praktikum;

import dao.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerException;

import org.apache.fop.apps.FOPException;

import Email.*;
import vao.*;
import xmlvpdf.Xml;
import blockchain.*;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;



@ManagedBean(name = "zrno")
@SessionScoped
public class Model {

	private Kartoteka izbranaKartoteka = new Kartoteka();
	private Kombinacije noveKombinacije = new Kombinacije();
	private Dopolnilo novoDopolnilo = new Dopolnilo();
	private Zapis novZapis = new Zapis();
	private Nasvet novNasvet = new Nasvet();
	private Zapis_dopolnilo novZapisDopolnila = new Zapis_dopolnilo();
	private ArrayList<Kartoteka> kartoteke = new ArrayList<Kartoteka>();
	private ArrayList<Dopolnilo> dopolnila = new ArrayList<Dopolnilo>();
	private ArrayList<Dopolnilo> dopolnilaBrezRecepta = new ArrayList<Dopolnilo>();
	private ArrayList<String> izbranaDopolnila = new ArrayList<String>();
	private ArrayList<Zapis> izbraniZapisi = new ArrayList<Zapis>();
	private ArrayList<Nasvet> izbraniNasveti = new ArrayList<Nasvet>();
	private ArrayList<Kombinacije> izbraneKombinacije = new ArrayList<Kombinacije>();
	private int izbranID;

	private int kolicina;
	private ArrayList<Integer> kolicine = new ArrayList<Integer>();

	private String pacientIme;
	
	
	public void ustvariPdf() {
		Xml ob = new Xml();
		try {
			ob.writeXmlFile(izbraniZapisi);
		} catch (FOPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	


	public boolean preveriKombinacijo() throws Exception {
	// kombinacije
		String idPacienta = this.getPacientIme().substring(0, this.getPacientIme().indexOf(" -"));
		int idKartoteke = Integer.parseInt(idPacienta);
		
		
	ArrayList<Integer> izbranaDopolnilaId = (ArrayList<Integer>) DopolniloDAO.getInstance()
			.poisciId(izbranaDopolnila);
	System.out.println("izbranaDopolnila:" + izbranaDopolnilaId);
	ArrayList<Integer> zauzitaDopolnila = kombiniraj(idKartoteke);
	System.out.println("zauzitaDopolnila:" + zauzitaDopolnila);
	
	for (int i = 0; i < zauzitaDopolnila.size(); i++) {
		for (int j = 0; j < izbranaDopolnilaId.size(); j++) {
			System.out.println("primerjam " + zauzitaDopolnila.get(i) + " in " + izbranaDopolnilaId.get(j));
			boolean vrni = KombinacijeDAO.getInstance().najdiKombinacije(zauzitaDopolnila.get(i),
					izbranaDopolnilaId.get(j));
			if (vrni == true) {
				System.out.println("KOMBINACIJA!");
				return false;
			}
		}
	}
	return true;
	}
	

	public ArrayList<Dopolnilo> prikaziIzbranaDopolnila() throws Exception {
		ArrayList<Dopolnilo> izbrana = new ArrayList<Dopolnilo>();
		izbrana = (ArrayList<Dopolnilo>) DopolniloDAO.pretvori(izbranaDopolnila);
		return izbrana;
	}

	public ArrayList<Integer> pridobiZapise(int idKartoteke) throws Exception {
		ArrayList<Integer> nedavnoZauziti = new ArrayList<Integer>();
		nedavnoZauziti = (ArrayList<Integer>) ZapisDAO.getInstance().vrniVseZauzite(idKartoteke);
		System.out.println("Zaužiti: " + nedavnoZauziti + " " + nedavnoZauziti.size());
		return nedavnoZauziti;
	}

	public ArrayList<Integer> kombiniraj(int idKartoteke) throws Exception {

		ArrayList<Integer> nedavnoZauziti = pridobiZapise(idKartoteke);
		ArrayList<Integer> idZdravil = new ArrayList<Integer>();
		idZdravil = (ArrayList<Integer>) Zapis_dopolniloDAO.getInstance().vrniVsaZdravila(nedavnoZauziti);
		System.out.println("Zdravila:" + idZdravil);

		return idZdravil;

	}

	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static int difficulty = 1;
	int i = 0;

	public void dodajNasvet(String avtor, String pacient) throws Exception {
		novNasvet.setAvtor(avtor);
		String idPacienta = this.getPacientIme().substring(0, this.getPacientIme().indexOf(" -"));
		int idKartoteke = Integer.parseInt(idPacienta);
		novNasvet.setKartoteka_id(idKartoteke);

		if (blockchain.isEmpty()) {
			blockchain.add(new Block(novNasvet.getNasvet(), "0"));
			novNasvet.setHash(blockchain.get(i).mineBlock(difficulty));
		}

		else {
			blockchain.add(new Block(novNasvet.getNasvet(), blockchain.get(blockchain.size() - 1).hash));
			novNasvet.setHash(blockchain.get(i).mineBlock(difficulty));
		}
		i++;
		NasvetDAO.getInstance().shraniNasvet(novNasvet);
		novNasvet = new Nasvet();

	}

	public static Boolean isChainValid() {
		Block currentBlock;
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');

		// loop through blockchain to check hashes:
		for (int i = 1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i - 1);
			// compare registered hash and calculated hash:
			if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
				System.out.println("Current Hashes not equal");
				return false;
			}
			// compare previous hash and registered previous hash
			if (!previousBlock.hash.equals(currentBlock.previousHash)) {
				System.out.println("Previous Hashes not equal");
				return false;
			}
			// check if hash is solved
			if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
				System.out.println("This block hasn't been mined");
				return false;
			}
		}
		return true;
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

	public ArrayList<Nasvet> izbraniNasveti(String ime) throws Exception {
		String idPacienta = this.getPacientIme().substring(0, this.getPacientIme().indexOf(" -"));
		int idKartoteke = Integer.parseInt(idPacienta);
		izbraniNasveti = (ArrayList<Nasvet>) NasvetDAO.getInstance().vrniVse(idKartoteke);
		return izbraniNasveti;

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

	public ArrayList<Zapis> neizdaniZapisiLekarnar() throws Exception {
		izbraniZapisi = (ArrayList<Zapis>) ZapisDAO.getInstance().vrniVseNeizdane();
		return izbraniZapisi;

	}

	public ArrayList<Zapis> vsiIzdani(String ime) throws Exception {
		String idPacienta = this.getPacientIme().substring(0, this.getPacientIme().indexOf(" -"));
		int idKartoteke = Integer.parseInt(idPacienta);
		izbraniZapisi = (ArrayList<Zapis>) ZapisDAO.getInstance().vrniVseIzdane(idKartoteke);
		System.out.println("dolzinaLekarnar: " + izbraniZapisi.size());
		return izbraniZapisi;
	}

	public ArrayList<Zapis> vsiIzdani() throws Exception {
		izbraniZapisi = (ArrayList<Zapis>) ZapisDAO.getInstance().vrniVseIzdane();
		System.out.println("dolzinaLekarnar: " + izbraniZapisi.size());
		return izbraniZapisi;

	}
	

	public void izdaj(String avtor, int idZapis, ArrayList<Dopolnilo> dopolnila) throws Exception {
		try {
			String idPacienta = this.getPacientIme().substring(0, this.getPacientIme().indexOf(" -"));
			int idKartoteke = Integer.parseInt(idPacienta);
			novZapis.setKartoteka_id(idKartoteke);
			java.util.Date utilDate = new java.util.Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(utilDate);
			new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(cal.getTime());
			novZapis.setCas(cal);
			novZapis.setTip("izdaja");
			novZapis.setAvtor(avtor);
			ZapisDAO.getInstance().shraniZapis(novZapis);

			System.out.println("ID zapisa: " + novZapis.getId());

			novZapisDopolnila.setZapis_id(novZapis.getId());

			System.out.println("ID ZAPISA: " + idZapis);
			ZapisDAO.getInstance().posodobiIzdano(idZapis);

			for (int i = 0; i < dopolnila.size(); i++) {
				izbranaDopolnila.add(dopolnila.get(i).getNaziv());
			}

			for (int i = 0; i < izbranaDopolnila.size(); i++) {
				Dopolnilo izbrano = DopolniloDAO.getInstance().najdiDopolnilo(izbranaDopolnila.get(i));
				System.out.println("poglejmo izbranoooo " + izbrano.getId());
				Zapis_dopolnilo najden = Zapis_dopolniloDAO.getInstance().najdiDoloceno(izbrano.getId(), idZapis);
				novZapisDopolnila.setDopolnilo_id(izbrano.getId());
				novZapisDopolnila.setKolicina(najden.getKolicina());
				Zapis_dopolniloDAO.getInstance().shraniZapis_dopolnilo(novZapisDopolnila);
			}

			// POSILJANJE OBVESTILA
			izbranaKartoteka = KartotekaDAO.getInstance().najdiKartoteko(idKartoteke);
			String email = izbranaKartoteka.getEmail();
			String ime = izbranaKartoteka.getIme();
			String priimek = izbranaKartoteka.getPriimek();
			EmailPoslji e = new EmailPoslji();
			e.poslji(avtor, email, ime, priimek);

			// novZapis.setDopolnila(izbranaDopolnila);
			novZapis = new Zapis();
			novZapisDopolnila = new Zapis_dopolnilo();
			izbranaDopolnila = new ArrayList<String>();

			// IZRAČUN ZAUŽITJA

			idPacienta = this.getPacientIme().substring(0, this.getPacientIme().indexOf(" -"));
			idKartoteke = Integer.parseInt(idPacienta);
			novZapis.setKartoteka_id(idKartoteke);
			int najdaljse = dopolnila.get(0).getTrajanje() * dopolnila.get(0).getKolicina();
			for (int i = 0; i < dopolnila.size(); i++) {
				if (dopolnila.get(i).getTrajanje() * dopolnila.get(i).getKolicina() > najdaljse) {
					najdaljse = dopolnila.get(i).getTrajanje() * dopolnila.get(i).getKolicina();
				}
			}

			java.util.Date utilDate2 = new java.util.Date();
			cal.setTime(utilDate2);
			cal.add(Calendar.DATE, najdaljse);
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());

			novZapis.setCas(cal);
			novZapis.setTip("zadnje zaužitje");

			ZapisDAO.getInstance().shraniZapis(novZapis);

			novZapisDopolnila.setZapis_id(novZapis.getId());


			for (int i = 0; i < dopolnila.size(); i++) {
				izbranaDopolnila.add(dopolnila.get(i).getNaziv());
			}

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


	public void izdajPogledSkupaj(String avtor, int idZapis, ArrayList<Dopolnilo> dopolnila, int idKartoteke)
			throws Exception {
		try {
			novZapis.setKartoteka_id(idKartoteke);
			java.util.Date utilDate = new java.util.Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(utilDate);
			new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(cal.getTime());
			novZapis.setCas(cal);
			novZapis.setTip("izdaja");
			novZapis.setAvtor(avtor);
			ZapisDAO.getInstance().shraniZapis(novZapis);

			System.out.println("ID zapisa: " + novZapis.getId());

			novZapisDopolnila.setZapis_id(novZapis.getId());

			System.out.println("ID ZAPISA: " + idZapis);
			ZapisDAO.getInstance().posodobiIzdano(idZapis);

			for (int i = 0; i < dopolnila.size(); i++) {
				izbranaDopolnila.add(dopolnila.get(i).getNaziv());
			}

			for (int i = 0; i < izbranaDopolnila.size(); i++) {
				Dopolnilo izbrano = DopolniloDAO.getInstance().najdiDopolnilo(izbranaDopolnila.get(i));
				System.out.println("poglejmo izbranoooo " + izbrano.getId());
				Zapis_dopolnilo najden = Zapis_dopolniloDAO.getInstance().najdiDoloceno(izbrano.getId(), idZapis);
				novZapisDopolnila.setDopolnilo_id(izbrano.getId());
				novZapisDopolnila.setKolicina(najden.getKolicina());
				Zapis_dopolniloDAO.getInstance().shraniZapis_dopolnilo(novZapisDopolnila);
			}

			// POSILJANJE OBVESTILA
			izbranaKartoteka = KartotekaDAO.getInstance().najdiKartoteko(idKartoteke);
			String email = izbranaKartoteka.getEmail();
			String ime = izbranaKartoteka.getIme();
			String priimek = izbranaKartoteka.getPriimek();
			EmailPoslji e = new EmailPoslji();
			e.poslji(avtor, email, ime, priimek);

			// novZapis.setDopolnila(izbranaDopolnila);
			novZapis = new Zapis();
			novZapisDopolnila = new Zapis_dopolnilo();
			izbranaDopolnila = new ArrayList<String>();

			// IZRAČUN ZAUŽITJA

			novZapis.setKartoteka_id(idKartoteke);
			System.out.println(novZapis.getKartoteka_id());
			int najdaljse = dopolnila.get(0).getTrajanje();
			for (int i = 0; i < dopolnila.size(); i++) {
				if (dopolnila.get(i).getTrajanje() > najdaljse) {
					najdaljse = dopolnila.get(i).getTrajanje();
				}
			}

			java.util.Date utilDate2 = new java.util.Date();
			cal.setTime(utilDate2);
			cal.add(Calendar.DATE, najdaljse);
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());

			novZapis.setCas(cal);
			novZapis.setTip("zadnje zaužitje");

			ZapisDAO.getInstance().shraniZapis(novZapis);

			novZapisDopolnila.setZapis_id(novZapis.getId());


			for (int i = 0; i < dopolnila.size(); i++) {
				izbranaDopolnila.add(dopolnila.get(i).getNaziv());
			}

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

	public Nasvet getNovNasvet() {
		return novNasvet;
	}

	public void setNovNasvet(Nasvet novNasvet) {
		this.novNasvet = novNasvet;
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
			String idPacienta = this.getPacientIme().substring(0, this.getPacientIme().indexOf(" -"));
			int idKartoteke = Integer.parseInt(idPacienta);
			novZapis.setKartoteka_id(idKartoteke);
			novZapis.setIzdan(0);

			java.util.Date utilDate = new java.util.Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(utilDate);
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
			novZapis.setCas(cal);
			novZapis.setTip("predpis");
			novZapis.setAvtor(avtor);
			ZapisDAO.getInstance().shraniZapis(novZapis);


			// //
			novZapisDopolnila.setZapis_id(novZapis.getId());

			for (int i = 0; i < izbranaDopolnila.size(); i++) {
				Dopolnilo izbrano = DopolniloDAO.getInstance().najdiDopolnilo(izbranaDopolnila.get(i));
				novZapisDopolnila.setDopolnilo_id(izbrano.getId());
				novZapisDopolnila.setKolicina(kolicine.get(i));
				Zapis_dopolniloDAO.getInstance().shraniZapis_dopolnilo(novZapisDopolnila);

			}

			if(novNasvet.getNasvet() != null) {
			novNasvet.setAvtor(avtor);
			novNasvet.setKartoteka_id(idKartoteke);
			novNasvet.setZapis_id(novZapis.getId());

			if (blockchain.isEmpty()) {
				blockchain.add(new Block(novNasvet.getNasvet(), "0"));
				novNasvet.setHash(blockchain.get(i).mineBlock(difficulty));
			} else {
				blockchain.add(new Block(novNasvet.getNasvet(), blockchain.get(blockchain.size() - 1).hash));
				novNasvet.setHash(blockchain.get(i).mineBlock(difficulty));
			}
			i++;
		
			int id = NasvetDAO.getInstance().shraniNasvet(novNasvet);
			novNasvet = new Nasvet();
			// novZapis.setDopolnila(izbranaDopolnila);
			}
			novZapis = new Zapis();
			novZapisDopolnila = new Zapis_dopolnilo();
			kolicine = new ArrayList<Integer>();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void novaIzdajaLekarnar(String avtor) {
		try {
			String idPacienta = this.getPacientIme().substring(0, this.getPacientIme().indexOf(" -"));
			int idKartoteke = Integer.parseInt(idPacienta);
			novZapis.setKartoteka_id(idKartoteke);

			java.util.Date utilDate = new java.util.Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(utilDate);
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
			novZapis.setCas(cal);
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

	public Kombinacije getNoveKombinacije() {
		return noveKombinacije;
	}

	public void setNoveKombinacije(Kombinacije noveKombinacije) {
		this.noveKombinacije = noveKombinacije;
	}

	public ArrayList<Kombinacije> getIzbraneKombinacije() {
		return izbraneKombinacije;
	}

	public void setIzbraneKombinacije(ArrayList<Kombinacije> izbraneKombinacije) {
		this.izbraneKombinacije = izbraneKombinacije;
	}

}