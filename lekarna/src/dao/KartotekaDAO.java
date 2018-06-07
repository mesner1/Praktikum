package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import vao.Kartoteka;

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
				conn.createStatement().execute("create table if not exists  kartoteka (id int auto_increment, ime varchar(255), priimek varchar(255), email varchar(255) not null, primary key (id))");
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
					ret = new Kartoteka(id, rs.getString("ime"), rs.getString("priimek"), rs.getString("email"));
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
					Kartoteka o = new Kartoteka(rs.getString("ime"), rs.getString("priimek"), rs.getString("email"));
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