package com.example.carlog
import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class AdManager(private val context: Context) {
    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false

    // SENARYO 1: Önceden yükle (Örneğin LaunchedEffect içinde çağrılır)
    fun loadInterstitialAd(adUnitId: String) {
        if ((interstitialAd != null) || isLoading) return

        isLoading = true
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            adUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoading = false
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                    isLoading = false
                }
            }
        )
    }

    // SENARYO 1 Devamı: Önceden yüklenen reklamı bir aksiyonda (buton vb.) göster
    fun showInterstitialAd(activity: Activity, onAdDismissed: () -> Unit) {
        val ad = interstitialAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    onAdDismissed() // Reklam kapatılınca kullanıcının işlemine devam et
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    onAdDismissed() // Hata alsa da kullanıcıyı bloklama
                }
            }
            ad.show(activity)
        } else {
            // Reklam henüz yüklenmediyse kullanıcıyı bekletmeden akışa devam et
            onAdDismissed()
        }
    }

    // SENARYO 2: Yüklendiği an anında göster (Anlık tetiklemeler için)
    fun loadAndShowInterstitialAd(activity: Activity, adUnitId: String, onAdDismissed: (() -> Unit)? = null) {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            adUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            onAdDismissed?.invoke()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            onAdDismissed?.invoke()
                        }
                    }
                    ad.show(activity)
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    onAdDismissed?.invoke()
                }
            }
        )
    }
}