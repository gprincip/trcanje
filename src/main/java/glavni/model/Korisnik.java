package glavni.model;

public class Korisnik {

	private String email;
	private String username;
	private String password;
	private String ime;
	private String prezime;
	private String slika;
	private int enabled;
	
	public Korisnik(){}
	
	public Korisnik(String email, String username, String password, String ime, String prezime, String slika, int enabled) {
		super();
		this.username = username;
		this.password = password;
		this.ime = ime;
		this.prezime = prezime;
		this.slika = slika;
		this.enabled = enabled;
	}

	public String getEmail(){
		return email;
	}
	
	public void setEmail(String username){
		this.email = email;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public void setSlika(String nazivSlike){
		this.slika = nazivSlike;
	}
	
	public String getSlika(){
		return slika;
	}
	
	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	
}
