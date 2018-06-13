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
				conn.createStatement().execute("create table if not exists kombinacije (id int auto_increment not null,prvo_zdravilo varchar(255) not null, drugo_zdravilo varchar(255) not null, efekt varchar(255),primary key (id))");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
		}
		
		
		public boolean najdiKombinacije(int id1, int id2) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			Kombinacije ret = null;
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from Kombinacije where prvo_zdravilo=? and drugo_zdravilo=?",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, id1);
				ps.setInt(2, id2);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ret = new Kombinacije(rs.getInt("id"), id1, id2, rs.getString("efekt"));
					break;
				}
				
				if(ret==null) {
				PreparedStatement ps2 = conn.prepareStatement("select * from Kombinacije where drugo_zdravilo=? and prvo_zdravilo=?",PreparedStatement.RETURN_GENERATED_KEYS);
				ps2.setInt(1, id1);
				ps2.setInt(2, id2);
				ResultSet rs2 = ps2.executeQuery();
				while (rs2.next()) {
					ret = new Kombinacije(rs2.getInt("id"), id1, id2, rs2.getString("efekt"));
					break;
				}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			if(ret==null)
				return false;
			else
				return true;
		}
		

		
		
		public void shraniKombinacije(Kombinacije o) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			System.out.println("DAO: shranjujem kombinacijo "+o);
			Connection conn=null;
			try {
				conn=ds.getConnection();
				if(o==null) return;
					PreparedStatement ps = conn.prepareStatement("insert into Kombinacije(prvo_zdravilo , drugo_zdravilo, efekt) values (?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
					ps.setInt(1, o.getprvo_zdravilo());
					ps.setInt(2, o.getdrugo_zdravilo());
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
					Kombinacije o = new Kombinacije(rs.getInt("prvo_zdravilo"), rs.getInt("drugo_zdravilo"), rs.getString("efekt"));
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