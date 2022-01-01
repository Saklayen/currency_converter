package com.saklayen.currencyconverter.base.navigation

interface ScreenSwitcher<S : Screen> {
    fun open(mScreen: S)

    fun goBack()
}
