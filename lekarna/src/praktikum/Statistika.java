package praktikum;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import dao.DopolniloDAO;
import dao.KartotekaDAO;
import dao.ZapisDAO;
import dao.Zapis_dopolniloDAO;
import vao.Dopolnilo;
import vao.Kartoteka;
import vao.Kombinacije;
import vao.Zapis;
import vao.Zapis_dopolnilo;

@ManagedBean(name = "zrnoStatistika")
@SessionScoped
public class Statistika {

	private ArrayList<Dopolnilo> zdravila = new ArrayList<Dopolnilo>();
	private Dopolnilo izbranoZdravilo = new Dopolnilo();

	public ArrayList<Dopolnilo> getZdravila() throws Exception {
		zdravila = (ArrayList<Dopolnilo>) DopolniloDAO.getInstance().vrniVse();
		return zdravila;
	}

	public void setZdravila(ArrayList<Dopolnilo> zdravila) {
		this.zdravila = zdravila;
	}

	public Dopolnilo getIzbranoZdravilo() {
		return izbranoZdravilo;
	}

	public void setIzbranoZdravilo(Dopolnilo izbranoZdravilo) {
		this.izbranoZdravilo = izbranoZdravilo;
	}

	private LineChartModel grafZdravilo;
	private LineChartModel grafVseh;

	@PostConstruct
	public void init(Dopolnilo izbranoZdravilo2) throws Exception {
		System.out.println("izbrano zdravilo" + izbranoZdravilo2);
		createLineModels(izbranoZdravilo2);
	}

	@PostConstruct
	public void init() throws Exception {
		createLineModels();
	}

	public LineChartModel getGrafZdravilo() {
		return grafZdravilo;
	}

	public LineChartModel getGrafVseh() {
		return grafVseh;
	}

	private void createLineModels() throws Exception {
		// GRAF VSEH
		grafVseh = initVsi();
		grafVseh.setTitle("Graf izdane kolièine");
		grafVseh.setLegendPosition("e");
		grafVseh.setShowPointLabels(true);
		grafVseh.getAxes().put(AxisType.X, new CategoryAxis("Datum"));
		Axis yAxis = grafVseh.getAxis(AxisType.Y);
		yAxis.setLabel("Kolièina");
		yAxis.setMin(0);
	}

	private void createLineModels(Dopolnilo izbranoZdravilo2) throws Exception {

		// POSAMEZNO
		grafZdravilo = initCategoryModel(izbranoZdravilo2);
		grafZdravilo.setTitle("Graf izdane kolièine");
		grafZdravilo.setLegendPosition("e");
		grafZdravilo.setShowPointLabels(true);
		grafZdravilo.getAxes().put(AxisType.X, new CategoryAxis("Datum"));
		Axis yAxis = grafZdravilo.getAxis(AxisType.Y);
		yAxis.setLabel("Kolièina");
		yAxis.setMin(0);
		// yAxis.setMax(100);
	}

	private LineChartModel initCategoryModel(Dopolnilo izbranoZdravilo2) throws Exception {
		LineChartModel model = new LineChartModel();
		Dopolnilo dopolnilo = DopolniloDAO.getInstance().najdiDopolnilo(izbranoZdravilo2.getId());
		System.out.println("id dopolnila: " + dopolnilo.getId());

		ArrayList<Zapis_dopolnilo> zapisi_dopolnila = new ArrayList<Zapis_dopolnilo>();

		zapisi_dopolnila = Zapis_dopolniloDAO.getInstance().najdiVseZapise(dopolnilo.getId());
		if (!zapisi_dopolnila.isEmpty()) {
			ArrayList<Zapis> zapisi = new ArrayList<Zapis>();
			for (int i = 0; i < zapisi_dopolnila.size(); i++) {
				Zapis izdan = new Zapis();
				izdan = ZapisDAO.getInstance().najdiZgoljIzdane(zapisi_dopolnila.get(i).getZapis_id());
				if (izdan != null)
					zapisi.add(izdan);
			}
			ChartSeries dopolnila = new ChartSeries();
			dopolnila.setLabel(izbranoZdravilo2.getNaziv());

			// zapisi_dopolnila z zapisom_id IZDAJA
			ArrayList<Zapis_dopolnilo> zapisi_dopolnila2 = new ArrayList<Zapis_dopolnilo>();
			zapisi_dopolnila2 = Zapis_dopolniloDAO.getInstance().najdiVseZapise(dopolnilo.getId());
			ArrayList<Integer> kolicine = new ArrayList<Integer>();
			for (int i = 0; i < zapisi.size(); i++) {
				for (int j = 0; j < zapisi_dopolnila.size(); j++) {
					if (zapisi.get(i).getId() == zapisi_dopolnila.get(j).getZapis_id()) {
						kolicine.add(zapisi_dopolnila.get(j).getKolicina());

					}
				}
			}

			ArrayList<String> datumi = new ArrayList<String>();
			// isti dan + seštevek kolièine
			for (int i = 0; i < zapisi.size(); i++) {
				datumi.add(zapisi.get(i).getLeDatumCas());
			}

			System.out.println("DATUMI: " + datumi);
			System.out.println("ZAPISI_DOP: " + kolicine);

			ArrayList<String> razlicni = new ArrayList<String>();

			razlicni.add(datumi.get(0));
			for (int i = 0; i < datumi.size(); i++) {
				int stevec = 0;
				if (!datumi.get(0).equals(datumi.get(i))) {
					for (int j = 0; j < razlicni.size(); j++) {
						if (!datumi.get(i).equals(razlicni.get(j))) {
							stevec++;
						}
						if (stevec == razlicni.size()) {
							razlicni.add(datumi.get(i));
						}

					}
				}

			}

			int[] sestevek = new int[razlicni.size()];
			for (int i = 0; i < razlicni.size(); i++) {
				int kolicina = 0;
				String datum = razlicni.get(i);
				for (int j = 0; j < datumi.size(); j++) {
					if (datum.equals(datumi.get(j))) {
						kolicina += kolicine.get(j);
					}
				}
				sestevek[i] = kolicina;
			}

			System.out.println("polje datumov" + razlicni);
			for (int i = 0; i < sestevek.length; i++) {
				System.out.println("sestevek: " + sestevek[i]);
			}

			for (int i = 0; i < razlicni.size(); i++) {
				dopolnila.set(razlicni.get(i), sestevek[i]);
			}

			model.addSeries(dopolnila);
		} else {
			ChartSeries dopolnila = new ChartSeries();
			dopolnila.setLabel(izbranoZdravilo2.getNaziv());
			dopolnila.set(0, 0);
			model.addSeries(dopolnila);
		}

		return model;
	}

	private LineChartModel initVsi() throws Exception {
		LineChartModel model = new LineChartModel();
		ArrayList<Dopolnilo> vsa = (ArrayList<Dopolnilo>) DopolniloDAO.getInstance().vrniVse();
		for (int x = 0; x < vsa.size(); x++) {
			Dopolnilo dopolnilo = DopolniloDAO.getInstance().najdiDopolnilo(vsa.get(x).getId());
			System.out.println("id dopolnila: " + dopolnilo.getId());

			ArrayList<Zapis_dopolnilo> zapisi_dopolnila = new ArrayList<Zapis_dopolnilo>();

			zapisi_dopolnila = Zapis_dopolniloDAO.getInstance().najdiVseZapise(dopolnilo.getId());
			if (!zapisi_dopolnila.isEmpty()) {
				ArrayList<Zapis> zapisi = new ArrayList<Zapis>();
				for (int i = 0; i < zapisi_dopolnila.size(); i++) {
					Zapis izdan = new Zapis();
					izdan = ZapisDAO.getInstance().najdiZgoljIzdane(zapisi_dopolnila.get(i).getZapis_id());
					if (izdan != null)
						zapisi.add(izdan);
				}
				ChartSeries dopolnila = new ChartSeries();
				dopolnila.setLabel(vsa.get(x).getNaziv());

				// zapisi_dopolnila z zapisom_id IZDAJA
				ArrayList<Zapis_dopolnilo> zapisi_dopolnila2 = new ArrayList<Zapis_dopolnilo>();
				zapisi_dopolnila2 = Zapis_dopolniloDAO.getInstance().najdiVseZapise(dopolnilo.getId());
				ArrayList<Integer> kolicine = new ArrayList<Integer>();
				for (int i = 0; i < zapisi.size(); i++) {
					for (int j = 0; j < zapisi_dopolnila.size(); j++) {
						if (zapisi.get(i).getId() == zapisi_dopolnila.get(j).getZapis_id()) {
							kolicine.add(zapisi_dopolnila.get(j).getKolicina());

						}
					}
				}

				ArrayList<String> datumi = new ArrayList<String>();
				// isti dan + seštevek kolièine
				for (int i = 0; i < zapisi.size(); i++) {
					datumi.add(zapisi.get(i).getLeDatumCas());
				}

				System.out.println("DATUMI: " + datumi);
				System.out.println("ZAPISI_DOP: " + kolicine);

				ArrayList<String> razlicni = new ArrayList<String>();

				razlicni.add(datumi.get(0));
				for (int i = 0; i < datumi.size(); i++) {
					int stevec = 0;
					if (!datumi.get(0).equals(datumi.get(i))) {
						for (int j = 0; j < razlicni.size(); j++) {
							if (!datumi.get(i).equals(razlicni.get(j))) {
								stevec++;
							}
							if (stevec == razlicni.size()) {
								razlicni.add(datumi.get(i));
							}

						}
					}

				}

				int[] sestevek = new int[razlicni.size()];
				for (int i = 0; i < razlicni.size(); i++) {
					int kolicina = 0;
					String datum = razlicni.get(i);
					for (int j = 0; j < datumi.size(); j++) {
						if (datum.equals(datumi.get(j))) {
							kolicina += kolicine.get(j);
						}
					}
					sestevek[i] = kolicina;
				}

				System.out.println("polje datumov" + razlicni);
				for (int i = 0; i < sestevek.length; i++) {
					System.out.println("sestevek: " + sestevek[i]);
				}

				for (int i = 0; i < razlicni.size(); i++) {
					dopolnila.set(razlicni.get(i), sestevek[i]);
				}

				model.addSeries(dopolnila);
			} else {
				ChartSeries dopolnila = new ChartSeries();
				dopolnila.setLabel(vsa.get(x).getNaziv());
				
//				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
//				Date date = new Date();
//				System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
//				
//				dopolnila.set(dateFormat.format(date), 0);
//				model.addSeries(dopolnila);
			}
		}
		return model;

	}

}