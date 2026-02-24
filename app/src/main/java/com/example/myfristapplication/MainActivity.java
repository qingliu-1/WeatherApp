package com.example.myfristapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myfristapplication.util.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

/**
 * 天气查询主页面
 * 功能：接收用户输入的城市名，调用天气API并展示天气信息
 * 设计思路：
 * 1. UI层：使用EditText接收输入，Button触发查询，TextView展示结果
 * 2. 网络层：通过NetUtil工具类发起HTTP请求，获取JSON数据
 * 3. 逻辑层：使用Handler处理子线程返回的结果，更新UI
 *
 * @author 华士豪
 * @date 2026-02-23
 */
public class MainActivity extends AppCompatActivity {
//    查询天气的Button按钮
    private Button btStart;
//    输入城市名的编辑框
    private EditText etCity;
//    用于展示天气的TextView
    private TextView tvWeather,tvWin,tvWinSpeed,tvTem,tvTemDay,tvTemNight;
    /**
     * 处理子线程返回的天气数据，更新UI
     * 注意：Android不允许在子线程中直接更新UI，必须通过Handler切换到主线程
     */
    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0){
                String message = (String) msg.obj;
//              解析JSON并更新UI
                parseJsonDataAndShow(message);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//      初始化视图控件
        initView();
//        设置查询按钮点击事件
        btStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                获取城市名并去除空格
                String city = etCity.getText().toString().trim();
//                空值判断，防止无效请求
                if (city.isEmpty()) {
                    Toast.makeText(MainActivity.this, "请输入要查询的城市！", Toast.LENGTH_SHORT).show();
                    return;
                }
//                开启子线程执行网络请求
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = NetUtil.getWeatherOfCity(city);

                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = result;

                        handler.sendMessage(msg);
                    }
                });
                thread.start();
            }
        });
    }

    /**
     * {
     *     "nums":226, //今日实时请求次数
     *     "cityid":"101120101", //城市ID
     *     "city":"济南",
     *     "date":"2022-05-05",
     *     "week":"星期四",
     *     "update_time":"22:38", //更新时间
     *     "wea":"多云", //天气情况
     *     "wea_img":"yun", //天气标识
     *     "tem":"25", //实况温度
     *     "tem_day":"30", //白天温度(高温)
     *     "tem_night":"23", //夜间温度(低温)
     *     "win":"南风", //风向
     *     "win_speed":"3级", //风力
     *     "win_meter":"19km\/h", //风速
     *     "air":"53", //空气质量
     *     "pressure":"987", //气压
     *     "humidity":"27%" //湿度
     * }
     */
    /**
     * 解析天气API返回的JSON数据，并更新UI
     * @param jsonStr API返回的JSON字符串
     */
    private void parseJsonDataAndShow(String jsonStr) {
        // 解析JSON并更新TextView的代码...
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            String cityId = jsonObject.optString("cityid");
            String city = jsonObject.optString("city");
            String date = jsonObject.optString("date");
            String week = jsonObject.optString("week");
            String updateTime = jsonObject.optString("update_time");
            String weather = jsonObject.optString("wea");
            String weaImg = jsonObject.optString("wea_img");
            String tem = jsonObject.optString("tem");
            String temDay = jsonObject.optString("tem_day");
            String temNight = jsonObject.optString("tem_night");
            String win = jsonObject.optString("win");
            String winSpeed = jsonObject.optString("win_speed");
            String winMeter = jsonObject.optString("win_meter");
            String air = jsonObject.optString("air");
            String pressure = jsonObject.optString("pressure");
            String humidity = jsonObject.optString("humidity");

            tvWeather.setText(weather);
            tvWin.setText(win);
            tvWinSpeed.setText(winSpeed);
            tvTem.setText(tem);
            tvTemDay.setText(temDay);
            tvTemNight.setText(temNight);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    private void initView() {
        btStart = findViewById(R.id.bt_start);
        etCity = findViewById(R.id.et_city);
        tvWeather = findViewById(R.id.tv_weather);
        tvWin = findViewById(R.id.tv_win);
        tvWinSpeed = findViewById(R.id.tv_win_speed);
        tvTem = findViewById(R.id.tv_tem);
        tvTemDay = findViewById(R.id.tv_tem_day);
        tvTemNight = findViewById(R.id.tv_tem_night);
    }


}