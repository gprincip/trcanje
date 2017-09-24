package glavni.model;

import java.sql.Date;
import java.util.List;

import javax.print.attribute.standard.DateTimeAtCompleted;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.Attributes;

import pt.karambola.gpx.beans.Point;
import pt.karambola.gpx.beans.TrackPoint;
import pt.karambola.gpx.beans.TrackSegment;

public class GpxRezultat {

	private int id;
	private String username;
	private List<TrackSegment> trackSegments;

	public GpxRezultat(int id, String username, List<TrackSegment> trackSegments) {
		super();
		this.id = id;
		this.username = username;
		this.trackSegments = trackSegments;
	}

	public GpxRezultat() {

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

	public List<TrackSegment> getTrackPoints() {
		return trackSegments;
	}

	public void setTrackSegments(List<TrackSegment> trackPoints) {
		this.trackSegments = trackPoints;
	}

	public Date vremePocetka() {

		return (Date) trackSegments.get(0).getTrackPoints().get(0).getTime();

	}

	public double distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {

		final int R = 6371; // Radius of the earth

		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c * 1000; // convert to meters

		double height = el1 - el2;

		distance = Math.pow(distance, 2) + Math.pow(height, 2);

		return Math.sqrt(distance);
	}

	public double getUkupnaRazdaljina() { //u metrima

		double razdaljina = 0;

		for (int i = 0; i < trackSegments.size(); i++) {

			List<TrackPoint> trackPointsPomocni = trackSegments.get(i).getTrackPoints();

			for (int j = 1; j < trackPointsPomocni.size(); j++) {

				double lat1 = trackPointsPomocni.get(j - 1).getLatitude();
				double lat2 = trackPointsPomocni.get(j).getLatitude();

				double lon1 = trackPointsPomocni.get(j - 1).getLongitude();
				double lon2 = trackPointsPomocni.get(j).getLongitude();

				double el1 = trackPointsPomocni.get(j - 1).getElevation();
				double el2 = trackPointsPomocni.get(j).getElevation();

				razdaljina += distance(lat1, lat2, lon1, lon2, el1, el2);

			}

		}

		return razdaljina;

	}

	public double getUkupnoVreme() {

		double ukupnoVreme = 0;

		for (int i = 0; i < trackSegments.size(); i++) {

			List<TrackPoint> trackPointsPomocni = trackSegments.get(i).getTrackPoints();

			for (int j = 1; j < trackPointsPomocni.size(); j++) {

				java.util.Date dat1 = trackPointsPomocni.get(j-1).getTime();
				java.util.Date dat2 = trackPointsPomocni.get(j).getTime();

				long razlika = dat2.getTime() - dat1.getTime();//milisekunde
				razlika /= 1000;//sekunde
				ukupnoVreme += razlika;
			}

		}

		return ukupnoVreme;

	}

	public MinutaPoKilometru getProsecanTempo() {

		MinutaPoKilometru minkm = new MinutaPoKilometru();

		double min =  60 / ((getUkupnaRazdaljina()/1000) / (getUkupnoVreme()/60/60));
		
		double ceoDeo = Math.floor(min);
		double decimalniDeo = min - ceoDeo;
		minkm.setMinut((int)ceoDeo);
		minkm.setSekunda((int)Math.round(60 * decimalniDeo));
		return minkm;
		
		
	}

}