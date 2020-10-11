package xyz.schwaab.avvy

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import xyz.schwaab.avvylib.AvatarView
import xyz.schwaab.avvylib.BadgePosition

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonToggleHighlight.setOnClickListener {
            updateAvatars {
                isHighlighted = !isHighlighted
            }
        }
        buttonToggleProgress.setOnClickListener {
            updateAvatars {
                isAnimating = !isAnimating
            }
        }
        updateAvatars {
            setOnClickListener {
                Toast.makeText(this@MainActivity, R.string.it_works, Toast.LENGTH_SHORT).show()
            }
        }

        viewHighlightColor.setOnClickListener {
            requestColorPick(avatarView.highlightBorderColor) {
                viewHighlightColor.setBackgroundColor(it)
                updateAvatars {
                    highlightBorderColor = it
                }
            }
        }
        viewHighlightColorEnd.setOnClickListener {
            requestColorPick(avatarView.highlightBorderColorEnd) {
                viewHighlightColorEnd.setBackgroundColor(it)
                updateAvatars {
                    highlightBorderColorEnd = it
                }
            }
        }
        viewColor.setOnClickListener {
            requestColorPick(avatarView.borderColor) {
                viewColor.setBackgroundColor(it)
                updateAvatars {
                    borderColor = it
                }
            }
        }
        viewColorEnd.setOnClickListener {
            requestColorPick(avatarView.borderColorEnd) {
                viewColorEnd.setBackgroundColor(it)
                updateAvatars {
                    borderColorEnd = it
                    badgePosition = BadgePosition.TOP_LEFT
                }
            }
        }
    }

    private fun updateAvatars(apply: AvatarView.() -> Unit) {
        avatarView.apply()
        avatarView2.apply()
    }

    private fun requestColorPick(initialColor: Int, onPick: ((Int) -> Unit)) {
        ColorPickerDialogBuilder
                .with(this@MainActivity)
                .setTitle(R.string.choose_color)
                .initialColor(initialColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton(R.string.confirm) { _, color, _ ->
                    onPick(color)
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .build()
                .show()
    }
    override fun onResume() {
        super.onResume()
        seekbarHighlightBorderThickness.apply {
            configBuilder.apply {
                max(avatarView.highlightedBorderThickness * 3.toFloat())
                progress(avatarView.highlightedBorderThickness.toFloat())
                build()
            }
            onProgressChangedListener = onUserChange { progress, _ ->
                updateAvatars {
                    highlightedBorderThickness = progress
                }
            }
        }
        seekbarBorderThickness.apply {
            configBuilder.apply {
                max(avatarView.borderThickness * 3.toFloat())
                progress(avatarView.borderThickness.toFloat())
                build()
            }
            onProgressChangedListener = onUserChange { progress, _ ->
                updateAvatars {
                    borderThickness = progress
                }
            }
        }
        seekbarDistanceToBorder.apply {
            configBuilder.apply {
                max(avatarView.distanceToBorder * 4.toFloat())
                progress(avatarView.distanceToBorder.toFloat())
                build()
            }
            onProgressChangedListener = onUserChange { progress, _ ->
                updateAvatars {
                    distanceToBorder = progress
                }
            }
        }

        seekbarArcLength.onProgressChangedListener = onUserChange { _, progress ->
            updateAvatars {
                individualArcDegreeLength = progress
            }
        }
        seekbarArchesArea.onProgressChangedListener = onUserChange { _, progress ->
            updateAvatars {
                totalArchesDegreeArea = progress
            }
        }
        seekbarArchesCount.onProgressChangedListener = onUserChange { progress, _ ->
            updateAvatars {
                numberOfArches = progress
            }
        }
    }
}
