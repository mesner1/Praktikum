package dao;

import dao.*;
import vao.*;



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
				conn.createStatement().execute("create table dopolnilo (id int auto_increment, naziv varchar(255), naRecept integer, trajanje integer, opis varchar(255), embalaza varchar(255), primary key (id))");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
		}
		
		
		public Dopolnilo najdiDopolnilo(int id) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			Dopolnilo ret = null;
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from dopolnilo where id=?",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ret = new Dopolnilo(id, rs.getString("naziv"), rs.getInt("naRecept"), rs.getInt("trajanje"), rs.getString("opis"));
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
			Dopolnilo ret = null;
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from dopolnilo where naziv=?",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setString(1, naziv);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ret = new Dopolnilo(rs.getInt("id"), naziv, rs.getInt("naRecept"), rs.getInt("trajanje"), rs.getString("opis"), rs.getString("embalaza"));
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
					PreparedStatement ps = conn.prepareStatement("insert into dopolnilo(naziv , naRecept, trajanje) values (?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
					ps.setString(1, o.getNaziv());
					ps.setInt(2, o.getNaRecept());
					ps.setInt(3, o.getTrajanje());
					ps.setString(4, o.getOpis());
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
			List<Dopolnilo> ret = new ArrayList<Dopolnilo>();
			Connection conn=null;
			try {
				conn=ds.getConnection();

				ResultSet rs=conn.createStatement().executeQuery("select * from dopolnilo");
				while (rs.next()) {
					Dopolnilo o = new Dopolnilo(rs.getString("naziv"), rs.getInt("naRecept"), rs.getInt("trajanje"), rs.getString("opis"), rs.getString("embalaza"));
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
		
		
		public List<Dopolnilo> vrniVseBrezRecepta() throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			List<Dopolnilo> ret = new ArrayList<Dopolnilo>();
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from dopolnilo WHERE naRecept=?",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, 0);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					Dopolnilo o = new Dopolnilo(rs.getInt("id"), rs.getString("naziv"), 0, rs.getInt("trajanje"), rs.getString("opis"), rs.getString("embalaza"));
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
	 
		
		public List<Integer> poisciId(ArrayList<String> dopolnila) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			List<Integer> ret = new ArrayList<Integer>();
			Connection conn=null;
			for(int i = 0; i<dopolnila.size(); i++) {
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from dopolnilo WHERE naziv=?",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setString(1, dopolnila.get(i));
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					Dopolnilo o = new Dopolnilo(rs.getInt("id"), dopolnila.get(i), rs.getInt("naRecept"), rs.getInt("trajanje"), rs.getString("opis"), rs.getString("embalaza"));
					ret.add(o.getId());
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			}
			return ret;
		}
		
		
		public static List<Dopolnilo> pretvori(ArrayList<String> dopolnila) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			List<Dopolnilo> ret = new ArrayList<Dopolnilo>();
			Connection conn=null;
			for(int i = 0; i<dopolnila.size(); i++) {
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from dopolnilo WHERE naziv=?",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setString(1, dopolnila.get(i));
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					Dopolnilo o = new Dopolnilo(rs.getInt("id"), dopolnila.get(i), rs.getInt("naRecept"), rs.getInt("trajanje"), rs.getString("opis"), rs.getString("embalaza"));
					ret.add(o);
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			}
			return ret;
		}
	 
}