package com.saklayen.currencyconverter.widgets

import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import com.google.android.material.R
import com.google.android.material.textfield.TextInputLayout
import com.saklayen.currencyconverter.domain.Status
import com.saklayen.currencyconverter.utils.getProgressBarDrawable
import com.saklayen.currencyconverter.domain.Result

class StateTextInputLayout : TextInputLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        R.attr.textInputStyle
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        endIconMode = END_ICON_CUSTOM
        endIconDrawable = progressBarDrawable
        isErrorEnabled = true

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TextInputLayout,
            defStyleAttr, R.style.Widget_Design_TextInputLayout
        ).apply {
            try {
                errorDrawable = if (hasValue(R.styleable.TextInputLayout_errorIconDrawable))
                    getDrawable(R.styleable.TextInputLayout_errorIconDrawable)
                else
                    AppCompatResources.getDrawable(getContext(), R.drawable.mtrl_ic_error)
            } finally {
                recycle()
            }
        }
        errorIconDrawable = errorDrawable
    }

    private val progressBarDrawable = context.getProgressBarDrawable()
    private val animateProgress = progressBarDrawable as? Animatable

    var errorDrawable: Drawable? = null

    fun state(result: Result<Any>) {
        when (result.status) {
            Status.SUCCESS -> stateSuccess()
            Status.LOADING -> stateLoading()
            Status.ERROR -> stateError(result.message)
            Status.NOTHING -> stateNoting()
        }
    }

    private fun stateSuccess() {
        stopProgressAnimation()
        endIconMode = END_ICON_DROPDOWN_MENU
        isEnabled = true
        clearErrorAndFocus()
    }

    private fun stateLoading() {
        startProgressAnimation()
        endIconMode = END_ICON_CUSTOM
        clearErrorAndFocus()
        isEnabled = false
    }

    private fun stateError(message: String?) {
        stopProgressAnimation()
        error = message ?: ""
        if (errorIconDrawable == null) errorIconDrawable = errorDrawable
        isEnabled = true
        clearFocus()
    }

    private fun stateNoting() {
        clearErrorAndFocus()
    }

    private fun startProgressAnimation() {
        animateProgress?.start()
    }

    private fun stopProgressAnimation() {
        if (animateProgress?.isRunning == true) animateProgress.stop()
    }

    private fun clearErrorAndFocus() {
        error = ""
        clearFocus()
    }
}

@BindingAdapter("bindState")
fun StateTextInputLayout.bindState(result: Result<Any>?) {
    if (result != null) this.state(result)
}

@BindingAdapter("bindOnclickRetry")
fun StateTextInputLayout.bindOnclickRetry(clickListener: View.OnClickListener?) {
    this.setErrorIconOnClickListener(clickListener)
}
