package com.saklayen.currencyconverter.base.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import com.saklayen.currencyconverter.base.ui.BaseActivity
import kotlin.reflect.KClass


abstract class ActivityScreen : Screen {

    private var mTransitionView: View? = null

    fun attachTransitionView(mView: View?) {
        mTransitionView = mView
    }

    protected fun detachTransitionView(): View? {
        val mView = mTransitionView
        mTransitionView = null
        return mView
    }

    fun intent(mContext: Context): Intent {
        val intent = Intent(mContext, activityClass().java)
        configureIntent(intent)
        return intent
    }

    fun activityOptions(mActivity: Activity): Bundle? {
        val transitionView = detachTransitionView() ?: return null
        return ActivityOptionsCompat.makeSceneTransitionAnimation(
            mActivity,
            transitionView, BF_TRANSITION_VIEW
        ).toBundle()
    }

    protected abstract fun activityClass(): KClass<out BaseActivity<*>>

    protected abstract fun configureIntent(intent: Intent)

    companion object {
        private const val BF_TRANSITION_VIEW = "ActivityScreen.transitionView"

        fun setTransitionView(view: View) {
            ViewCompat.setTransitionName(view, BF_TRANSITION_VIEW)
        }
    }
}
