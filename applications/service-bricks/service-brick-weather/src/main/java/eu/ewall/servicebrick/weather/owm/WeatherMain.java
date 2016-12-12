package eu.ewall.servicebrick.weather.owm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherMain  implements Serializable {
	
	float temp;
	int humidity;
	float temp_min;
	float temp_max;
	int pressure;
	int sea_level;
	int grnd_level;
	
	public float getTemp() {
		return temp;
	}
	public void setTemp(float temp) {
		this.temp = temp;
	}
	public int getHumidity() {
		return humidity;
	}
	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}
	public float getTemp_min() {
		return temp_min;
	}
	public void setTemp_min(float temp_min) {
		this.temp_min = temp_min;
	}
	public float getTemp_max() {
		return temp_max;
	}
	public void setTemp_max(float temp_max) {
		this.temp_max = temp_max;
	}
	public int getPressure() {
		return pressure;
	}
	public void setPressure(int pressure) {
		this.pressure = pressure;
	}
	public int getSea_level() {
		return sea_level;
	}
	public void setSea_level(int sea_level) {
		this.sea_level = sea_level;
	}
	public int getGrnd_level() {
		return grnd_level;
	}
	public void setGrnd_level(int grnd_level) {
		this.grnd_level = grnd_level;
	}
	
}
