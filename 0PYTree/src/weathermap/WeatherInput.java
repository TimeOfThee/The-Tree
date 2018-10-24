package weathermap;

import main.Observer;
import weather.Weather;

public class WeatherInput {

	public static final boolean active=false;//set this to true when ready
	
	private Observer ob;
	private float wd,rd,sd,wdMax,rdMax,sdMax;//wd=wind density, rd=rain density, sd=snow density
	private String rainModel,snowModel;
	private int timer,often,weather;
	
	
	public WeatherInput(Observer ob,float wdMax,float rdMax,float sdMax,String rainModel,String snowModel,int often) {
		this.ob=ob;
		this.wdMax=wdMax;
		this.wd=0;
		this.rdMax=rdMax;
		this.rd=0;
		this.sdMax=sdMax;
		this.sd=0;
		this.rainModel=rainModel;
		this.snowModel=snowModel;
		this.timer=often;
		this.often=often;
	}
	public void update() {
		if(timer<=0) {
			timer=often;
			//updateWeathermap(f,f,f);
			//updateWeathermap(s);
		}else {
			timer--;
		}
	}
	
	public void updateWeathermap(float wind,float rain,float snow) {
		wd=100*wind/wdMax;
		rd=100*wind/rdMax;
		sd=100*wind/sdMax;
	}
	public void updateWeathermap(String weatherinput) {
		if(weatherinput.equals(snowModel)) {
			ob.getWeather().setState(Weather.snowing);
		}else if(weatherinput.equals(rainModel)) {
			ob.getWeather().setState(Weather.raining);
		}else {
			ob.getWeather().setState(Weather.knot);
		}
	}
	
	public float getMap(int which) {
		switch(which) {
		case Weather.raining:
			return rd;
		case Weather.snowing:
			return sd;
		default:
			return wd;
		}
	}
}
