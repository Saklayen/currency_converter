package com.saklayen.currencyconverter.base.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import io.github.inflationx.viewpump.ViewPumpContextWrapper

abstract class BaseFragment<T : ViewDataBinding> constructor(@LayoutRes private val mContentLayoutId: Int) :
    Fragment() {

    private var navigationHost: NavigationHost? = null
    var binding by autoCleared<T>()
    var toolbar: MaterialToolbar? = null
        private set

    override fun onAttach(newBase: Context) {
        navigationHost = newBase as NavigationHost
        super.onAttach(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, mContentLayoutId, container, false)
        binding.root.filterTouchesWhenObscured = true
        binding.lifecycleOwner = viewLifecycleOwner
        val rootView = binding.root
        initToolbar(rootView)
        return rootView
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
        navigationHost = null
    }

    private fun initToolbar(view: View) {
        if (haveToolbar && resToolbarId != 0) {
            toolbar = view.findViewById(resToolbarId)
            toolbar?.apply { navigationHost?.registerToolbarWithNavigation(this) }
        }
    }

    protected open val resToolbarId: Int = 0

    protected open val haveToolbar = false

    //protected fun activityScreenSwitcher() = navigationHost?.activityScreenSwitcher()
}
