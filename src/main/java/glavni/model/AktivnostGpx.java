package glavni.model;

public class AktivnostGpx {

	private int id;
	private String username;
	private String naziv_fajla;
	private String naslov; //kako ce da se zove aktivnost na sajtu
	
	public AktivnostGpx(){}
	
	public AktivnostGpx(int id, String username, String naziv_aktivnosti, String naslov) {
		super();
		this.id = id;
		this.username = username;
		this.naziv_fajla = naziv_aktivnosti;
		this.naslov = naslov;
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
	public String getNaziv_aktivnosti() {
		return naziv_fajla;
	}
	public void setNaziv_aktivnosti(String naziv_aktivnosti) {
		this.naziv_fajla = naziv_aktivnosti;
	}
	public String getNaslov(){
		return this.naslov;
	}
	public void setNaslov(String naslov){
		this.naslov = naslov;
	}
	
	
}
