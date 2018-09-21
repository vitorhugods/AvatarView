package xyz.schwaab.avvylib

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView

/**
 * Created by vitor on 18/09/18.
 */
internal object Defaults {
    val SCALE_TYPE = ImageView.ScaleType.CENTER_CROP
    val BITMAP_CONFIG = Bitmap.Config.ARGB_8888

    val BORDER_COLOR_HIGHLIGHT = Color.parseColor("#ffff6d00")
    val BORDER_COLOR = Color.parseColor("#ff757575")

    const val MIDDLE_COLOR = Color.TRANSPARENT

    const val COLORDRAWABLE_DIMENSION = 2
    const val BORDER_THICKNESS = 12
    const val HIGHLIGHTED_BORDER_THICKNESS = 16
    const val CIRCLE_BACKGROUND_COLOR = Color.TRANSPARENT
    const val IS_HIGHLIGHTED = false
    const val DISTANCE_TO_BORDER = 25

    const val NUMBER_OF_ARCHES = 5
    const val ARCHES_DEGREES_AREA = 90f
    const val INDIVIDUAL_ARCH_DEGREES_LENGHT = 3f
}