

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Zapis {
	private int id;
	private Calendar cas;
	private int kartoteka_id;
	private String tip;
	private String avtor;
	private int izdan;
	private ArrayList<Dopolnilo> dopolnila;

	public Zapis() {
	}

	public Zapis(int id, Calendar cas, int kartoteka_id, String tip, String avtor) {
		this.id = id;
		this.cas = cas;
		this.kartoteka_id = kartoteka_id;
		this.tip = tip;
		this.avtor = avtor;
	}

	public Zapis(Calendar cas, int kartoteka_id, String tip, String avtor) {
		this.cas = cas;
		this.kartoteka_id = kartoteka_id;
		this.tip = tip;
		this.avtor = avtor;
	}

	public Zapis(Calendar cas, int kartoteka_id, String tip, String avtor, int izdan, ArrayList<Dopolnilo> dopolnila) {
		this.cas = cas;
		this.kartoteka_id = kartoteka_id;
		this.tip = tip;
		this.avtor = avtor;
		this.izdan = izdan;
		this.dopolnila = dopolnila;
	}

	public Zapis(Calendar cas, int kartoteka_id, String tip, String avtor, ArrayList<Dopolnilo> dopolnila) {
		this.cas = cas;
		this.kartoteka_id = kartoteka_id;
		this.tip = tip;
		this.avtor = avtor;
		this.dopolnila = dopolnila;
	}

	public Zapis(Calendar cas, int kartoteka_id, String tip, ArrayList<Dopolnilo> dopolnila) {
		this.cas = cas;
		this.kartoteka_id = kartoteka_id;
		this.tip = tip;
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

	public Calendar getCas() {
		return cas;
	}

	public void setCas(Calendar cas) {
		this.cas = cas;
	}

	public int getKartoteka_id() {
		return kartoteka_id;
	}

	public void setKartoteka_id(int kartoteka_id) {
		this.kartoteka_id = kartoteka_id;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
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

	public int getIzdan() {
		return izdan;
	}

	public void setIzdan(int izdan) {
		this.izdan = izdan;
	}
	public static LocalDateTime toLocalDateTime(Calendar calendar) {
	      if (calendar == null) {
	          return null;
	      }
	      TimeZone tz = calendar.getTimeZone();
	      
	      
	      //ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
	      ZoneId zid = ZoneId.of("Europe/Ljubljana");
	      
	      
	      return LocalDateTime.ofInstant(calendar.toInstant(), zid);
	  }

	
	public String getDatumCas() {
		LocalDateTime cas2 = toLocalDateTime(cas);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

      String formatDateTime = cas2.format(formatter);
      return formatDateTime;
	}

	// private static SimpleDateFormat sdf=new SimpleDateFormat("dd. MM. yyyy");
//	@Override
//	public String toString() {
//		ArrayList<String> izpisDopolnil = new ArrayList<String>();
//		for (int i = 0; i < dopolnila.size(); i++) {
//			izpisDopolnil.add(dopolnila.get(i).getNaziv());
//		}
//		StringBuilder sb = new StringBuilder();
//		for (String s : izpisDopolnil)
//		{
//		    sb.append(s);
//		    sb.append("\t");
//		}
//		return cas + " " + kartoteka_id + " " + sb.toString() + " " + tip;
//	}
	
	
	@Override
	public String toString() {
		return cas + " " + kartoteka_id + " " + tip;
	}
	


}