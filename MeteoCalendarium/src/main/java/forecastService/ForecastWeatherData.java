/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forecastService;

import org.json.JSONObject;

/**
 * @author home */
public class ForecastWeatherData extends LocalizedWeatherData {
	static private final String JSON_DT = "dt";

	private long calcDateTime = Long.MIN_VALUE;
        
	
	/**
	 * @param json
	 * @throws JSONException */
	public ForecastWeatherData (JSONObject json) {
		super (json);
		this.calcDateTime = (json.optLong (ForecastWeatherData.JSON_DT, Long.MIN_VALUE)*1000);
	}

	public long getCalcDateTime () {
		return this.calcDateTime;
	}
        
}