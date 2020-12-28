package xyz.schwaab.avvylib.animation

import android.animation.ValueAnimator
import xyz.schwaab.avvylib.AvatarView

interface AvatarViewAnimator {
    val baseAnimator: ValueAnimator

    fun onValueUpdate(animatorInterface: AvatarView.AnimatorInterface)
}