

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Zapis_dopolniloDAO {
	private Zapis_dopolniloDAO() {
		try {
			// DataSource ds=(DataSource)new
			// InitialContext().lookup("java:jboss/datasources/fitnes");
			kreirajZapis_dopolnilo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Zapis_dopolniloDAO inst = new Zapis_dopolniloDAO();

	public static Zapis_dopolniloDAO getInstance() {
		return inst;
	}

	public void kreirajZapis_dopolnilo() throws Exception {
		DataSource ds = (DataSource) new InitialContext().lookup("java:jboss/datasources/lekarna");
		Connection conn = null;
		try {
			conn = ds.getConnection();
			conn.createStatement().execute(
					"create table zapis_dopolnilo (id int auto_increment, dopolnilo_id int, zapis_id int, kolicina int, primary key (id))");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

	public Zapis_dopolnilo najdiZapis_dopolnilo(int id) throws Exception {
		DataSource ds = (DataSource) new InitialContext().lookup("java:jboss/datasources/lekarna");
		System.out.println("DAO: išèem " + id);
		Zapis_dopolnilo ret = null;
		Connection conn = null;
		try {
			conn = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from zapis_dopolnilo where id=?",
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ret = new Zapis_dopolnilo(id, rs.getInt("dopolnilo_id"), rs.getInt("zapis_id"), rs.getInt("kolicina"));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return ret;
	}

	public Zapis_dopolnilo najdiDoloceno(int dopolnilo, int zapis) throws Exception {
		DataSource ds = (DataSource) new InitialContext().lookup("java:jboss/datasources/lekarna");
		System.out.println("DAO: išèem " + zapis + ", " + dopolnilo);
		Zapis_dopolnilo ret = null;
		Connection conn = null;
		try {
			conn = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from zapis_dopolnilo where dopolnilo_id=? AND zapis_id=?",
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, dopolnilo);
			ps.setInt(2, zapis);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ret = new Zapis_dopolnilo(rs.getInt("id"), dopolnilo, zapis, rs.getInt("kolicina"));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return ret;
	}

	public void shraniZapis_dopolnilo(Zapis_dopolnilo o) throws Exception {
		DataSource ds = (DataSource) new InitialContext().lookup("java:jboss/datasources/lekarna");
		System.out.println("DAO: shranjujem zapis_dopolnilo " + o);
		Connection conn = null;
		try {
			conn = ds.getConnection();
			if (o == null)
				return;
			PreparedStatement ps = conn.prepareStatement(
					"insert into zapis_dopolnilo(dopolnilo_id , zapis_id, kolicina) values (?,?,?)",
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, o.getDopolnilo_id());
			ps.setInt(2, o.getZapis_id());
			ps.setInt(3, o.getKolicina());
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

	public List<Zapis_dopolnilo> vrniVse() throws Exception {
		DataSource ds = (DataSource) new InitialContext().lookup("java:jboss/datasources/lekarna");
		System.out.println(("DAO: vraèam vse èlane"));
		List<Zapis_dopolnilo> ret = new ArrayList<Zapis_dopolnilo>();
		Connection conn = null;
		try {
			conn = ds.getConnection();

			ResultSet rs = conn.createStatement().executeQuery("select * from zapis_dopolnilo");
			while (rs.next()) {
				Zapis_dopolnilo o = new Zapis_dopolnilo(rs.getInt("dopolnilo_id"), rs.getInt("zapis_id"),
						rs.getInt("kolicina"));
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