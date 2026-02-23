package com.example.myfristapplication.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetUtil {
    public static String BASE_URL = "http://v1.yiketianqi.com/free/day";
    public static String APP_ID = "57866311";
    public static String APP_SECRET = "OuJ5ZZIt";
    public static String doGet(String url){
        HttpURLConnection httpURLConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;

        try {
//            1.建立联系
            URL requestUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestProperty("charset","utf-8");
            httpURLConnection.connect();

//            2.获取二进制流
            InputStream inputStream = httpURLConnection.getInputStream();

//            3.将二进制流包装
            reader = new BufferedReader(new InputStreamReader(inputStream));

//            4.从BufferedReader中读取string字符串
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null){
                builder.append(line);
//                builder.append("\n");
            }

            if (builder.length() == 0) {
                return null;
            }
            bookJSONString = builder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bookJSONString;
    }

    public static String getWeatherOfCity(String city){
//        拼接出请求的url
        String weatherUrl = BASE_URL + "?" + "appid=" + APP_ID + "&" + "appsecret=" + APP_SECRET + "&city=" + city;
        Log.d("tag", "----weatherUrl----" + weatherUrl);
        String weatherResult = doGet(weatherUrl);
        return weatherResult;
    }
}
