package xyz.schwaab.avvylib.animation

import android.animation.Animator

/**
 * Created by vitor on 19/09/18.
 */
internal fun Animator.addOnAnimationEndListener(onEnd: ((Animator?) -> Unit)) {
    addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) = Unit

        override fun onAnimationCancel(animation: Animator?) = Unit

        override fun onAnimationStart(animation: Animator?) = Unit

        override fun onAnimationEnd(animation: Animator?) {
            onEnd(animation)
        }
    })
}