package com.lalinda.emojiguess.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

/**
 * AdConfig holds the AdMob Ad Unit IDs used throughout the app.
 *
 * IMPORTANT: Replace these official Google test IDs with your production AdMob IDs before publishing.
 */
object AdConfig {
    // Official Test AdMob Application ID (Set in AndroidManifest.xml)
    // const val APP_ID = "ca-app-pub-3940256099942544~3347511713"

    // Official Test Banner Unit ID
    const val BANNER_ID = "ca-app-pub-3940256099942544/9214589741"

    // Official Test Interstitial Unit ID
    const val INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"

    // Official Test Rewarded Unit ID
    const val REWARDED_ID = "ca-app-pub-3940256099942544/5224354917"
}

class AdManager(private val context: Context) {

    private var rewardedAd: RewardedAd? = null
    private var interstitialAd: InterstitialAd? = null
    private var isRewardedLoading = false
    private var isInterstitialLoading = false

    companion object {
        private const val TAG = "AdManager"

        @Volatile
        private var instance: AdManager? = null

        fun getInstance(context: Context): AdManager {
            return instance ?: synchronized(this) {
                instance ?: AdManager(context.applicationContext).also { instance = it }
            }
        }
    }

    fun initialize(onInitialized: () -> Unit = {}) {
        try {
            MobileAds.initialize(context) {
                Log.d(TAG, "Mobile Ads SDK initialized successfully")
                preloadRewardedAd()
                preloadInterstitialAd()
                onInitialized()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize Mobile Ads SDK", e)
        }
    }

    fun preloadRewardedAd() {
        if (rewardedAd != null || isRewardedLoading) return
        isRewardedLoading = true

        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            AdConfig.REWARDED_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Rewarded ad loaded successfully")
                    rewardedAd = ad
                    isRewardedLoading = false
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.w(TAG, "Rewarded ad failed to load: ${loadAdError.message}")
                    rewardedAd = null
                    isRewardedLoading = false
                }
            }
        )
    }

    fun showRewardedAd(
        activity: Activity,
        onRewardEarned: () -> Unit,
        onAdClosed: () -> Unit = {},
        onAdFailed: () -> Unit = {}
    ) {
        val ad = rewardedAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Rewarded ad dismissed")
                    rewardedAd = null
                    preloadRewardedAd()
                    onAdClosed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                    Log.w(TAG, "Rewarded ad failed to show: ${adError.message}")
                    rewardedAd = null
                    preloadRewardedAd()
                    onAdFailed()
                }
            }

            ad.show(activity) { rewardItem ->
                Log.d(TAG, "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
                onRewardEarned()
            }
        } else {
            Log.w(TAG, "Rewarded ad not ready when requested")
            preloadRewardedAd()
            onAdFailed()
        }
    }

    fun isRewardedAdReady(): Boolean = rewardedAd != null

    fun preloadInterstitialAd() {
        if (interstitialAd != null || isInterstitialLoading) return
        isInterstitialLoading = true

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            AdConfig.INTERSTITIAL_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d(TAG, "Interstitial ad loaded successfully")
                    interstitialAd = ad
                    isInterstitialLoading = false
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.w(TAG, "Interstitial ad failed to load: ${loadAdError.message}")
                    interstitialAd = null
                    isInterstitialLoading = false
                }
            }
        )
    }

    fun showInterstitialAd(
        activity: Activity,
        onAdClosed: () -> Unit = {}
    ) {
        val ad = interstitialAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Interstitial ad dismissed")
                    interstitialAd = null
                    preloadInterstitialAd()
                    onAdClosed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                    Log.w(TAG, "Interstitial ad failed to show: ${adError.message}")
                    interstitialAd = null
                    preloadInterstitialAd()
                    onAdClosed()
                }
            }
            ad.show(activity)
        } else {
            Log.w(TAG, "Interstitial ad not ready")
            preloadInterstitialAd()
            onAdClosed()
        }
    }
}
