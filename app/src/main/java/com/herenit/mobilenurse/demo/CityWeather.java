package com.herenit.mobilenurse.demo;

import java.util.List;

/**
 * author: HouBin
 * date: 2018/12/29 13:50
 * desc:
 */
public class CityWeather {

    /**
     * time : 2018-12-29 09:08:57
     * cityInfo : {"city":"天津市","cityId":"101030100","parent":"天津","updateTime":"08:55"}
     * date : 20181229
     * message : Success !
     * status : 200
     * data : {"shidu":"13%","pm25":42,"pm10":60,"quality":"良","wendu":"-5","ganmao":"极少数敏感人群应减少户外活动","yesterday":{"date":"28","sunrise":"07:29","high":"高温 -4.0℃","low":"低温 -9.0℃","sunset":"16:55","aqi":35,"ymd":"2018-12-28","week":"星期五","fx":"西北风","fl":"4-5级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},"forecast":[{"date":"29","sunrise":"07:30","high":"高温 -3.0℃","low":"低温 -8.0℃","sunset":"16:56","aqi":67,"ymd":"2018-12-29","week":"星期六","fx":"西北风","fl":"4-5级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"30","sunrise":"07:30","high":"高温 -2.0℃","low":"低温 -6.0℃","sunset":"16:57","aqi":32,"ymd":"2018-12-30","week":"星期日","fx":"北风","fl":"<3级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"31","sunrise":"07:30","high":"高温 -1.0℃","low":"低温 -5.0℃","sunset":"16:58","aqi":40,"ymd":"2018-12-31","week":"星期一","fx":"东南风","fl":"<3级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"01","sunrise":"07:30","high":"高温 1.0℃","low":"低温 -5.0℃","sunset":"16:58","aqi":39,"ymd":"2019-01-01","week":"星期二","fx":"西北风","fl":"4-5级","type":"阴","notice":"不要被阴云遮挡住好心情"},{"date":"02","sunrise":"07:30","high":"高温 3.0℃","low":"低温 -4.0℃","sunset":"16:59","aqi":27,"ymd":"2019-01-02","week":"星期三","fx":"西北风","fl":"4-5级","type":"晴","notice":"愿你拥有比阳光明媚的心情"}]}
     */

    private String time;
    private CityInfoBean cityInfo;
    private String date;
    private String message;
    private int status;
    private DataBean data;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public CityInfoBean getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfoBean cityInfo) {
        this.cityInfo = cityInfo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class CityInfoBean {
        /**
         * city : 天津市
         * cityId : 101030100
         * parent : 天津
         * updateTime : 08:55
         */

        private String city;
        private String cityId;
        private String parent;
        private String updateTime;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }

    public static class DataBean {
        /**
         * shidu : 13%
         * pm25 : 42.0
         * pm10 : 60.0
         * quality : 良
         * wendu : -5
         * ganmao : 极少数敏感人群应减少户外活动
         * yesterday : {"date":"28","sunrise":"07:29","high":"高温 -4.0℃","low":"低温 -9.0℃","sunset":"16:55","aqi":35,"ymd":"2018-12-28","week":"星期五","fx":"西北风","fl":"4-5级","type":"晴","notice":"愿你拥有比阳光明媚的心情"}
         * forecast : [{"date":"29","sunrise":"07:30","high":"高温 -3.0℃","low":"低温 -8.0℃","sunset":"16:56","aqi":67,"ymd":"2018-12-29","week":"星期六","fx":"西北风","fl":"4-5级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"30","sunrise":"07:30","high":"高温 -2.0℃","low":"低温 -6.0℃","sunset":"16:57","aqi":32,"ymd":"2018-12-30","week":"星期日","fx":"北风","fl":"<3级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"31","sunrise":"07:30","high":"高温 -1.0℃","low":"低温 -5.0℃","sunset":"16:58","aqi":40,"ymd":"2018-12-31","week":"星期一","fx":"东南风","fl":"<3级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"01","sunrise":"07:30","high":"高温 1.0℃","low":"低温 -5.0℃","sunset":"16:58","aqi":39,"ymd":"2019-01-01","week":"星期二","fx":"西北风","fl":"4-5级","type":"阴","notice":"不要被阴云遮挡住好心情"},{"date":"02","sunrise":"07:30","high":"高温 3.0℃","low":"低温 -4.0℃","sunset":"16:59","aqi":27,"ymd":"2019-01-02","week":"星期三","fx":"西北风","fl":"4-5级","type":"晴","notice":"愿你拥有比阳光明媚的心情"}]
         */

        private String shidu;
        private double pm25;
        private double pm10;
        private String quality;
        private String wendu;
        private String ganmao;
        private YesterdayBean yesterday;
        private List<ForecastBean> forecast;

        public String getShidu() {
            return shidu;
        }

        public void setShidu(String shidu) {
            this.shidu = shidu;
        }

        public double getPm25() {
            return pm25;
        }

        public void setPm25(double pm25) {
            this.pm25 = pm25;
        }

        public double getPm10() {
            return pm10;
        }

        public void setPm10(double pm10) {
            this.pm10 = pm10;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public String getWendu() {
            return wendu;
        }

        public void setWendu(String wendu) {
            this.wendu = wendu;
        }

        public String getGanmao() {
            return ganmao;
        }

        public void setGanmao(String ganmao) {
            this.ganmao = ganmao;
        }

        public YesterdayBean getYesterday() {
            return yesterday;
        }

        public void setYesterday(YesterdayBean yesterday) {
            this.yesterday = yesterday;
        }

        public List<ForecastBean> getForecast() {
            return forecast;
        }

        public void setForecast(List<ForecastBean> forecast) {
            this.forecast = forecast;
        }

        public static class YesterdayBean {
            /**
             * date : 28
             * sunrise : 07:29
             * high : 高温 -4.0℃
             * low : 低温 -9.0℃
             * sunset : 16:55
             * aqi : 35.0
             * ymd : 2018-12-28
             * week : 星期五
             * fx : 西北风
             * fl : 4-5级
             * type : 晴
             * notice : 愿你拥有比阳光明媚的心情
             */

            private String date;
            private String sunrise;
            private String high;
            private String low;
            private String sunset;
            private double aqi;
            private String ymd;
            private String week;
            private String fx;
            private String fl;
            private String type;
            private String notice;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getSunrise() {
                return sunrise;
            }

            public void setSunrise(String sunrise) {
                this.sunrise = sunrise;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getSunset() {
                return sunset;
            }

            public void setSunset(String sunset) {
                this.sunset = sunset;
            }

            public double getAqi() {
                return aqi;
            }

            public void setAqi(double aqi) {
                this.aqi = aqi;
            }

            public String getYmd() {
                return ymd;
            }

            public void setYmd(String ymd) {
                this.ymd = ymd;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getFx() {
                return fx;
            }

            public void setFx(String fx) {
                this.fx = fx;
            }

            public String getFl() {
                return fl;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getNotice() {
                return notice;
            }

            public void setNotice(String notice) {
                this.notice = notice;
            }
        }

        public static class ForecastBean {
            /**
             * date : 29
             * sunrise : 07:30
             * high : 高温 -3.0℃
             * low : 低温 -8.0℃
             * sunset : 16:56
             * aqi : 67.0
             * ymd : 2018-12-29
             * week : 星期六
             * fx : 西北风
             * fl : 4-5级
             * type : 晴
             * notice : 愿你拥有比阳光明媚的心情
             */

            private String date;
            private String sunrise;
            private String high;
            private String low;
            private String sunset;
            private double aqi;
            private String ymd;
            private String week;
            private String fx;
            private String fl;
            private String type;
            private String notice;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getSunrise() {
                return sunrise;
            }

            public void setSunrise(String sunrise) {
                this.sunrise = sunrise;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getSunset() {
                return sunset;
            }

            public void setSunset(String sunset) {
                this.sunset = sunset;
            }

            public double getAqi() {
                return aqi;
            }

            public void setAqi(double aqi) {
                this.aqi = aqi;
            }

            public String getYmd() {
                return ymd;
            }

            public void setYmd(String ymd) {
                this.ymd = ymd;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getFx() {
                return fx;
            }

            public void setFx(String fx) {
                this.fx = fx;
            }

            public String getFl() {
                return fl;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getNotice() {
                return notice;
            }

            public void setNotice(String notice) {
                this.notice = notice;
            }
        }
    }
}
