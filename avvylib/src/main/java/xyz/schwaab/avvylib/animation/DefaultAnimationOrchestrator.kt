package xyz.schwaab.avvylib.animation

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import xyz.schwaab.avvylib.AvatarView

object DefaultAnimationOrchestrator {

    private const val DEFAULT_ROTATION_DURATION = 2000L
    private const val DEFAULT_EXPANSION_DURATION = 250L

    fun create(
        rotationDurationInMillis: Long = DEFAULT_ROTATION_DURATION,
        expandDurationInMillis: Long = DEFAULT_EXPANSION_DURATION
    ): AvatarViewAnimationOrchestrator {
        val expansionAnimator = createDefaultExpansionAnimator(expandDurationInMillis)
        val rotationAnimator = createDefaultRotationAnimator(rotationDurationInMillis)

        return AvatarViewAnimationOrchestrator(expansionAnimator, rotationAnimator)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun createDefaultExpansionAnimator(expandDurationInMillis: Long): AvatarViewAnimator {
        return object : AvatarViewAnimator {
            override val baseAnimator = ValueAnimator.ofFloat(
                AvatarView.AnimationDrawingState.MIN_VALUE,
                AvatarView.AnimationDrawingState.MAX_VALUE
            ).apply {
                interpolator = LinearInterpolator()
                duration = expandDurationInMillis
            }!!

            override fun onValueUpdate(animatorInterface: AvatarView.AnimatorInterface) {
                animatorInterface.updateAnimationState { state ->
                    val animatedValue = baseAnimator.animatedValue as Float
                    state.copy(archesExpansionProgress = animatedValue)
                }
            }
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun createDefaultRotationAnimator(rotationDurationInMillis: Long): AvatarViewAnimator {
        return object : AvatarViewAnimator {
            override val baseAnimator: ValueAnimator = ValueAnimator.ofFloat(
                AvatarView.AnimationDrawingState.MIN_VALUE,
                AvatarView.AnimationDrawingState.MAX_VALUE
            ).apply {
                repeatCount = ValueAnimator.INFINITE
                duration = rotationDurationInMillis
                interpolator = LinearInterpolator()
            }!!

            override fun onValueUpdate(animatorInterface: AvatarView.AnimatorInterface) {
                animatorInterface.updateAnimationState { state ->
                    val animatedValue = baseAnimator.animatedValue as Float
                    state.copy(rotationProgress = animatedValue)
                }
            }
        }
    }
}