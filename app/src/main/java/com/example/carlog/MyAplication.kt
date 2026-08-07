package com.example.carlog
import android.app.Application
import com.google.android.gms.ads.MobileAds

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // AdMob SDK'sını uygulama başladığında ilklendiriyoruz
        MobileAds.initialize(this) {}
    }
}