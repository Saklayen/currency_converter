package com.saklayen.currencyconverter.analytics

import android.util.Log

import timber.log.Timber

class CrashReportingTree : Timber.Tree() {
    override fun log(mPriority: Int, mTag: String?, mMessage: String, mThrowable: Throwable?) {
        if (mPriority == Log.VERBOSE || mPriority == Log.DEBUG) {
            return
        }
        TODO("Gazi add Crashlytics")
//        Crashlytics.log(mPriority, mTag, mMessage)
//        if (mThrowable != null) {
//            Crashlytics.logException(mThrowable)
//        }
    }
}
