package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import dao.*;
import vao.*;



public class ZapisDAO {

	
	 private ZapisDAO() {
			try {
				//DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/fitnes");	
				kreirajZapis();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	 
		private static ZapisDAO inst=new ZapisDAO();
		
		public static ZapisDAO getInstance() {
			return inst;
		}
	 
	 
		public void kreirajZapis() throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			Connection conn=null;
			try {
				conn=ds.getConnection();
				conn.createStatement().execute("create table if not exists zapis (id int auto_increment not null, cas timestamp not null, kartoteka_id integer not null, tip_id integer not null, avtor varchar(255), izdan integer, primary key (id))");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
		}
		
		

		public Zapis najdiZapis(int id) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			System.out.println("DAO: išèem "+id);
			Zapis ret = null;
			Tip_zapis tip = new Tip_zapis();
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from zapis where id=?",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
//					PreparedStatement ps2 = conn.prepareStatement("select * from tip where id=?",PreparedStatement.RETURN_GENERATED_KEYS);
//					ps2.setInt(1, rs.getInt("tip_id"));
//					ResultSet rs2 = ps2.executeQuery();
					tip = Tip_zapisDAO.getInstance().najdiTip(rs.getInt("tip_id"));
					//tip = rs2.getString("naziv");
					GregorianCalendar cas = new GregorianCalendar();
					cas.setTimeInMillis(rs.getTimestamp("cas").getTime());
					ret = new Zapis(id, cas, rs.getInt("kartoteka_id"), tip.getNaziv(), rs.getString("avtor"));
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return ret;
		}
		
		public Zapis najdiZapis() throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			Zapis ret = null;
			Tip_zapis tip = new Tip_zapis();
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM zapis WHERE id = (SELECT MAX(id) FROM zapis where tip_id=?)",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, 2);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					GregorianCalendar cas = new GregorianCalendar();
					cas.setTimeInMillis(rs.getTimestamp("cas").getTime());
					ret = new Zapis(rs.getInt("id"), cas, rs.getInt("kartoteka_id"), "izdaja", rs.getString("avtor"));
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return ret;
		}
		
		
		
		public Zapis najdiZgoljIzdane(int id) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			System.out.println("DAO: išèem "+id);
			Zapis ret = null;
			Tip_zapis tip = new Tip_zapis();
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from zapis where id=? AND tip_id=?",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, id);
				ps.setInt(2, 2);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
//					PreparedStatement ps2 = conn.prepareStatement("select * from tip where id=?",PreparedStatement.RETURN_GENERATED_KEYS);
//					ps2.setInt(1, rs.getInt("tip_id"));
//					ResultSet rs2 = ps2.executeQuery();
					tip = Tip_zapisDAO.getInstance().najdiTip(rs.getInt("tip_id"));
					//tip = rs2.getString("naziv");
					GregorianCalendar cas = new GregorianCalendar();
					cas.setTimeInMillis(rs.getTimestamp("cas").getTime());
					ret = new Zapis(id, cas, rs.getInt("kartoteka_id"), tip.getNaziv(), rs.getString("avtor"));
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return ret;
		}
		
		
		public void posodobiIzdano(int id) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("update zapis set izdan=?, cas=cas where id=?",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1,  1);
				ps.setInt(2, id);
				ps.executeUpdate();
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
		}
		
		
		
		public void shraniZapis(Zapis o) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			System.out.println("DAO: shranjujem zdravilo "+o);
			Tip_zapis tip = new Tip_zapis();
			Connection conn=null;
			try {
				conn=ds.getConnection();
				if(o==null) return;
					PreparedStatement ps = conn.prepareStatement("insert into zapis(cas , kartoteka_id, tip_id, avtor, izdan) values (?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
					System.out.println("prenesen cas: " + o.getCas());
//					Date novDatumM = o.getCas();
//					java.sql.Date novDatumMsql = new java.sql.Date(novDatumM.getTime());
//					System.out.println("NOV MYSQL CAS: " + novDatumMsql);
					ps.setTimestamp(1, new Timestamp(o.getCas().getTimeInMillis()));
					ps.setInt(2, o.getKartoteka_id());				
					tip = Tip_zapisDAO.getInstance().najdiTip(o.getTip());
					ps.setInt(3,  tip.getId());
					ps.setString(4, o.getAvtor());
					ps.setInt(5, o.getIzdan());
					ps.executeUpdate();
					ResultSet res = ps.getGeneratedKeys();
					while (res.next())
						o.setId(res.getInt(1));
					res.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
		}
		
		
    	public List<Zapis> vrniVse(int id) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			List<Zapis> ret = new ArrayList<Zapis>();
			List<Integer> ids = new ArrayList<Integer>();
			List<Zapis_dopolnilo> vsaDopolnilaZapisa = new ArrayList<Zapis_dopolnilo>();
			Tip_zapis tip = new Tip_zapis();
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from zapis WHERE kartoteka_id=? ORDER BY cas DESC",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ArrayList<Dopolnilo> dopolnila = new ArrayList<Dopolnilo>();
					
					tip = Tip_zapisDAO.getInstance().najdiTip(rs.getInt("tip_id"));
					//tip = rs2.getString("naziv");	
					GregorianCalendar cas = new GregorianCalendar();
					cas.setTimeInMillis((rs.getTimestamp("cas").getTime()));
					Zapis o = new Zapis(cas, rs.getInt("kartoteka_id"), tip.getNaziv(), rs.getString("avtor"), dopolnila);
					o.getCas().setTimeInMillis(rs.getTimestamp("cas").getTime());
					o.setId(rs.getInt("id"));
					ids.add(o.getId());
					PreparedStatement ps2 = conn.prepareStatement("select * from zapis_dopolnilo WHERE zapis_id=?",PreparedStatement.RETURN_GENERATED_KEYS);
					ps2.setInt(1, o.getId());
					ResultSet rs2 = ps2.executeQuery();
					while (rs2.next()) {
						Zapis_dopolnilo zd = new Zapis_dopolnilo(rs2.getInt("dopolnilo_id"), rs2.getInt("zapis_id"), rs2.getInt("kolicina"));
						zd.setId(rs2.getInt("id"));
						vsaDopolnilaZapisa.add(zd);
						
						PreparedStatement ps3 = conn.prepareStatement("select * from dopolnilo WHERE id=?",PreparedStatement.RETURN_GENERATED_KEYS);
						ps3.setInt(1, zd.getDopolnilo_id());
						ResultSet rs3 = ps3.executeQuery();
						while (rs3.next()) {
							Dopolnilo dopolnilo = new Dopolnilo(rs3.getString("naziv"), rs3.getInt("naRecept"), rs3.getInt("trajanje"), rs2.getInt("kolicina"));
							dopolnilo.setId(rs3.getInt("id"));
							dopolnila.add(dopolnilo);
						}
					}
					
					o.setDopolnila(dopolnila);
					ret.add(o);
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return ret;
		}
    	
    	public List<Zapis> vrniVseNeizdane(int id) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			List<Zapis> ret = new ArrayList<Zapis>();
			List<Integer> ids = new ArrayList<Integer>();
			List<Zapis_dopolnilo> vsaDopolnilaZapisa = new ArrayList<Zapis_dopolnilo>();
			Tip_zapis tip = new Tip_zapis();
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from zapis WHERE kartoteka_id=? AND tip_id=? AND izdan=? ORDER BY cas DESC",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, id);
				ps.setInt(2, 1);
				ps.setInt(3, 0);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ArrayList<Dopolnilo> dopolnila = new ArrayList<Dopolnilo>();
					
					tip = Tip_zapisDAO.getInstance().najdiTip(rs.getInt("tip_id"));
					//tip = rs2.getString("naziv");
					GregorianCalendar cas = new GregorianCalendar();
					cas.setTimeInMillis(rs.getTimestamp("cas").getTime());
					Zapis o = new Zapis(cas, rs.getInt("kartoteka_id"), tip.getNaziv(), rs.getString("avtor"), dopolnila);
					o.getCas().setTimeInMillis(rs.getTimestamp("cas").getTime());
					o.setId(rs.getInt("id"));

					ids.add(o.getId());
					PreparedStatement ps2 = conn.prepareStatement("select * from zapis_dopolnilo WHERE zapis_id=?",PreparedStatement.RETURN_GENERATED_KEYS);
					ps2.setInt(1, o.getId());
					ResultSet rs2 = ps2.executeQuery();
					while (rs2.next()) {
						Zapis_dopolnilo zd = new Zapis_dopolnilo(rs2.getInt("dopolnilo_id"), rs2.getInt("zapis_id"), rs2.getInt("kolicina"));
						zd.setId(rs2.getInt("id"));
						vsaDopolnilaZapisa.add(zd);
						
						PreparedStatement ps3 = conn.prepareStatement("select * from dopolnilo WHERE id=?",PreparedStatement.RETURN_GENERATED_KEYS);
						ps3.setInt(1, zd.getDopolnilo_id());
						ResultSet rs3 = ps3.executeQuery();
						while (rs3.next()) {
							Dopolnilo dopolnilo = new Dopolnilo(rs3.getString("naziv"), rs3.getInt("naRecept"), rs3.getInt("trajanje"), rs2.getInt("kolicina"));
							dopolnilo.setId(rs3.getInt("id"));
							dopolnila.add(dopolnilo);
						}
					}
					
					o.setDopolnila(dopolnila);
					ret.add(o);
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return ret;
		}
    	
    	
    	public List<Zapis> vrniVseNeizdane() throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			List<Zapis> ret = new ArrayList<Zapis>();
			List<Integer> ids = new ArrayList<Integer>();
			List<Zapis_dopolnilo> vsaDopolnilaZapisa = new ArrayList<Zapis_dopolnilo>();
			Tip_zapis tip = new Tip_zapis();
			Kartoteka k = new Kartoteka();
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from zapis WHERE tip_id=? AND izdan=? ORDER BY cas DESC",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, 1);
				ps.setInt(2, 0);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {

					
					ArrayList<Dopolnilo> dopolnila = new ArrayList<Dopolnilo>();
					
					tip = Tip_zapisDAO.getInstance().najdiTip(rs.getInt("tip_id"));
					//tip = rs2.getString("naziv");
					GregorianCalendar cas = new GregorianCalendar();
					cas.setTimeInMillis(rs.getTimestamp("cas").getTime());
					Zapis o = new Zapis(cas, rs.getInt("kartoteka_id"), tip.getNaziv(), rs.getString("avtor"), dopolnila);
					
					
					PreparedStatement ps4 = conn.prepareStatement("select * from kartoteka WHERE id=?",PreparedStatement.RETURN_GENERATED_KEYS);
					ps4.setInt(1, o.getKartoteka_id());
					ResultSet rs4 = ps4.executeQuery();
					while (rs4.next()) {
						k = new Kartoteka(o.getKartoteka_id(), rs4.getString("ime"), rs4.getString("priimek"), rs4.getString("email"));
					}
					o.setPacient(k.getIme() + " " + k.getPriimek());
					
					o.getCas().setTimeInMillis(rs.getTimestamp("cas").getTime());
					o.setId(rs.getInt("id"));
					

					


					ids.add(o.getId());
					PreparedStatement ps2 = conn.prepareStatement("select * from zapis_dopolnilo WHERE zapis_id=?",PreparedStatement.RETURN_GENERATED_KEYS);
					ps2.setInt(1, o.getId());
					ResultSet rs2 = ps2.executeQuery();
					while (rs2.next()) {
						Zapis_dopolnilo zd = new Zapis_dopolnilo(rs2.getInt("dopolnilo_id"), rs2.getInt("zapis_id"), rs2.getInt("kolicina"));
						zd.setId(rs2.getInt("id"));
						vsaDopolnilaZapisa.add(zd);
						
						PreparedStatement ps3 = conn.prepareStatement("select * from dopolnilo WHERE id=?",PreparedStatement.RETURN_GENERATED_KEYS);
						ps3.setInt(1, zd.getDopolnilo_id());
						ResultSet rs3 = ps3.executeQuery();
						while (rs3.next()) {
							Dopolnilo dopolnilo = new Dopolnilo(rs3.getString("naziv"), rs3.getInt("naRecept"), rs3.getInt("trajanje"), rs2.getInt("kolicina"));
							dopolnilo.setId(rs3.getInt("id"));
							dopolnila.add(dopolnilo);
							

						}
					}
					
					o.setDopolnila(dopolnila);
					ret.add(o);
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return ret;
		}
    	
    	
    	public List<Zapis> vrniVseIzdane(int id) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			List<Zapis> ret = new ArrayList<Zapis>();
			List<Integer> ids = new ArrayList<Integer>();
			List<Zapis_dopolnilo> vsaDopolnilaZapisa = new ArrayList<Zapis_dopolnilo>();
			Tip_zapis tip = new Tip_zapis();
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from zapis WHERE kartoteka_id=? AND tip_id=? ORDER BY cas DESC",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, id);
				ps.setInt(2, 2);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ArrayList<Dopolnilo> dopolnila = new ArrayList<Dopolnilo>();
					
					tip = Tip_zapisDAO.getInstance().najdiTip(rs.getInt("tip_id"));
					//tip = rs2.getString("naziv");			
					GregorianCalendar cas = new GregorianCalendar();
					cas.setTimeInMillis(rs.getTimestamp("cas").getTime());
					Zapis o = new Zapis(cas, rs.getInt("kartoteka_id"), tip.getNaziv(), rs.getString("avtor"), dopolnila);
					o.getCas().setTimeInMillis(rs.getTimestamp("cas").getTime());
					o.setId(rs.getInt("id"));
					
					System.out.println("vmesna1: " + o.getAvtor());
					System.out.println("OGETI" + o.getId());
					ids.add(o.getId());
					PreparedStatement ps2 = conn.prepareStatement("select * from zapis_dopolnilo WHERE zapis_id=?",PreparedStatement.RETURN_GENERATED_KEYS);
					ps2.setInt(1, o.getId());
					ResultSet rs2 = ps2.executeQuery();
					while (rs2.next()) {
						Zapis_dopolnilo zd = new Zapis_dopolnilo(rs2.getInt("dopolnilo_id"), rs2.getInt("zapis_id"), rs2.getInt("kolicina"));
						zd.setId(rs2.getInt("id"));
						vsaDopolnilaZapisa.add(zd);
						
						PreparedStatement ps3 = conn.prepareStatement("select * from dopolnilo WHERE id=?",PreparedStatement.RETURN_GENERATED_KEYS);
						ps3.setInt(1, zd.getDopolnilo_id());
						ResultSet rs3 = ps3.executeQuery();
						while (rs3.next()) {
							Dopolnilo dopolnilo = new Dopolnilo(rs3.getString("naziv"), rs3.getInt("naRecept"), rs3.getInt("trajanje"), rs2.getInt("kolicina"));
							dopolnilo.setId(rs3.getInt("id"));
							dopolnila.add(dopolnilo);
						}
					}
					
					o.setDopolnila(dopolnila);
					ret.add(o);
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return ret;
		}
    	
    	
    	public List<Zapis> vrniVseIzdane() throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			List<Zapis> ret = new ArrayList<Zapis>();
			Kartoteka k = new Kartoteka();
			List<Integer> ids = new ArrayList<Integer>();
			List<Zapis_dopolnilo> vsaDopolnilaZapisa = new ArrayList<Zapis_dopolnilo>();
			Tip_zapis tip = new Tip_zapis();
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from zapis WHERE tip_id=? ORDER BY cas DESC",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, 2);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ArrayList<Dopolnilo> dopolnila = new ArrayList<Dopolnilo>();
					
					tip = Tip_zapisDAO.getInstance().najdiTip(rs.getInt("tip_id"));
					//tip = rs2.getString("naziv");			
					GregorianCalendar cas = new GregorianCalendar();
					cas.setTimeInMillis(rs.getTimestamp("cas").getTime());
					Zapis o = new Zapis(cas, rs.getInt("kartoteka_id"), tip.getNaziv(), rs.getString("avtor"), dopolnila);
					
					PreparedStatement ps4 = conn.prepareStatement("select * from kartoteka WHERE id=?",PreparedStatement.RETURN_GENERATED_KEYS);
					ps4.setInt(1, o.getKartoteka_id());
					ResultSet rs4 = ps4.executeQuery();
					while (rs4.next()) {
						k = new Kartoteka(o.getKartoteka_id(), rs4.getString("ime"), rs4.getString("priimek"), rs4.getString("email"));
					}
					o.setPacient(k.getIme() + " " + k.getPriimek());
					
					o.getCas().setTimeInMillis(rs.getTimestamp("cas").getTime());
					o.setId(rs.getInt("id"));
					
					System.out.println("vmesna1: " + o.getAvtor());
					System.out.println("OGETI" + o.getId());
					ids.add(o.getId());
					PreparedStatement ps2 = conn.prepareStatement("select * from zapis_dopolnilo WHERE zapis_id=?",PreparedStatement.RETURN_GENERATED_KEYS);
					ps2.setInt(1, o.getId());
					ResultSet rs2 = ps2.executeQuery();
					while (rs2.next()) {
						Zapis_dopolnilo zd = new Zapis_dopolnilo(rs2.getInt("dopolnilo_id"), rs2.getInt("zapis_id"), rs2.getInt("kolicina"));
						zd.setId(rs2.getInt("id"));
						vsaDopolnilaZapisa.add(zd);
						
						PreparedStatement ps3 = conn.prepareStatement("select * from dopolnilo WHERE id=?",PreparedStatement.RETURN_GENERATED_KEYS);
						ps3.setInt(1, zd.getDopolnilo_id());
						ResultSet rs3 = ps3.executeQuery();
						while (rs3.next()) {
							Dopolnilo dopolnilo = new Dopolnilo(rs3.getString("naziv"), rs3.getInt("naRecept"), rs3.getInt("trajanje"), rs2.getInt("kolicina"));
							dopolnilo.setId(rs3.getInt("id"));
							dopolnila.add(dopolnilo);
						}
					}
					
					o.setDopolnila(dopolnila);
					ret.add(o);
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return ret;
		}
    	
    	
    	public List<Zapis> vrniVseNeizdane(int id, int neizdani) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			List<Zapis> ret = new ArrayList<Zapis>();
			List<Integer> ids = new ArrayList<Integer>();
			List<Zapis_dopolnilo> vsaDopolnilaZapisa = new ArrayList<Zapis_dopolnilo>();
			Tip_zapis tip = new Tip_zapis();
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from zapis WHERE kartoteka_id=? AND tip_id=? ORDER BY cas DESC",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, id);
				ps.setInt(2, neizdani);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ArrayList<Dopolnilo> dopolnila = new ArrayList<Dopolnilo>();
					
					tip = Tip_zapisDAO.getInstance().najdiTip(rs.getInt("tip_id"));
					//tip = rs2.getString("naziv");		
					GregorianCalendar cas = new GregorianCalendar();
					cas.setTimeInMillis(rs.getTimestamp("cas").getTime());
					Zapis o = new Zapis(cas, rs.getInt("kartoteka_id"), tip.getNaziv(), rs.getString("avtor"), dopolnila);
					o.getCas().setTimeInMillis(rs.getTimestamp("cas").getTime());
					o.setId(rs.getInt("id"));
					

					ids.add(o.getId());
					PreparedStatement ps2 = conn.prepareStatement("select * from zapis_dopolnilo WHERE zapis_id=?",PreparedStatement.RETURN_GENERATED_KEYS);
					ps2.setInt(1, o.getId());
					ResultSet rs2 = ps2.executeQuery();
					while (rs2.next()) {
						Zapis_dopolnilo zd = new Zapis_dopolnilo(rs2.getInt("dopolnilo_id"), rs2.getInt("zapis_id"), rs2.getInt("kolicina"));
						zd.setId(rs2.getInt("id"));
						vsaDopolnilaZapisa.add(zd);
						
						PreparedStatement ps3 = conn.prepareStatement("select * from dopolnilo WHERE id=?",PreparedStatement.RETURN_GENERATED_KEYS);
						ps3.setInt(1, zd.getDopolnilo_id());
						ResultSet rs3 = ps3.executeQuery();
						while (rs3.next()) {
							Dopolnilo dopolnilo = new Dopolnilo(rs3.getString("naziv"), rs3.getInt("naRecept"), rs3.getInt("trajanje"), rs2.getInt("kolicina"));
							dopolnilo.setId(rs3.getInt("id"));
							dopolnila.add(dopolnilo);
						}
					}
					
					o.setDopolnila(dopolnila);
					ret.add(o);
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return ret;
		}
    	
    	
    	
    	
    	
    	public List<Integer> vrniVseZauzite(int id) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			//List<Zapis> ret = new ArrayList<Zapis>();
			List<Integer> ids = new ArrayList<Integer>();
			Tip_zapis tip = new Tip_zapis();
			Connection conn=null;
			//trenutni�as
		//	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar trenutniCas = Calendar.getInstance();
		//	System.out.println("prvi:"+dateFormat.format(cal));
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from zapis WHERE kartoteka_id=? AND tip_id=? ORDER BY cas DESC",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, id);
				ps.setInt(2, 3);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ArrayList<Dopolnilo> dopolnila = new ArrayList<Dopolnilo>();
					
					tip = Tip_zapisDAO.getInstance().najdiTip(rs.getInt("tip_id"));
					//tip = rs2.getString("naziv");		
					GregorianCalendar cas = new GregorianCalendar();
					System.out.println("TIP: " + tip.getNaziv());
					cas.setTimeInMillis(rs.getTimestamp("cas").getTime());
					Zapis o = new Zapis(cas, rs.getInt("kartoteka_id"), tip.getNaziv(), rs.getString("avtor"), dopolnila);
					o.getCas().setTimeInMillis(rs.getTimestamp("cas").getTime());
					o.setId(rs.getInt("id"));

					
					System.out.println("ttenutni:"+trenutniCas.getTime());
					System.out.println("drugi:"+cas.getTime());
					
					int i = cas.compareTo(trenutniCas);

					if(i == 1) {
						ids.add(o.getId());
					}

				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return ids;
		}

	 
}