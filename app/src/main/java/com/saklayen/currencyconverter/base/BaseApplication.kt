package com.saklayen.currencyconverter.base

import android.app.Application
import android.content.res.Resources
import android.os.StrictMode
import com.facebook.stetho.Stetho
import com.saklayen.currencyconverter.BuildConfig
import com.saklayen.currencyconverter.R
import com.saklayen.currencyconverter.analytics.CrashReportingTree
import dagger.hilt.android.HiltAndroidApp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import timber.log.Timber

@HiltAndroidApp
class BaseApplication : Application() {

    companion object {
        lateinit var instance: Application
        lateinit var appResources: Resources
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        appResources = resources

        if (BuildConfig.DEBUG) enableStrictMode()
        initCalligraphy()
        initTimber()
        Stetho.initializeWithDefaults(this)
    }

    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build()
        )
    }

    private fun initCalligraphy() {
        val fontPath = "fonts/BalooDa2-Regular.ttf"
        val mCalligraphyConfig = CalligraphyConfig.Builder()
            //  .setDefaultFontPath(fontPath)
            .setFontAttrId(R.attr.fontPath)
            .build()
        val mInterceptor = CalligraphyInterceptor(mCalligraphyConfig)
        val mViewPumpBuilder = ViewPump.builder()
            .addInterceptor(mInterceptor)
            .build()
        ViewPump.init(mViewPumpBuilder)
    }

    private fun initTimber() {
        Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else CrashReportingTree())
    }
}
