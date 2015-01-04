/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Forecast;

import managerBeans.OwmClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author home */
public class WeatherHistoryStationResponse extends AbstractOwmResponse {
	static private final String JSON_CALCTIME_TICK = "tick";
	static private final String JSON_STATION_ID    = "station_id";
	static private final String JSON_TYPE          = "type";

	private final float calctimeTick;
	private final int stationId;
	private final OwmClient.HistoryType type;
	private final List<AbstractWeatherData> history;

	/**
	 * @param json */
	public WeatherHistoryStationResponse (JSONObject json) {
		super (json);

		this.stationId = json.optInt (WeatherHistoryStationResponse.JSON_STATION_ID, Integer.MIN_VALUE);

		OwmClient.HistoryType typeValue = null;
		String typeStr = json.optString (WeatherHistoryStationResponse.JSON_TYPE);
		if (typeStr != null && typeStr.length () > 0) {
			try {
				typeValue = OwmClient.HistoryType.valueOf (typeStr.trim ().toUpperCase ());
			} catch (IllegalArgumentException e) {
				typeValue = OwmClient.HistoryType.UNKNOWN;
			}
		}
		this.type = typeValue;

		if (this.type == OwmClient.HistoryType.TICK) {
			String calcTimeStr = json.optString (AbstractOwmResponse.JSON_CALCTIME);
			this.calctimeTick = AbstractOwmResponse.getValueFromCalcTimeStr (calcTimeStr, WeatherHistoryStationResponse.JSON_CALCTIME_TICK);
		} else {
			this.calctimeTick = Float.NaN;
		}

		JSONArray jsonHistory = json.optJSONArray (AbstractOwmResponse.JSON_LIST);
		if (jsonHistory == null) {
			this.history = Collections.emptyList ();
		} else {
			this.history = new ArrayList<AbstractWeatherData> (jsonHistory.length ());
			switch (this.type) {
				case TICK:
					for (int i = 0; i <jsonHistory.length (); i++) {
						JSONObject jsonBaseWeatherData = jsonHistory.optJSONObject (i);
						if (jsonBaseWeatherData != null) {
							this.history.add (new WeatherData (jsonBaseWeatherData));
						}
					}
					break;
				case HOUR:
				case DAY:
					for (int i = 0; i <jsonHistory.length (); i++) {
						JSONObject jsonBaseWeatherData = jsonHistory.optJSONObject (i);
						if (jsonBaseWeatherData != null) {
							this.history.add (new SampledWeatherData (jsonBaseWeatherData));
						}
					}
					break;
				default:
					break;
			}
		}

	}

	public boolean hasCalcTimeTick () {
		return !Double.isNaN (this.calctimeTick);
	}
	public double getCalcTimeTick () {
		return this.calctimeTick;
	}

	public boolean hasStationId () {
		return this.stationId != Integer.MIN_VALUE;
	}
	public int getStationId () {
		return this.stationId;
	}

	public boolean hasType () {
		return this.type != null;
	}
	public OwmClient.HistoryType getType () {
		return this.type;
	}

	public boolean hasHistory () {
		return this.history != null && !this.history.isEmpty ();
	}
	public List<AbstractWeatherData> getHistory () {
		return this.history;
	}
}