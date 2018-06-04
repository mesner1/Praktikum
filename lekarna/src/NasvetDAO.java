

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;



public class NasvetDAO {
	private NasvetDAO() {
		try {
			// DataSource ds=(DataSource)new
			// InitialContext().lookup("java:jboss/datasources/fitnes");
			kreirajNasvet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static NasvetDAO inst = new NasvetDAO();

	public static NasvetDAO getInstance() {
		return inst;
	}

	public void kreirajNasvet() throws Exception {
		DataSource ds = (DataSource) new InitialContext().lookup("java:jboss/datasources/lekarna");
		Connection conn = null;
		try {
			conn = ds.getConnection();
			conn.createStatement().execute(
					"create table nasvet (id int auto_increment, nasvet varchar(255) not null, avtor varchar(255) not null, hash varchar(255), zapis_id int, kartoteka_id int not null, primary key (id))");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

	public Nasvet najdiNasvet(int id) throws Exception {
		DataSource ds = (DataSource) new InitialContext().lookup("java:jboss/datasources/lekarna");
		System.out.println("DAO: išèem " + id);
		Nasvet ret = null;
		Connection conn = null;
		try {
			conn = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from nasvet where id=?",
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ret = new Nasvet(id, rs.getString("nasvet"), rs.getString("avtor"), rs.getString("hash"),
						rs.getInt("zapis_id"), rs.getInt("kartoteka_id"));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return ret;
	}

	public int shraniNasvet(Nasvet o) throws Exception {
		DataSource ds = (DataSource) new InitialContext().lookup("java:jboss/datasources/lekarna");
		System.out.println("DAO: shranjujem nasvet " + o);
		Connection conn = null;
		try {
			conn = ds.getConnection();
			PreparedStatement ps;
			if(o.getZapis_id()==0) {
			ps = conn.prepareStatement(
					"insert into nasvet(nasvet, avtor, hash, kartoteka_id) values (?,?,?,?)",
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, o.getNasvet());
			ps.setString(2, o.getAvtor());
			ps.setString(3, o.getHash());
			ps.setInt(4, o.getKartoteka_id());
			ps.executeUpdate();

			}
			else {
			ps = conn.prepareStatement(
					"insert into nasvet(nasvet, avtor, hash, zapis_id, kartoteka_id) values (?,?,?,?,?)",
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, o.getNasvet());
			ps.setString(2, o.getAvtor());
			ps.setString(3, o.getHash());
			ps.setInt(4, o.getZapis_id());
			ps.setInt(5, o.getKartoteka_id());
			ps.executeUpdate();
			}
			ResultSet res = ps.getGeneratedKeys();
			while (res.next())
				o.setId(res.getInt(1));
			res.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		
		return o.getId();
	}

	public List<Nasvet> vrniVse(int id) throws Exception {
		DataSource ds = (DataSource) new InitialContext().lookup("java:jboss/datasources/lekarna");
		System.out.println(("DAO: vraèam vse nasvete"));
		List<Nasvet> ret = new ArrayList<Nasvet>();
		Connection conn = null;
		try {
			conn=ds.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from nasvet WHERE kartoteka_id=?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Nasvet o = new Nasvet(rs.getInt("id"), rs.getString("nasvet"), rs.getString("avtor"), rs.getString("hash"),
						rs.getInt("zapis_id"), id);
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