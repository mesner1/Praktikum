

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DopolniloDAO {

	
	 private DopolniloDAO() {
			try {
				//DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/fitnes");	
				kreirajDopolnilo();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	 
		private static DopolniloDAO inst=new DopolniloDAO();
		
		public static DopolniloDAO getInstance() {
			return inst;
		}
	 
	 
		public void kreirajDopolnilo() throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			Connection conn=null;
			try {
				conn=ds.getConnection();
				conn.createStatement().execute("create table dopolnilo (id int auto_increment, naziv varchar(255), naRecept integer, trajanje integer, primary key (id))");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
		}
		
		
		public Dopolnilo najdiDopolnilo(int id) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			System.out.println("DAO: išèem "+id);
			Dopolnilo ret = null;
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from dopolnilo where id=?",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ret = new Dopolnilo(id, rs.getString("naziv"), rs.getInt("naRecept"), rs.getInt("trajanje"));
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return ret;
		}
		
		
		public Dopolnilo najdiDopolnilo(String naziv) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			System.out.println("DAO: išèem "+naziv);
			Dopolnilo ret = null;
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from dopolnilo where naziv=?",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setString(1, naziv);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ret = new Dopolnilo(rs.getInt("id"), naziv, rs.getInt("naRecept"), rs.getInt("trajanje"));
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return ret;
		}
		
		
		public void shraniDopolnilo(Dopolnilo o) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			System.out.println("DAO: shranjujem zdravilo "+o);
			Connection conn=null;
			try {
				conn=ds.getConnection();
				if(o==null) return;
					PreparedStatement ps = conn.prepareStatement("insert into dopolnilo(naziv , naRecept, trajanje) values (?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
					ps.setString(1, o.getNaziv());
					ps.setInt(2, o.getNaRecept());
					ps.setInt(3, o.getTrajanje());
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
		
		
		public List<Dopolnilo> vrniVse() throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			System.out.println(("DAO: vraèam vse èlane"));
			List<Dopolnilo> ret = new ArrayList<Dopolnilo>();
			Connection conn=null;
			try {
				conn=ds.getConnection();

				ResultSet rs=conn.createStatement().executeQuery("select * from dopolnilo");
				while (rs.next()) {
					Dopolnilo o = new Dopolnilo(rs.getString("naziv"), rs.getInt("naRecept"), rs.getInt("trajanje"));
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