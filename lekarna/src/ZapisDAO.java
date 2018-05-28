

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.naming.InitialContext;
import javax.sql.DataSource;

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
				conn.createStatement().execute("create table dopolnilo (id int auto_increment, cas date, kartoteka_id integer, tip_id integer, avtor varchar(255) primary key (id))");
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
			Tip tip = new Tip();
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
					tip = TipDAO.getInstance().najdiTip(rs.getInt("tip_id"));
					//tip = rs2.getString("naziv");
					ret = new Zapis(id, rs.getDate("cas"), rs.getInt("kartoteka_id"), tip.getNaziv(), rs.getString("avtor"));
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return ret;
		}
		
		
		public void shraniZapis(Zapis o) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			System.out.println("DAO: shranjujem zdravilo "+o);
			Tip tip = new Tip();
			Connection conn=null;
			try {
				conn=ds.getConnection();
				if(o==null) return;
					PreparedStatement ps = conn.prepareStatement("insert into zapis(cas , kartoteka_id, tip_id, avtor) values (?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
					Date novDatumM = o.getCas();
					java.sql.Date novDatumMsql = new java.sql.Date(novDatumM.getTime());
					ps.setDate(1, novDatumMsql);
					ps.setInt(2, o.getKartoteka_id());				
					tip = TipDAO.getInstance().najdiTip(o.getTip());
					ps.setInt(3,  tip.getId());
					ps.setString(4, o.getAvtor());
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
			Tip tip = new Tip();
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from zapis WHERE kartoteka_id=?",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ArrayList<Dopolnilo> dopolnila = new ArrayList<Dopolnilo>();
					
					tip = TipDAO.getInstance().najdiTip(rs.getInt("tip_id"));
					//tip = rs2.getString("naziv");					
					Zapis o = new Zapis(rs.getDate("cas"), rs.getInt("kartoteka_id"), tip.getNaziv(), rs.getString("avtor"), dopolnila);
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
			Tip tip = new Tip();
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from zapis WHERE kartoteka_id=? AND tip_id=?",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, id);
				ps.setInt(2, neizdani);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ArrayList<Dopolnilo> dopolnila = new ArrayList<Dopolnilo>();
					
					tip = TipDAO.getInstance().najdiTip(rs.getInt("tip_id"));
					//tip = rs2.getString("naziv");					
					Zapis o = new Zapis(rs.getDate("cas"), rs.getInt("kartoteka_id"), tip.getNaziv(), rs.getString("avtor"), dopolnila);
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
//		
//		public void izbrisiClana(int id) throws Exception {
//			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/fitnes");	
//			System.out.println("DAO: išèem za izbris "+id);
//			Connection conn=null;
//			try {
//				conn=ds.getConnection();
//				PreparedStatement ps = conn.prepareStatement("delete from clan where id=?",PreparedStatement.RETURN_GENERATED_KEYS);
//				PreparedStatement ps2 = conn.prepareStatement("delete from meritev where idClana=?",PreparedStatement.RETURN_GENERATED_KEYS);
//				ps.setInt(1, id);
//				ps2.setInt(1, id);
//				ps.execute();
//				ps2.execute();
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				conn.close();
//			}
//		}
//		
//		
//		public void spremeniClana(Clan o) throws Exception {
//			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/fitnes");	
//			System.out.println("DAO: išèem za izbris "+o);
//			Connection conn=null;
//			try {
//				conn=ds.getConnection();
//				PreparedStatement ps = conn.prepareStatement("update clan set ime=? , priimek=? , spol = ?, datumRojstva = ? , datumVpisa = ?, sifra = ? where id=?");
//				ps.setString(1, o.getIme());
//				ps.setString(2, o.getPriimek());
//				ps.setString(3, o.getSpol());
//				Date novDatumR = o.getDatumRojstva();
//				java.sql.Date novDatumRsql = new java.sql.Date(novDatumR.getTime());
//				ps.setDate(4, novDatumRsql);
//				Date novDatumV = o.getDatumVpisa();
//				java.sql.Date novDatumVsql = new java.sql.Date(novDatumV.getTime());
//				ps.setDate(5, novDatumVsql);
//				ps.setInt(6, o.getSifra());
//				ps.setInt(7, o.getId());
//				ps.executeUpdate();
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				conn.close();
//			}
//		}
	 
}