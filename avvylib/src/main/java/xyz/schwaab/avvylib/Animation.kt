package xyz.schwaab.avvylib

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator

/**
 * Created by vitor on 19/09/18.
 */
object Animation {
    private const val DURATION = 2000L

    fun getLoopAnimator() = ValueAnimator.ofFloat(0f, 360f).apply {
        repeatCount = ValueAnimator.INFINITE
        duration = Animation.DURATION
        interpolator = LinearInterpolator()
    }!!

    fun getSetupAnimator() =
            ValueAnimator.ofFloat(0f, 1f).apply {
                interpolator = LinearInterpolator()
            }!!
}