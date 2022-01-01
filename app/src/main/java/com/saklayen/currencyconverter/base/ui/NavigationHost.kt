package com.asainternational.ambsmobile.base.ui

import com.saklayen.currencyconverter.base.navigation.ActivityScreenSwitcher
import com.google.android.material.appbar.MaterialToolbar

interface NavigationHost {
    fun registerToolbarWithNavigation(toolbar: MaterialToolbar)

    //fun activityScreenSwitcher(): ActivityScreenSwitcher
}
