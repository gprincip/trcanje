package glavni.model;

import java.util.ArrayList;
import java.util.List;

import pt.karambola.geo.Geo;
import pt.karambola.gpx.beans.Gpx;
import pt.karambola.gpx.beans.Point;
import pt.karambola.gpx.beans.Route;
import pt.karambola.gpx.beans.RoutePoint;
import pt.karambola.gpx.beans.Track;
import pt.karambola.gpx.beans.TrackPoint;
import pt.karambola.gpx.beans.TrackSegment;
import pt.karambola.gpx.io.GpxFileIo;

public class GpxReader {

	public static void main(String args[]) {

		Gpx gpx = GpxFileIo.parseIn("C:\\Users\\Gavra\\Desktop\\RK_gpx _2017-07-25_2215.gpx");
		List<Track> tracks = gpx.getTracks();

		List<TrackSegment> segments = tracks.get(0).getTrackSegments();
		List<TrackPoint> trackPoints = segments.get(0).getTrackPoints();
		/*for (int i = 0; i < trackPoints.size(); i++) {
			System.out.println(trackPoints.get(i));
		}*/

		GpxRezultat gpxRez = new GpxRezultat(0, "sda", segments);
		
		System.out.println(gpxRez.getUkupnoVreme());

		
	}

}
