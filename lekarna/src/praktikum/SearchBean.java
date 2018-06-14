package praktikum;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.bean.ManagedBean;

import dao.DopolniloDAO;
import dao.KartotekaDAO;
import vao.Dopolnilo;
import vao.Kartoteka;

@ManagedBean(name = "searchBean")
public class SearchBean {
	private ArrayList<Kartoteka> kartoteke = new ArrayList<Kartoteka>();
	private ArrayList<Dopolnilo> dopolnilo = new ArrayList<Dopolnilo>();
	private ArrayList<String> izbranaKartoteka = new ArrayList<String>();
	private String countryName;	

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	private String country;
	
	public String getCountry() {
		return (country);
	}
 
	public void setCountry(String country) {
		this.country = country;
	}
	// Method To Display The Country List On The JSF Page
	
	public List<String> autoComplete(String countryPrefix) {  		
		
	
		
		try {
			
			
			kartoteke = (ArrayList<Kartoteka>) KartotekaDAO.getInstance().vrniVse();
			String [] k = new String[kartoteke.size()];
			System.out.println(kartoteke);
			  for (int i=0; i<kartoteke.size();i++) {
				  Object convert = kartoteke.get(i);	
				  String con = new String(convert.toString());
				  k[i] = con;
				  System.out.println(k);
				  
		           
		        }
			
			  
			  
			for (String country : k) {
					if (country.toUpperCase().startsWith(countryPrefix.toUpperCase())) {
						izbranaKartoteka.add(country);
					}
				}	  
			  
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return izbranaKartoteka;  
	}
	
	
public List<String> autoCompleteZdravilo(String countryPrefix) {  		
		
	
		
		try {
			
			
			dopolnilo = (ArrayList<Dopolnilo>) DopolniloDAO.getInstance().vrniVse();
			String [] k = new String[dopolnilo.size()];
			System.out.println(dopolnilo);
			  for (int i=0; i<dopolnilo.size();i++) {
				  Object convert = dopolnilo.get(i);	
				  String con = new String(convert.toString());
				  k[i] = con;
				  System.out.println(k);
				  
		           
		        }
			
			  
			  
			for (String country : k) {
					if (country.toUpperCase().startsWith(countryPrefix.toUpperCase())) {
						izbranaKartoteka.add(country);
					}
				}	  
			  
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return izbranaKartoteka;  
	}
	
	
	
}