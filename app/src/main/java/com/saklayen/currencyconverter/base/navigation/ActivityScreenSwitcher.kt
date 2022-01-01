package com.saklayen.currencyconverter.base.navigation

import android.app.Activity
import android.content.Intent
import androidx.core.app.ActivityCompat
import com.saklayen.currencyconverter.base.ui.BaseActivity

class ActivityScreenSwitcher : ScreenSwitcher<ActivityScreen> {
    private var mActivity: BaseActivity<*>? = null

    fun attach(mActivity: BaseActivity<*>) {
        this.mActivity = mActivity
    }

    fun detach() {
        this.mActivity = null
    }

    override fun open(mScreen: ActivityScreen) {
        when (mActivity) {
            null -> return
            else -> {
                val intent = mScreen.intent(mActivity!!)
                ActivityCompat.startActivity(
                    mActivity!!, intent,
                    mScreen.activityOptions(mActivity!!)
                )
            }
        }
    }

    override fun goBack() {
        if (mActivity != null) {
            mActivity!!.onBackPressed()
        }
    }

    fun startForResult(mScreen: ActivityScreen, mRequestCode: Int) {
        when (mActivity) {
            null -> return
            else -> {
                val mIntent = mScreen.intent(mActivity!!)
                ActivityCompat.startActivityForResult(
                    mActivity!!,
                    mIntent,
                    mRequestCode,
                    mScreen.activityOptions(mActivity!!)
                )
            }
        }
    }

    fun setResultAndGoBack(mData: Intent?) {
        if (mActivity == null)
            return
        if (mData != null) {
            mActivity!!.setResult(Activity.RESULT_OK, mData)
        }
        goBack()
    }
}
