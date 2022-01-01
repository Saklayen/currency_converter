package com.saklayen.currencyconverter.base.ui

import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.saklayen.currencyconverter.base.navigation.ActivityScreenSwitcher
import com.asainternational.ambsmobile.base.ui.NavigationHost
import com.google.android.material.appbar.MaterialToolbar
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding> constructor(@LayoutRes private val mContentLayoutId: Int) :
    AppCompatActivity(),
    NavigationHost {
    protected val binding: T by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.setContentView<T>(this, mContentLayoutId)
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(context))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.root.filterTouchesWhenObscured = true
        binding.lifecycleOwner = this
    }

    override fun registerToolbarWithNavigation(toolbar: MaterialToolbar) {
    }

}
