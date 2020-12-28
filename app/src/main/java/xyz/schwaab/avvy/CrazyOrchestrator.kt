package xyz.schwaab.avvy

import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import xyz.schwaab.avvylib.AvatarView
import xyz.schwaab.avvylib.animation.AvatarViewAnimationOrchestrator
import xyz.schwaab.avvylib.animation.AvatarViewAnimator
import kotlin.math.*

object CrazyOrchestrator {
    fun create(): AvatarViewAnimationOrchestrator {
        val bouncer = object : AvatarViewAnimator {
            override val baseAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
                repeatCount = ValueAnimator.INFINITE
                duration = 3000L
                interpolator = BounceInterpolator()
            }

            override fun onValueUpdate(animatorInterface: AvatarView.AnimatorInterface) {
                val animatedValue = baseAnimator.animatedValue as Float
                animatorInterface.updateAnimationState { currentState ->
                    currentState.copy(rotationProgress = animatedValue)
                }
            }
        }
        val expansion = object: AvatarViewAnimator{
            override val baseAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
                repeatMode = ValueAnimator.REVERSE
                duration = 500L
                interpolator = DecelerateInterpolator()
            }

            override fun onValueUpdate(animatorInterface: AvatarView.AnimatorInterface) {
                val animatedValue = baseAnimator.animatedValue as Float
                animatorInterface.updateAnimationState { currentState ->
                    currentState.copy(archesExpansionProgress = animatedValue)
                }
            }
        }
        return AvatarViewAnimationOrchestrator(expansion, bouncer)
    }
}