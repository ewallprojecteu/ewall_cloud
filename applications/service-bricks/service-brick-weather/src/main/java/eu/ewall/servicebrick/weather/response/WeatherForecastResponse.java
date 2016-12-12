package eu.ewall.servicebrick.weather.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import eu.ewall.servicebrick.weather.owm.WeatherCity;
import eu.ewall.servicebrick.weather.owm.WeatherForecastItem;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class WeatherForecastResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long dt;
	int cod;
	int cnt;
	
	WeatherCity city;

	List<WeatherForecastItem> list;

	public long getDt() {
		return dt;
	}

	public void setDt(long dt) {
		this.dt = dt;
	}

	public int getCod() {
		return cod;
	}

	public void setCod(int cod) {
		this.cod = cod;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public WeatherCity getCity() {
		return city;
	}

	public void setCity(WeatherCity city) {
		this.city = city;
	}

	public List<WeatherForecastItem> getList() {
		return list;
	}

	public void setList(List<WeatherForecastItem> list) {
		this.list = list;
	}
	
}
