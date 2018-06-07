package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import vao.Kombinacije;



public class KombinacijeDAO {

	
	 private KombinacijeDAO() {
			try {
				//DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/fitnes");	
				kreirajKombinacije();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	 
		private static KombinacijeDAO inst=new KombinacijeDAO();
		
		public static KombinacijeDAO getInstance() {
			return inst;
		}
	 
	 
		public void kreirajKombinacije() throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			Connection conn=null;
			try {
				conn=ds.getConnection();
				conn.createStatement().execute("create table if not exists kombinacije (id int auto_increment not null,prvo_zdravilo varchar(255) not null, drugo_zdravilo varchar(255) not null, efekt varchar(255) not null,primary key (id))");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
		}
		
		
		public Kombinacije najdiKombinacije(int id) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			System.out.println("DAO: išèem "+id);
			Kombinacije ret = null;
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from Kombinacije where id=?",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ret = new Kombinacije(id, rs.getString("prvo_zdravilo"), rs.getString("prvo_zdravilo"), rs.getString("efekt"));
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return ret;
		}
		
		
		public Kombinacije najdiKombinacije(String prvo_zdravilo, String drugo_zdravilo) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			System.out.println("DAO: išèem "+prvo_zdravilo+", "+drugo_zdravilo);
			Kombinacije ret = null;
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from Kombinacije where prvo_zdravilo=? and drugo_zdravilo=?",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setString(1, prvo_zdravilo);
				ps.setString(2, drugo_zdravilo);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ret = new Kombinacije(rs.getInt("id"), prvo_zdravilo, drugo_zdravilo, rs.getString("efekt"));
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return ret;
		}
		
		
		public void shraniKombinacije(Kombinacije o) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			System.out.println("DAO: shranjujem kombinacijo "+o);
			Connection conn=null;
			try {
				conn=ds.getConnection();
				if(o==null) return;
					PreparedStatement ps = conn.prepareStatement("insert into Kombinacije(prvo_zdravilo , drugo_zdravilo, efekt) values (?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
					ps.setString(1, o.getprvo_zdravilo());
					ps.setString(2, o.getdrugo_zdravilo());
					ps.setString(3, o.getEfekt());
					
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
		
		
		public List<Kombinacije> vrniVse() throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			System.out.println(("DAO: vraèam vse kombinacije"));
			List<Kombinacije> ret = new ArrayList<Kombinacije>();
			Connection conn=null;
			try {
				conn=ds.getConnection();

				ResultSet rs=conn.createStatement().executeQuery("select * from Kombinacije");
				while (rs.next()) {
					Kombinacije o = new Kombinacije(rs.getString("prvo_zdravilo"), rs.getString("drugo_zdravilo"), rs.getString("efekt"));
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
		
		
}