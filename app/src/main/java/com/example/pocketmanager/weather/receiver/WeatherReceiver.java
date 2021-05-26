package com.example.pocketmanager.weather.receiver;


import com.example.pocketmanager.schedule.LocationData;
import com.example.pocketmanager.general.Time;
import com.example.pocketmanager.weather.WeatherData;

public class WeatherReceiver implements Runnable {
    private static WeatherReceiver instance = new WeatherReceiver();

    private static boolean todayWeatherReady;
    private static boolean tomorrowWeatherReady;
    private static boolean dailyWeatherReady;
    private static boolean airPollutionReady;

    @Override
    public void run() {
        receiveWeatherData();
    }

    public static WeatherReceiver getInstance() {
        return instance;
    }

    public static void initCurrentLocationWeatherData() {
        long startDt = Time.getCurrentDt() / 86400 * 86400;
        long dt = 3600;

        if (WeatherData.hourlyWeatherData.isEmpty()) {
            for (int i = 0; i < 48; i++)
                WeatherData.hourlyWeatherData.add(new WeatherData());
        }
        for (int i = 0; i < 48; i++)
            WeatherData.hourlyWeatherData.get(i).setDt(startDt + i * dt);

        if (WeatherData.dailyWeatherData.isEmpty())
            for (int i = 0; i < 8; i++)
                WeatherData.dailyWeatherData.add(new WeatherData());
    }
    public void receiveWeatherData() {
        todayWeatherReady = false;
        tomorrowWeatherReady = false;
        dailyWeatherReady = false;
        airPollutionReady = false;

        initCurrentLocationWeatherData();

        synchronized (this) {
            try {
                if (!LocationData.isGpsReady()) {
                    LocationData.receiveCurrentLocation(this);
                    this.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        receiveHistoricalWeatherData();
        receiveForecastWeatherData();
        receiveAirPollutionData();
        receiveDailyWeatherData();
    }

    private static void receiveHistoricalWeatherData() {
        HistoricalWeatherReceiver.getInstance().getHistoricalWeather(
                (result) -> {
                    for (int i = 0; i < result.size(); i++) {
                        WeatherData output = WeatherData.hourlyWeatherData.get(i);
                        WeatherData input  = result.get(i);

                        output.setDt(input.getDt());

                        //main
                        output.setTemp(input.getTemp());
                        output.setFeels_like(input.getFeels_like());
                        output.setHumidity(input.getHumidity());

                        //weather
                        output.setWeather(input.getWeather());
                        output.setIcon(input.getIcon());
                        output.setWind_speed(input.getWind_speed());

                        output.setRain(input.getRain());
                        output.setSnow(input.getSnow());
                    }
                    todayWeatherReady = true;
                });
    }
    private static void receiveForecastWeatherData() {
        WeatherForecastReceiver.getInstance().getWeatherForecast(
                (result) -> {
                    int di = (result.get(0).getHour() + 15) % 24;
                    for (int i = 0; i < result.size() && i + di < WeatherData.hourlyWeatherData.size(); i++) {
                        WeatherData output = WeatherData.hourlyWeatherData.get(i + di);
                        WeatherData input  = result.get(i);

                        output.setDt(input.getDt());

                        //main
                        output.setTemp(input.getTemp());
                        output.setFeels_like(input.getFeels_like());
                        output.setHumidity(input.getHumidity());

                        //weather
                        output.setWeather(input.getWeather());
                        output.setIcon(input.getIcon());
                        output.setWind_speed(input.getWind_speed());

                        output.setRain(input.getRain());
                        output.setSnow(input.getSnow());
                    }
                    tomorrowWeatherReady = true;
                });
    }
    private static void receiveDailyWeatherData() {
        DailyWeatherReceiver.getInstance().getDailyWeather(
                (result) -> {
                    for (int i = 0; i < result.size(); i++) {
                        WeatherData output = WeatherData.dailyWeatherData.get(i);
                        WeatherData input  = result.get(i);

                        output.setDt(input.getDt());

                        //main
                        output.setTemp(input.getTemp());
                        output.setFeels_like(input.getFeels_like());
                        output.setHumidity(input.getHumidity());

                        //weather
                        output.setWeather(input.getWeather());
                        output.setIcon(input.getIcon());
                        output.setWind_speed(input.getWind_speed());

                        //
                        output.setRain(input.getRain());
                        output.setSnow(input.getSnow());
                    }
                    dailyWeatherReady = true;
                });
    }
    private static void receiveAirPollutionData() {
        AirPollutionReceiver.getInstance().getAirPollution(
                (result) -> {
                    int i = 0, j = 0;
                    while (i < WeatherData.hourlyWeatherData.size() && j < result.size()) {
                        WeatherData output = WeatherData.hourlyWeatherData.get(i);
                        WeatherData input  = result.get(j);
                        if (output.getDt() == input.getDt()) {
                            output.setPm2_5(input.getPm2_5());
                            output.setPm10(input.getPm10());
                            i++;
                            j++;
                        }
                        else if (output.getDt() < input.getDt())
                            i++;
                        else
                            j++;
                    }
                    airPollutionReady = true;
                });

    }

    public static boolean isWeatherReady() {
        return todayWeatherReady && tomorrowWeatherReady && dailyWeatherReady && airPollutionReady;
    }

}
