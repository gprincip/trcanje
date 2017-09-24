package glavni.model;

public class AktivnostKorisnik {

	private Aktivnost aktivnost;
	private Korisnik korisnik;
	private int brojLajkova;
	
	public AktivnostKorisnik(){}
	
	public AktivnostKorisnik(Aktivnost aktivnost, Korisnik korisnik, int brojLajkova) {
		super();
		this.aktivnost = aktivnost;
		this.korisnik = korisnik;
		this.brojLajkova = brojLajkova;
	}
	public Aktivnost getAktivnost() {
		return aktivnost;
	}
	public void setAktivnost(Aktivnost aktivnost) {
		this.aktivnost = aktivnost;
	}
	public Korisnik getKorisnik() {
		return korisnik;
	}
	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}	
	public int getBrojLajkova(){
		return brojLajkova;
	}
	public void setBrojLajkova(int brojLajkova){
		this.brojLajkova = brojLajkova;
	}
	
}
