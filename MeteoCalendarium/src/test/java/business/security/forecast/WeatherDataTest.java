/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.security.forecast;

import business.security.forecast.StatusWeatherData;
import business.security.forecast.WeatherData;
import business.security.forecast.LocalizedWeatherData.GeoCoord;
import business.security.forecast.WeatherData.Clouds.CloudDescription;
import business.security.forecast.WeatherData.WeatherCondition;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;


import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

/**
 * @author mtavares */
public class WeatherDataTest {
	@Test
	public void testWeatherDataParsing_Point () throws JSONException {
		JSONObject weatherDatajson = new JSONObject (TestData.CURRENT_WEATHER_POINT);
		StatusWeatherData weather = new StatusWeatherData (weatherDatajson);
		
		assertNotNull (weather);
		assertEquals (5106529, weather.getId ());
		assertEquals ("Woodbridge", weather.getName ());
		assertEquals (1359818400, weather.getDateTime ());

		assertTrue (weather.hasCoord ());
		GeoCoord coord = weather.getCoord ();
		assertNotNull (coord);
		assertEquals (-74.284592f, coord.getLongitude (), 0.000001f);
		assertEquals (40.557598, coord.getLatitude (), 0.000001f);

		WeatherData.Main mainWeather = weather.getMain ();
		assertNotNull (mainWeather);
		assertTrue (mainWeather.hasTemp ());
		assertEquals (267.97f, mainWeather.getTemp (), 0.001f);
		assertTrue (mainWeather.hasTempMin ());
		assertEquals (267.15f, mainWeather.getTempMin (), 0.001f);
		assertTrue (mainWeather.hasTempMax ());
		assertEquals (269.15f, mainWeather.getTempMax (), 0.001f);
		assertTrue (mainWeather.hasPressure ());
		assertEquals (1026f, mainWeather.getPressure (), 0.1f);
		assertTrue (mainWeather.hasHumidity ());
		assertEquals (45f, mainWeather.getHumidity (), 0.1f);

		assertTrue (weather.hasWind ());
		WeatherData.Wind wind = weather.getWind ();
		assertNotNull (wind);
		assertTrue (wind.hasSpeed ());
		assertEquals (3.1f, wind.getSpeed (), 0.01f);
		assertTrue (wind.hasDeg ());
		assertEquals (260, wind.getDeg ());
		assertFalse (wind.hasGust ());
		assertFalse (wind.hasVarBeg ());
		assertFalse (wind.hasVarEnd ());

		assertTrue (weather.hasClouds ());
		WeatherData.Clouds clouds = weather.getClouds ();
		assertNotNull (clouds);
		assertEquals (40, clouds.getAll ());
		assertFalse (clouds.hasMeasures ());

		assertFalse (weather.hasRain ());
		assertFalse (weather.hasSnow ());

		assertTrue (weather.hasWeatherConditions ());
		List<WeatherCondition> weatherConditions = weather.getWeatherConditions ();
		assertNotNull (weatherConditions);
		assertEquals (1, weatherConditions.size ());
		WeatherCondition condition = weatherConditions.get (0);
		assertNotNull (condition);
		assertEquals (802, condition.getCode ().getId ());
		assertEquals (WeatherCondition.ConditionCode.SCATTERED_CLOUDS, condition.getCode ());
		assertEquals ("Clouds", condition.getMain ());
		assertEquals ("scattered clouds", condition.getDescription ());
		assertEquals ("03d", condition.getIconName ());
	}

	@Test
	public void testWeatherDataParsing_City () throws JSONException {
		JSONObject weatherDatajson = new JSONObject (TestData.CURRENT_WEATHER_CITY);
		StatusWeatherData weather = new StatusWeatherData (weatherDatajson);

		assertTrue (weather.hasClouds ());
		WeatherData.Clouds clouds = weather.getClouds ();
		assertNotNull (clouds);
		assertFalse (clouds.hasAll ());
		assertTrue (clouds.hasConditions ());
		List<CloudDescription> conditions = clouds.getConditions ();
		assertNotNull (conditions);
		assertEquals (2, conditions.size ());
		CloudDescription cloudDescription = conditions.get (0);
		assertNotNull (cloudDescription);
		assertTrue (cloudDescription.hasDistance ());
		assertTrue (cloudDescription.hasSkyCondition ());
		assertFalse (cloudDescription.hasCumulus ());
		assertEquals (91, cloudDescription.getDistance ());
		assertEquals (CloudDescription.SkyCondition.BKN, cloudDescription.getSkyCondition ());

		cloudDescription = conditions.get (1);
		assertNotNull (cloudDescription);
		assertTrue (cloudDescription.hasDistance ());
		assertTrue (cloudDescription.hasSkyCondition ());
		assertTrue (cloudDescription.hasCumulus ());
		assertEquals (305, cloudDescription.getDistance ());
		assertEquals (CloudDescription.SkyCondition.OVC, cloudDescription.getSkyCondition ());
		assertEquals (CloudDescription.Cumulus.CB, cloudDescription.getCumulus ());
	}

	// TODO test with invalid data.
}