package xyz.schwaab.avvylib.animation

import android.animation.AnimatorSet
import xyz.schwaab.avvylib.AvatarView

/**
 * @param setupAnimators These animators will run before the progressAnimators.
 * When stopping the animations, these animators will be played in reverse, to animate back to the original state.
 * It's really useful to expand/collapse the arches, manipulating the [AvatarView.AnimationDrawingState.archesExpansionProgress] parameter during animation
 * @param progressAnimators These will e run after the setup animators.
 * They will not be called in reverse, as they expected to repeat infinitely.
 */
class AvatarViewAnimationOrchestrator(
    private val setupAnimators: List<AvatarViewAnimator> = listOf(),
    private val progressAnimators: List<AvatarViewAnimator> = listOf(),
) {

    constructor(setupAnimator: AvatarViewAnimator, progressAnimator: AvatarViewAnimator) : this(
        listOf(setupAnimator),
        listOf(progressAnimator)
    )

    val setupSet = AnimatorSet().apply {
        val baseAnimators = setupAnimators.map { it.baseAnimator }
        if (baseAnimators.isNotEmpty()) {
            playTogether(*baseAnimators.toTypedArray())
        }
    }

    /**
     * These animators will be called
     */
    val progressSet = AnimatorSet().apply {
        val baseAnimators = progressAnimators.map { it.baseAnimator }
        if (baseAnimators.isNotEmpty()) {
            playTogether(*baseAnimators.toTypedArray())
        }
    }

    val animatorSet = AnimatorSet().apply {
        playSequentially(setupSet, progressSet)
    }

    internal fun cancel() {
        animatorSet.cancel()
    }

    internal fun start() {
        animatorSet.start()
    }

    internal fun reverse() {
        setupAnimators.forEach { animator ->
            animator.baseAnimator.reverse()
        }
    }

    internal fun attach(
        animatorInterface: AvatarView.AnimatorInterface,
        onSetupEnd: () -> Unit = {}
    ) {
        (setupAnimators + progressAnimators).forEach { animator ->
            animator.baseAnimator.addUpdateListener {
                animator.onValueUpdate(animatorInterface)
            }
        }
        setupSet.addOnAnimationEndListener {
            onSetupEnd()
        }
    }
}