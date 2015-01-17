/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forecastService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author home */
public class WeatherHistoryCityResponse extends AbstractOwmResponse {
	static private final String JSON_CALCTIME_FETCH = "fetch";
	static private final String JSON_CALCTIME_FIND  = "find";
	static private final String JSON_CITY_ID        = "city_id";

	private final double calctimeFind;
	private final double calctimeFetch;
	private final int cityId;
	private final List<WeatherData> history;

	/**
	 * @param json */
	public WeatherHistoryCityResponse (JSONObject json) {
		super (json);

		String calcTimeStr = json.optString (AbstractOwmResponse.JSON_CALCTIME);
		this.calctimeFind = AbstractOwmResponse.getValueFromCalcTimeStr (calcTimeStr, WeatherHistoryCityResponse.JSON_CALCTIME_FIND);
		this.calctimeFetch = AbstractOwmResponse.getValueFromCalcTimeStr (calcTimeStr, WeatherHistoryCityResponse.JSON_CALCTIME_FETCH);
		this.cityId = json.optInt (WeatherHistoryCityResponse.JSON_CITY_ID, Integer.MIN_VALUE);
		
		JSONArray jsonHistory = json.optJSONArray (AbstractOwmResponse.JSON_LIST);
		if (jsonHistory == null) {
			this.history = Collections.emptyList ();
		} else {
			this.history = new ArrayList<WeatherData> (jsonHistory.length ());
			for (int i = 0; i <jsonHistory.length (); i++) {
				JSONObject jsonBaseWeatherData = jsonHistory.optJSONObject (i);
				if (jsonBaseWeatherData != null) {
					this.history.add (new WeatherData (jsonBaseWeatherData));
				}
			}
		}

	}

	public boolean hasCalcTimeFetch () {
		return !Double.isNaN (this.calctimeFetch);
	}
	public double getCalcTimeFetch () {
		return this.calctimeFetch;
	}

	public boolean hasCalcTimeFind () {
		return !Double.isNaN (this.calctimeFind);
	}
	public double getCalcTimeFind () {
		return this.calctimeFind;
	}

	public boolean hasCityId () {
		return this.cityId != Integer.MIN_VALUE;
	}
	public int getCityId () {
		return this.cityId;
	}

	public boolean hasHistory () {
		return this.history != null && !this.history.isEmpty ();
	}
	public List<WeatherData> getHistory () {
		return this.history;
	}
}