/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.security.forecast;


import org.json.JSONObject;

public class StatusWeatherData extends LocalizedWeatherData {
	private static final String JSON_ID      = "id";
	private static final String JSON_NAME    = "name";
	private static final String JSON_STATION = "station";

	static public class Station {
		private static final String JSON_ZOOM = "zoom";

		private final int zoom;

		public Station (JSONObject json) {
			this.zoom = json.optInt (Station.JSON_ZOOM, Integer.MIN_VALUE);
		}

		public boolean hasZoom () {
			return this.zoom != Integer.MIN_VALUE;
		}
		public int getZoom () {
			return this.zoom;
		}
	}
	private final long id;
	private final String name;
	private final Station station;

	public StatusWeatherData (JSONObject json) {
		super (json);
		this.id = json.optLong (StatusWeatherData.JSON_ID, Long.MIN_VALUE);
		this.name = json.optString (StatusWeatherData.JSON_NAME);
		JSONObject stationJson = json.optJSONObject (StatusWeatherData.JSON_STATION);
		this.station = stationJson != null ? new Station (stationJson) : null;
	}

	public boolean hasId () {
		return this.id != Long.MIN_VALUE;
	}
	public long getId () {
		return this.id;
	}

	public boolean hasName () {
		return this.name != null && this.name.length () > 0;
	}
	public String getName () {
		return this.name;
	}

	public boolean hasStation () {
		return this.station != null;
	}
	public Station getStation () {
		return this.station;
	}
}