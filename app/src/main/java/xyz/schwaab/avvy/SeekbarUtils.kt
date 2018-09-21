package xyz.schwaab.avvy

import com.xw.repo.BubbleSeekBar

/**
 * Created by vitor on 21/09/18.
 */

fun onUserChange(action: ((Int, Float) -> Unit)) = object: BubbleSeekBar.OnProgressChangedListenerAdapter(){
    override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
        action(progress, progressFloat)
    }
}