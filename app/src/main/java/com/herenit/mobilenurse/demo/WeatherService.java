package com.herenit.mobilenurse.demo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * author: HouBin
 * date: 2018/12/29 13:54
 * desc: Retrofit接口
 */
public interface WeatherService {
    @GET("weather/city/{cityCode}")
    Observable<CityWeather> getCityWeather(@Path("cityCode") String cityCode);
}
