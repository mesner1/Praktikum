package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import vao.Tip_zapis;



public class Tip_zapisDAO {

	 private Tip_zapisDAO() {
			try {
				//DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/fitnes");	
				kreirajTip();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	 
		private static Tip_zapisDAO inst=new Tip_zapisDAO();
		
		public static Tip_zapisDAO getInstance() {
			return inst;
		}
	 
	 
		public void kreirajTip() throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			Connection conn=null;
			try {
				conn=ds.getConnection();
				conn.createStatement().execute("create table tip_zapis (id int auto_increment, naziv varchar(255), primary key (id))");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
		}
		
		
		public Tip_zapis najdiTip(int id) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			Tip_zapis ret = null;
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from tip_zapis where id=?",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ret = new Tip_zapis(id, rs.getString("naziv"));
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return ret;
		}
		
		
		public Tip_zapis najdiTip(String naziv) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			Tip_zapis ret = null;
			Connection conn=null;
			try {
				conn=ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from tip_zapis where naziv=?",PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setString(1, naziv);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ret = new Tip_zapis(rs.getInt("id"), naziv);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return ret;
		}
		
		
		public void shraniTip(Tip_zapis o) throws Exception {
			DataSource ds=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			System.out.println("DAO: shranjujem kartoteko "+o);
			Connection conn=null;
			try {
				conn=ds.getConnection();
				if(o==null) return;
					PreparedStatement ps = conn.prepareStatement("insert into tip_zapis(naziv) values (?)",PreparedStatement.RETURN_GENERATED_KEYS);
					ps.setString(1, o.getNaziv());
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
}