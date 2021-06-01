package com.example.pocketmanager.weather.receiver;

import com.example.pocketmanager.map.LocationData;
import com.example.pocketmanager.general.Time;
import com.example.pocketmanager.weather.DailyWeatherData;
import com.example.pocketmanager.weather.WeatherData;

import java.util.Iterator;
import java.util.LinkedList;

public class WeatherReceiver implements Runnable {
    private static WeatherReceiver instance = new WeatherReceiver();
    private static LinkedList<Runnable> pendingThreads = new LinkedList<>();

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
                WeatherData.dailyWeatherData.add(new DailyWeatherData());
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
                    LocationData.addPendingThread(this);
                    this.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Thread t1 = new Thread (() -> {
            receiveHistoricalWeatherData();
        });
        Thread t2 = new Thread (() -> {
            receiveForecastWeatherData();
        });
        Thread t3 = new Thread (() -> {
            receiveAirPollutionData();
        });
        Thread t4 = new Thread (() -> {
            receiveDailyWeatherData();
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }

    private static void receiveHistoricalWeatherData() {
        HistoricalWeatherReceiver.getInstance().getHistoricalWeather(
                (result) -> {
                    if (result != null) {
                        for (int i = 0; i < result.size(); i++) {
                            WeatherData output = WeatherData.hourlyWeatherData.get(i);
                            WeatherData input = result.get(i);

                            output.setDt(input.getDt());

                            //main
                            output.setTemp(input.getTemp());
                            output.setFeels_like(input.getFeels_like());
                            output.setHumidity(input.getHumidity());

                            //weather
                            output.setWeather(input.getWeather());
                            output.setIcon(input.getIcon());
                            output.setWind_speed(input.getWind_speed());

                            output.setPop(0);
                            output.setRain(input.getRain());
                            output.setSnow(input.getSnow());
                        }
                        todayWeatherReady = true;
                    }
                });
    }
    private static void receiveForecastWeatherData() {
        WeatherForecastReceiver.getInstance().getWeatherForecast(
                (result) -> {
                    if (result != null) {
                        int di = (result.get(0).getHour() + 15) % 24;
                        for (int i = 0; i < result.size() && i + di < WeatherData.hourlyWeatherData.size(); i++) {
                            WeatherData output = WeatherData.hourlyWeatherData.get(i + di);
                            WeatherData input = result.get(i);

                            output.setDt(input.getDt());

                            //main
                            output.setTemp(input.getTemp());
                            output.setFeels_like(input.getFeels_like());
                            output.setHumidity(input.getHumidity());

                            //weather
                            output.setWeather(input.getWeather());
                            output.setIcon(input.getIcon());
                            output.setWind_speed(input.getWind_speed());

                            output.setPop(input.getPop());
                            output.setRain(input.getRain());
                            output.setSnow(input.getSnow());
                        }
                        tomorrowWeatherReady = true;
                    }

                });
    }
    private static void receiveDailyWeatherData() {
        DailyWeatherReceiver.getInstance().getDailyWeather(
                (result) -> {
                    if (result != null) {
                        for (int i = 0; i < result.size(); i++) {
                            DailyWeatherData output = WeatherData.dailyWeatherData.get(i);
                            DailyWeatherData input = result.get(i);

                            output.setDt(input.getDt());

                            //main
                            output.setMax_temp(input.getMax_temp());
                            output.setMin_temp(input.getMin_temp());

                            //weather
                            output.setWeather(input.getWeather());
                            output.setIcon(input.getIcon());
                            output.setPop(input.getPop());
                        }
                        dailyWeatherReady = true;
                    }

                });
    }
    private static void receiveAirPollutionData() {
        AirPollutionReceiver.getInstance().getAirPollution(
                (result) -> {
                    if (result != null) {
                        int i = 0, j = 0;
                        while (i < WeatherData.hourlyWeatherData.size() && j < result.size()) {
                            WeatherData output = WeatherData.hourlyWeatherData.get(i);
                            WeatherData input = result.get(j);
                            if (output.getDt() == input.getDt()) {
                                output.setPm2_5(input.getPm2_5());
                                output.setPm10(input.getPm10());
                                i++;
                                j++;
                            } else if (output.getDt() < input.getDt())
                                i++;
                            else
                                j++;
                        }
                        airPollutionReady = true;
                    }

                });

    }

    public synchronized static boolean isWeatherReady() {
        return todayWeatherReady && tomorrowWeatherReady && dailyWeatherReady && airPollutionReady;
    }

}
