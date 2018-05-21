

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.InitialContext;
import javax.sql.DataSource;





public class DAO {

	
	
	private DataSource baza;
	Connection conn=null;
	
    public DAO() {
		try {
			baza=(DataSource)new InitialContext().lookup("java:jboss/datasources/lekarna");	
			kreirajTabele();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	public void kreirajTabele() throws Exception {
		
		
		try {
			conn=baza.getConnection();
			
			conn.createStatement().execute("create table if not exists zdravilo (ime varchar(45))");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}
	

	public Oseba najdi(String sifra) throws Exception {
		Oseba ret = null;
		try {
			conn=baza.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from oseba where sifra=?");
			ps.setString(1, sifra);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ret = new Oseba(rs.getString("ime"), rs.getString("priimek"),rs.getString("naziv"),sifra);
				
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		
		
		
		
		return ret;
	}
	
	
	
	
}