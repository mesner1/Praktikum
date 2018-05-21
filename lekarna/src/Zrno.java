



import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


@ManagedBean(name="zrno")
@SessionScoped
public class Zrno {
	
	Oseba o=new Oseba();
	String izbranasifra;
	String naziv;
	DAO dao=new DAO();
	String alarm="";
	
public String login() throws Exception {
	
	o=dao.najdi(izbranasifra);
	
	if(o==null) {
	System.out.println(izbranasifra);
	alarm="Vnesli ste narobe";
	return "failure";}
	
	System.out.println("objekt obstaja");
	
	alarm="";
	return "success";
	
	
}



public Oseba getO() {
	return o;
}


public void setO(Oseba o) {
	this.o = o;
}


public String getIzbranasifra() {
	return izbranasifra;
}


public void setIzbranasifra(String izbranasifra) {
	this.izbranasifra = izbranasifra;
}


public String getNaziv() {
	return naziv;
}


public void setNaziv(String naziv) {
	this.naziv = naziv;
}


public DAO getDao() {
	return dao;
}


public void setDao(DAO dao) {
	this.dao = dao;
}

public String getAlarm() {
	return alarm;
}

public void setAlarm(String alarm) {
	this.alarm = alarm;
}



	

}
