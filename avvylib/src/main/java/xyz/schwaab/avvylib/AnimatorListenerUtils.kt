package xyz.schwaab.avvylib

import android.animation.Animator

/**
 * Created by vitor on 19/09/18.
 */
fun onAnimationEnd(onEnd: ((Animator?) -> Unit)) = object : Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: Animator?) = Unit

    override fun onAnimationCancel(animation: Animator?) = Unit

    override fun onAnimationStart(animation: Animator?) = Unit

    override fun onAnimationEnd(animation: Animator?) {
        onEnd(animation)
    }
}