

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class KartotekaDAO {

	 private KartotekaDAO() {
			try {
				//DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/fitnes");	
				kreirajKartoteko();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	 
		private static KartotekaDAO inst=new KartotekaDAO();
		
		public static KartotekaDAO getInstance() {
			return inst;
		}
	 
	 
		public void kreirajKartoteko() throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			Connection conn=null;
			try {
				conn=ds.getConnection();
				conn.createStatement().execute("create table kartoteka (id int auto_increment, ime varchar(255), priimek varchar(255), primary key (id))");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
		}
		
		
		public Kartoteka najdiKartoteko(int id) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			System.out.println("DAO: išèem "+id);
			Kartoteka ret = null;
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from kartoteka where id=?",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ret = new Kartoteka(id, rs.getString("ime"), rs.getString("priimek"));
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return ret;
		}
		
		
		public void shraniKartoteko(Kartoteka o) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			System.out.println("DAO: shranjujem kartoteko "+o);
			Connection conn=null;
			try {
				conn=ds.getConnection();
				if(o==null) return;
					PreparedStatement ps = conn.prepareStatement("insert into kartoteka(ime , priimek) values (?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
					ps.setString(1, o.getIme());
					ps.setString(2, o.getPriimek());
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
		
		
		public List<Kartoteka> vrniVse() throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			System.out.println(("DAO: vraèam vse èlane"));
			List<Kartoteka> ret = new ArrayList<Kartoteka>();
			Connection conn=null;
			try {
				conn=ds.getConnection();

				ResultSet rs=conn.createStatement().executeQuery("select * from kartoteka");
				while (rs.next()) {
					Kartoteka o = new Kartoteka(rs.getString("ime"), rs.getString("priimek"));
					o.setId(rs.getInt("id"));
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