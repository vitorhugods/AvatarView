package xyz.schwaab.avvy

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonToggleHighlight.setOnClickListener {
            avatarView.isHighlighted = !avatarView.isHighlighted
        }
        buttonToggleProgress.setOnClickListener {
            avatarView.isAnimating = !avatarView.isAnimating
        }
        avatarView.setOnClickListener {
            Toast.makeText(this@MainActivity, "It works!", Toast.LENGTH_SHORT).show()
        }

        viewHighlightColor.setOnClickListener {
            requestColorPick(avatarView.highlightBorderColor){
                viewHighlightColor.setBackgroundColor(it)
                avatarView.highlightBorderColor = it
            }
        }
        viewHighlightColorEnd.setOnClickListener {
            requestColorPick(avatarView.highlightBorderColorEnd){
                viewHighlightColorEnd.setBackgroundColor(it)
                avatarView.highlightBorderColorEnd = it
            }
        }
        viewColor.setOnClickListener {
            requestColorPick(avatarView.borderColor){
                viewColor.setBackgroundColor(it)
                avatarView.borderColor = it
            }
        }
        viewColorEnd.setOnClickListener {
            requestColorPick(avatarView.borderColorEnd){
                viewColorEnd.setBackgroundColor(it)
                avatarView.borderColorEnd = it
            }
        }
    }

    fun requestColorPick(initialColor: Int, onPick: ((Int) -> Unit)){
        ColorPickerDialogBuilder
                .with(this@MainActivity)
                .setTitle(R.string.choose_color)
                .initialColor(initialColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton(R.string.confirm) { _, color, _ ->
                    onPick(color)
                }
                .setNegativeButton(R.string.cancel){_,_ -> }
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
                avatarView.highlightedBorderThickness = progress
            }
        }
        seekbarBorderThickness.apply {
            configBuilder.apply {
                max(avatarView.borderThickness * 3.toFloat())
                progress(avatarView.borderThickness.toFloat())
                build()
            }
            onProgressChangedListener = onUserChange { progress, _ ->
                avatarView.borderThickness = progress
            }
        }
        seekbarDistanceToBorder.apply {
            configBuilder.apply {
                max(avatarView.distanceToBorder * 4.toFloat())
                progress(avatarView.distanceToBorder.toFloat())
                build()
            }
            onProgressChangedListener = onUserChange { progress, _ ->
                avatarView.distanceToBorder = progress
            }
        }

        seekbarArcLenght.onProgressChangedListener = onUserChange { _, progress ->
            avatarView.individualArcDegreeLenght = progress
        }
        seekbarArchesArea.onProgressChangedListener = onUserChange { _, progress ->
            avatarView.totalArchesDegreeArea = progress
        }
        seekbarArchesCount.onProgressChangedListener = onUserChange { progress, _ ->
            avatarView.numberOfArches = progress
        }
    }
}
