package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
				conn.createStatement().execute("create table if not exists  kartoteka (id int auto_increment, ime varchar(255), priimek varchar(255), email varchar(255) not null, datumRojstva date, spol integer, primary key (id))");
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
					Date datumRojstva = rs.getDate("datumRojstva");
					Calendar cal = Calendar.getInstance();
					cal.setTime(datumRojstva);
					int letoRojstva = cal.get(Calendar.YEAR);
					int trenutnoLeto = Calendar.getInstance().get(Calendar.YEAR);
					int starost = trenutnoLeto - letoRojstva;
					ret = new Kartoteka(id, rs.getString("ime"), rs.getString("priimek"), rs.getString("email"), starost,  rs.getInt("spol"));
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			return ret;
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
					Date datumRojstva = rs.getDate("datumRojstva");
					Calendar cal = Calendar.getInstance();
					cal.setTime(datumRojstva);
					int letoRojstva = cal.get(Calendar.YEAR);
					int trenutnoLeto = Calendar.getInstance().get(Calendar.YEAR);
					int starost = trenutnoLeto - letoRojstva;
					Kartoteka o = new Kartoteka(rs.getString("ime"), rs.getString("priimek"), rs.getString("email"), starost,  rs.getInt("spol"));
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