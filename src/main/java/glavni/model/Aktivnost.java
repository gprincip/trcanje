package glavni.model;

public class Aktivnost {

	private int id;
	private String username;
	private String datum;
	private String datumFormatiran;
	private String tempo;
	private double distanca;
	private String naslov;
	private String trajanje;
	
	public Aktivnost(){}

	public Aktivnost(int id, String username, String datum, String datumFormatiran, String tempo, double distanca, String naslov, String trajanje) {
		super();
		this.id = id;
		this.username = username;
		this.datum = datum;
		this.datumFormatiran = datumFormatiran;
		this.tempo = tempo;
		this.distanca = distanca;
		this.naslov = naslov;
		this.trajanje = trajanje;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDatum() {
		return datum;
	}

	public String getDatumFormatiran() {
		return datumFormatiran;
	}
	
	public void setDatum(String datum) {
		this.datum = datum;
	}

	public void setDatumFormatiran(String datumFormatiran) {
		this.datumFormatiran = datumFormatiran;
	}
	
	public String getTempo() {
		return tempo;
	}

	public void setTempo(String tempo) {
		this.tempo = tempo;
	}

	public double getDistanca() {
		return distanca;
	}

	public void setDistanca(double distanca) {
		this.distanca = distanca;
	}

	public String getNaslov(){
		return this.naslov;
	}
	
	public String getTrajanje(){
		return this.trajanje;
	}
	
	public void setTrajanje(String trajanje){
		this.trajanje = trajanje;
	}
	
}
