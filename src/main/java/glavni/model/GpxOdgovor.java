package glavni.model;

public class GpxOdgovor {

	private String username;
	private String ime;
	private String prezime;
	private String slika;
	private double distanca;
	private double vreme;
	private String tempo;
	
	public GpxOdgovor(){}
	
	public GpxOdgovor(String username, String ime, String prezime, String slika, double distanca, double vreme,
			String tempo) {
		super();
		this.username = username;
		this.ime = ime;
		this.prezime = prezime;
		this.slika = slika;
		this.distanca = distanca;
		this.vreme = vreme;
		this.tempo = tempo;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public String getSlika() {
		return slika;
	}
	public void setSlika(String slika) {
		this.slika = slika;
	}
	public double getDistanca() {
		return distanca;
	}
	public void setDistanca(double distanca) {
		this.distanca = distanca;
	}
	public double getVreme() {
		return vreme;
	}
	public void setVreme(double vreme) {
		this.vreme = vreme;
	}
	public String getTempo() {
		return tempo;
	}
	public void setTempo(String tempo) {
		this.tempo = tempo;
	}
	
	
	
}
