package glavni.model;

public class MinutaPoKilometru {

	private int minut;
	private int sekunda;
	
	public MinutaPoKilometru(){}
	
	public MinutaPoKilometru(int minut, int sekunda) {
		super();
		this.minut = minut;
		this.sekunda = sekunda;
	}
	
	public int getMinut() {
		return minut;
	}
	public void setMinut(int minut) {
		this.minut = minut;
	}
	public int getSekunda() {
		return sekunda;
	}
	public void setSekunda(int sekunda) {
		this.sekunda = sekunda;
	}
	
	@Override
	public String toString() {
		
		return (minut+":"+sekunda+"/km");		
	}
	
}
