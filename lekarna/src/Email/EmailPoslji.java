package Email;

public class EmailPoslji {
public void poslji(String kdo, String email, String ime, String priimek) {
	String besedilo = "Pozdravljeni "+ime+" "+priimek+", lekarnikar '"+kdo+"' vam je izdal predpis";
	Email.sendMail(email, besedilo);
	System.out.println("mail je bil poslan");
}
}