package com.example.yourmovies;
import android.app.Application;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;
//import com.yandex.metrica.push.YandexMetricaPush;

public class App extends Application {

    /**
     * Replace API_KEY with your unique API key. Please, read official documentation how to obtain one:
     * https://tech.yandex.com/metrica-mobile-sdk/doc/mobile-sdk-dg/concepts/android-initialize-docpage/
     */
    private static final String API_KEY = "e00071a2-3285-42d1-86b4-b81aadf239d9";

    @Override
    public void onCreate() {
        super.onCreate();

        //YandexMetrica must be activated before using YandexMetricaPush
        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder(API_KEY).build();
        // Initializing the AppMetrica SDK.
        YandexMetrica.activate(getApplicationContext(), config);
        // Automatic tracking of user activity.
        YandexMetrica.enableActivityAutoTracking(this);
        //YandexMetricaPush.init(this);
    }
}
