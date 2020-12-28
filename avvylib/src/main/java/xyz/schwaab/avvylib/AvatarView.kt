package xyz.schwaab.avvylib

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import xyz.schwaab.avvylib.animation.AvatarViewAnimationOrchestrator
import xyz.schwaab.avvylib.animation.DefaultAnimationOrchestrator
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min


/**
 * Created by vitor on 18/09/18.
 */
class AvatarView : ImageView {
    companion object {
        const val TAG = "AvatarView"
    }

    private val avatarDrawableRect = RectF()
    private val middleRect = RectF()
    private val borderRect = RectF()
    private val arcBorderRect = RectF()
    private val initialsRect = Rect()

    private val shaderMatrix = Matrix()
    private val bitmapPaint = Paint()
    private val middlePaint = Paint()
    private val borderPaint = Paint()
    private val circleBackgroundPaint = Paint()
    private val initialsPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 20f
        setup()
    }
    private val badgePaint = Paint()
    private val badgeStrokePaint = Paint()

    private var middleThickness = 0f
    private val avatarInset
        get() = distanceToBorder + max(
            borderThickness.toFloat(),
            highlightedBorderThickness.toFloat()
        )

    private var avatarDrawable: Bitmap? = null
    private var bitmapShader: BitmapShader? = null
    private var bitmapWidth = 0
    private var bitmapHeight = 0

    private var drawableRadius = 0f
    private var middleRadius = 0f
    private var borderRadius = 0f

    /**
     * The length (in degrees) available for the arches when animating.
     */
    var totalArchesDegreeArea = Defaults.ARCHES_DEGREES_AREA
        set(value) {
            field = value
            logWarningOnArcLengthIfNeeded()
            setup()
        }

    /**
     * The number of arches displayed across the border when animating.
     * This can be set to zero, if no secondary arches are wanted.
     */
    var numberOfArches = Defaults.NUMBER_OF_ARCHES
        set(value) {
            field = if (value <= 0) 1 else value
            logWarningOnArcLengthIfNeeded()
            setup()
        }

    /**
     * The length (in degrees) of each arch when animating.
     * Keep in mind that the arches may overlap if this value is too high
     * and [totalArchesDegreeArea] is too low.
     */
    var individualArcDegreeLength = Defaults.INDIVIDUAL_ARCH_DEGREES_LENGTH
        set(value) {
            field = value
            logWarningOnArcLengthIfNeeded()
            setup()
        }

    private fun logWarningOnArcLengthIfNeeded() {
        if (individualArcDegreeLength * numberOfArches > totalArchesDegreeArea)
            Log.w(
                TAG,
                "The arches are too big for them to be visible. (i.e. individualArcLength * numberOfArches > totalArchesDegreeArea)"
            )
    }

    /**
     * The color of the gap between the border and the avatar.
     * Default: [Color.TRANSPARENT]
     */
    var middleColor = Defaults.MIDDLE_COLOR
        set(value) {
            field = value
            setup()
        }

    /**
     * The border color.
     * Remember: The border is colored using a gradient.
     * If you want a solid color, make sure that the [borderColorEnd] is set to the same value.
     */
    var borderColor = Defaults.BORDER_COLOR
        set(value) {
            field = value
            setup()
        }

    /**
     * The second color of the border gradient.
     * Remember: The border is colored using a gradient.
     * If you want a solid color, make sure that the [borderColor] is set to the same value.
     */
    var borderColorEnd = Defaults.BORDER_COLOR
        set(value) {
            field = value
            setup()
        }

    /**
     * The border color when highlighted.
     * Remember: The border is colored using a gradient.
     * If you want a solid color, make sure that the [highlightBorderColorEnd] is set to the same value.
     */
    var highlightBorderColor = Defaults.BORDER_COLOR_HIGHLIGHT
        set(value) {
            field = value
            setup()
        }

    /**
     * The second color of the border gradient when highlighted.
     * Remember: The border is colored using a gradient.
     * If you want a solid color, make sure that the [highlightBorderColor] is set to the same value.
     */
    var highlightBorderColorEnd = Defaults.BORDER_COLOR_HIGHLIGHT
        set(value) {
            field = value
            setup()
        }

    /**
     * The distance (in pixels) between the avatar and the border.
     * Keep in mind that as the [borderThickness] and [highlightedBorderThickness] may be different,
     * the highest value between them will be considered in order to keep a steady avatar when
     * switching between highlight modes.
     */
    var distanceToBorder = Defaults.DISTANCE_TO_BORDER
        set(value) {
            field = if (value <= 0) 0 else value
            setup()
        }

    /**
     * The border thickness (in pixels) when not highlighted
     */
    var borderThickness = Defaults.BORDER_THICKNESS
        set(value) {
            field = if (value <= 0) 0 else value
            setup()
        }

    /**
     * The border thickness (in pixels) when highlighted
     */
    var highlightedBorderThickness = Defaults.HIGHLIGHTED_BORDER_THICKNESS
        set(value) {
            field = if (value <= 0) 0 else value
            setup()
        }

    /**
     * The background color of the avatar.
     * Only visible if the image has any transparency.
     */
    var avatarBackgroundColor = Defaults.CIRCLE_BACKGROUND_COLOR
        set(value) {
            field = value
            setup()
        }

    /**
     * The default onClick and onLongClick manifestation is a simple scale/zoom bounce.
     * It must have a onClickListener or onLongClickListener in order to happen.
     * If you want to turn it off by any reason, just set this to false.
     */
    var shouldBounceOnClick = true

    /**
     * Text for which the initial(s) has to be displayed
     */
    var text: String? = null
        set(value) {
            field = value
            findInitials()
            setup()
        }

    /**
     * The radius (in dp) of the badge
     */
    var badgeRadius = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        Defaults.BADGE_RADIUS,
        resources.displayMetrics
    )
        set(value) {
            field = value
            setup()
        }

    /**
     * The color of the badge.
     */
    var badgeColor = Defaults.BADGE_COLOR
        set(value) {
            field = value
            setup()
        }

    /**
     * The color of the stroke of the badge.
     */
    var badgeStrokeColor = Defaults.BADGE_STROKE_COLOR
        set(value) {
            field = value
            setup()
        }

    /**
     * The stroke width (in pixels) of the badge.
     */
    var badgeStrokeWidth = 0
        set(value) {
            field = if (value <= 0) 0 else value
            setup()
        }

    /**
     * Flag to toggle visibility of the badge.
     */
    var showBadge = Defaults.SHOW_BADGE
        set(value) {
            field = value
            setup()
        }

    /**
     * Position of the badge with respect to [borderRect]
     *
     * Should be one of:
     * @see [BadgePosition.BOTTOM_RIGHT]
     * @see [BadgePosition.BOTTOM_LEFT]
     * @see [BadgePosition.TOP_RIGHT]
     * @see [BadgePosition.TOP_LEFT]
     */
    var badgePosition = BadgePosition.BOTTOM_RIGHT
        set(value) {
            field = value
            setup()
        }

    private var initials: String? = null

    private val spaceBetweenArches
        get() = totalArchesDegreeArea / (numberOfArches) - individualArcDegreeLength

    private val animatorInterface = AnimatorInterface()
    private var animationDrawingState = AnimationDrawingState(0f, 0f)

    private val AnimationDrawingState.archesAreaInDegrees: Float
        get() {
            return coercedArchesExpansionProgress * totalArchesDegreeArea
        }

    private val AnimationDrawingState.rotationInDegrees: Float
        get() {
            return coercedRotationProgress * 360
        }

    private var isReadingAttributes = false

    /**
     * Set if this view should be highlighted or not.
     * If highlighted, the values of [highlightedBorderThickness], [highlightBorderColor] and [highlightBorderColorEnd] will apply
     * Otherwise, [borderThickness], [borderColor] and [borderColorEnd] will come into play.
     */
    var isHighlighted = false
        set(value) {
            field = value
            setup()
        }

    private val scaleAnimator = ValueAnimator.ofFloat(1f, 0.9f, 1f).apply {
        addUpdateListener {
            this@AvatarView.scaleX = it.animatedValue as Float
            this@AvatarView.scaleY = it.animatedValue as Float
        }
    }

    var animationOrchestrator: AvatarViewAnimationOrchestrator =
        DefaultAnimationOrchestrator.create().also {
            attachOrchestrator(it)
        }
        set(value) {
            if (field == value) return
            field.cancel()
            field = value
            attachOrchestrator(field)
        }

    private fun attachOrchestrator(animationOrchestrator: AvatarViewAnimationOrchestrator) {
        animationOrchestrator.attach(animatorInterface) {
            if (isReversedAnimating) {
                animationDrawingState = animationDrawingState.copy(
                    rotationProgress = 0f
                )
                isReversedAnimating = false
            }
        }
    }

    private var isReversedAnimating = false

    var isAnimating = false
        set(value) {
            if (value && !field) {
                if (isReversedAnimating) {
                    animationOrchestrator.reverse()
                }
                animationOrchestrator.start()
            } else if (!value && field) {
                isReversedAnimating = true
                animationOrchestrator.cancel()
                animationOrchestrator.reverse()
            }
            field = value
            setup()
        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        readAttrs(attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        readAttrs(attrs, defStyle)
        init()
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        initializeBitmap()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        initializeBitmap()
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        initializeBitmap()
    }

    private fun readAttrs(attrs: AttributeSet, defStyle: Int = 0) {
        isReadingAttributes = true
        val a = context.obtainStyledAttributes(attrs, R.styleable.AvatarView, defStyle, 0)

        avatarBackgroundColor = a.getColor(
            R.styleable.AvatarView_avvy_circle_background_color,
            Defaults.CIRCLE_BACKGROUND_COLOR
        )

        distanceToBorder = a.getDimensionPixelSize(
            R.styleable.AvatarView_avvy_distance_to_border,
            Defaults.DISTANCE_TO_BORDER
        )
        borderThickness = a.getDimensionPixelSize(
            R.styleable.AvatarView_avvy_border_thickness,
            Defaults.BORDER_THICKNESS
        )
        highlightedBorderThickness = a.getDimensionPixelSize(
            R.styleable.AvatarView_avvy_border_thickness_highlight,
            Defaults.HIGHLIGHTED_BORDER_THICKNESS
        )

        middleColor = a.getColor(R.styleable.AvatarView_avvy_middle_color, Defaults.MIDDLE_COLOR)
        borderColor = a.getColor(R.styleable.AvatarView_avvy_border_color, Defaults.BORDER_COLOR)
        borderColorEnd = a.getColor(R.styleable.AvatarView_avvy_border_color_end, borderColor)
        highlightBorderColor = a.getColor(
            R.styleable.AvatarView_avvy_border_highlight_color,
            Defaults.BORDER_COLOR_HIGHLIGHT
        )
        highlightBorderColorEnd =
            a.getColor(R.styleable.AvatarView_avvy_border_highlight_color_end, highlightBorderColor)

        isHighlighted =
            a.getBoolean(R.styleable.AvatarView_avvy_highlighted, Defaults.IS_HIGHLIGHTED)

        totalArchesDegreeArea = a.getFloat(
            R.styleable.AvatarView_avvy_loading_arches_degree_area,
            Defaults.ARCHES_DEGREES_AREA
        )
        numberOfArches =
            a.getInt(R.styleable.AvatarView_avvy_loading_arches, Defaults.NUMBER_OF_ARCHES)
        individualArcDegreeLength = a.getFloat(
            R.styleable.AvatarView_avvy_loading_arc_degree_length,
            Defaults.INDIVIDUAL_ARCH_DEGREES_LENGTH
        )

        initialsPaint.textSize =
            a.getDimension(R.styleable.AvatarView_avvy_text_size, initialsPaint.textSize)
        initialsPaint.color =
            a.getColor(R.styleable.AvatarView_avvy_text_color, initialsPaint.color)
        text = a.getString(R.styleable.AvatarView_avvy_text)

        showBadge = a.getBoolean(R.styleable.AvatarView_avvy_show_badge, Defaults.SHOW_BADGE)
        badgeColor = a.getColor(R.styleable.AvatarView_avvy_badge_color, Defaults.BADGE_COLOR)
        badgeStrokeColor =
            a.getColor(R.styleable.AvatarView_avvy_badge_stroke_color, Defaults.BADGE_STROKE_COLOR)
        badgeStrokeWidth = a.getDimensionPixelSize(
            R.styleable.AvatarView_avvy_badge_stroke_width,
            badgeStrokeWidth
        )
        badgeRadius = a.getDimension(R.styleable.AvatarView_avvy_badge_radius, badgeRadius)
        badgePosition =
            BadgePosition.values()[a.getInt(R.styleable.AvatarView_avvy_badge_position, 0)]

        a.recycle()
        isReadingAttributes = false
    }

    private fun init() {
//        setLayerType(LAYER_TYPE_HARDWARE, null)
        scaleType = Defaults.SCALE_TYPE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outlineProvider = OutlineProvider()
        }
        setup()
    }

    private fun setup() {
        if (isReadingAttributes) {
            return
        }
        if (width == 0 && height == 0) {
            return
        }

        val avatarDrawable = this.avatarDrawable
        if (avatarDrawable == null) {
            setImageResource(android.R.color.transparent)
            return
        }

        bitmapHeight = avatarDrawable.height
        bitmapWidth = avatarDrawable.width

        bitmapShader = BitmapShader(avatarDrawable, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        bitmapPaint.isAntiAlias = true
        bitmapPaint.shader = bitmapShader

        val currentBorderThickness = (if (isHighlighted)
            highlightedBorderThickness
        else borderThickness).toFloat()

        borderRect.set(calculateBounds())
        borderRadius = min(
            (borderRect.height() - currentBorderThickness) / 2.0f,
            (borderRect.width() - currentBorderThickness) / 2.0f
        )

        val currentBorderGradient = LinearGradient(
            0f, 0f, borderRect.width(), borderRect.height(),
            if (isHighlighted) highlightBorderColor else borderColor,
            if (isHighlighted) highlightBorderColorEnd else borderColorEnd,
            Shader.TileMode.CLAMP
        )
        borderPaint.apply {
            shader = currentBorderGradient
            strokeWidth = currentBorderThickness
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE
        }

        avatarDrawableRect.set(borderRect)
        avatarDrawableRect.inset(avatarInset, avatarInset)
        middleThickness =
            (borderRect.width() - currentBorderThickness * 2 - avatarDrawableRect.width()) / 2

        middleRect.set(borderRect)
        middleRect.inset(
            currentBorderThickness + middleThickness / 2,
            currentBorderThickness + middleThickness / 2
        )

        middleRadius =
            floor(middleRect.height() / 2.0).coerceAtMost(floor(middleRect.width() / 2.0)).toFloat()
        drawableRadius =
            (avatarDrawableRect.height() / 2.0f).coerceAtMost(avatarDrawableRect.width() / 2.0f)

        middlePaint.apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
            color = middleColor
            strokeWidth = middleThickness
        }

        circleBackgroundPaint.apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = avatarBackgroundColor
        }

        arcBorderRect.apply {
            set(borderRect)
            inset(currentBorderThickness / 2f, currentBorderThickness / 2f)
        }

        badgePaint.apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = badgeColor
        }

        badgeStrokePaint.apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = badgeStrokeColor
        }

        updateShaderMatrix()
        invalidate()
    }

    private fun updateShaderMatrix() {
        val scale: Float
        var dx = 0f
        var dy = 0f

        shaderMatrix.set(null)

        if (bitmapWidth * avatarDrawableRect.height() > avatarDrawableRect.width() * bitmapHeight) {
            scale = avatarDrawableRect.height() / bitmapHeight.toFloat()
            dx = (avatarDrawableRect.width() - bitmapWidth * scale) / 2f
        } else {
            scale = avatarDrawableRect.width() / bitmapWidth.toFloat()
            dy = (avatarDrawableRect.height() - bitmapHeight * scale) / 2f
        }

        shaderMatrix.setScale(scale, scale)
        shaderMatrix.postTranslate(
            (dx + 0.5f).toInt() + avatarDrawableRect.left,
            (dy + 0.5f).toInt() + avatarDrawableRect.top
        )

        bitmapShader!!.setLocalMatrix(shaderMatrix)
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        return try {
            val bitmap = if (drawable is ColorDrawable) {
                Bitmap.createBitmap(
                    Defaults.COLORDRAWABLE_DIMENSION,
                    Defaults.COLORDRAWABLE_DIMENSION,
                    Defaults.BITMAP_CONFIG
                )
            } else {
                Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Defaults.BITMAP_CONFIG
                )
            }

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun initializeBitmap() {
        avatarDrawable = getBitmapFromDrawable(drawable)
        setup()
    }

    private fun calculateBounds(): RectF {
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        val sideLength = Math.min(availableWidth, availableHeight)

        val left = paddingLeft + (availableWidth - sideLength) / 2f
        val top = paddingTop + (availableHeight - sideLength) / 2f

        return RectF(left, top, left + sideLength, top + sideLength)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return inTouchableArea(event.x, event.y) && super.onTouchEvent(event)
    }

    private fun animateClick() {
        if (shouldBounceOnClick) scaleAnimator.start()
    }

    override fun performClick(): Boolean {
        animateClick()
        return super.performClick()
    }

    override fun performLongClick(): Boolean {
        animateClick()
        return super.performLongClick()
    }

    private fun inTouchableArea(x: Float, y: Float): Boolean {
        return Math.pow(
            x - borderRect.centerX().toDouble(),
            2.0
        ) + Math.pow(y - borderRect.centerY().toDouble(), 2.0) <= Math.pow(
            borderRadius.toDouble(),
            2.0
        )
    }

    override fun onDraw(canvas: Canvas) {
        if (avatarDrawable == null &&
            initials == null
        ) {
            return
        }
        if (avatarBackgroundColor != Color.TRANSPARENT) {
            canvas.drawCircle(
                avatarDrawableRect.centerX(),
                avatarDrawableRect.centerY(),
                drawableRadius,
                circleBackgroundPaint
            )
        }

        val avatarDrawable = this.avatarDrawable
        if (null != avatarDrawable && hasAvatar()) {
            canvas.drawCircle(
                avatarDrawableRect.centerX(),
                avatarDrawableRect.centerY(),
                drawableRadius,
                bitmapPaint
            )
        } else if (null != initials) {
            canvas.drawText(
                initials!!,
                width.div(2f) - initialsRect.width().div(2f),
                height.div(2f) + initialsRect.height().div(2f),
                initialsPaint
            )
        }
        if (middleThickness > 0) {
            canvas.drawCircle(middleRect.centerX(), middleRect.centerY(), middleRadius, middlePaint)
        }
        drawBorder(canvas)
        if (showBadge && badgeColor != Color.TRANSPARENT) {
            var badgeCx = 0f
            var badgeCy = 0f
            when (badgePosition) {
                BadgePosition.BOTTOM_RIGHT -> {
                    badgeCx = borderRect.right - badgeRadius
                    badgeCy = borderRect.bottom - badgeRadius
                }
                BadgePosition.BOTTOM_LEFT -> {
                    badgeCx = borderRect.left + badgeRadius
                    badgeCy = borderRect.bottom - badgeRadius
                }
                BadgePosition.TOP_RIGHT -> {
                    badgeCx = borderRect.right - badgeRadius
                    badgeCy = borderRect.top + badgeRadius
                }
                BadgePosition.TOP_LEFT -> {
                    badgeCx = borderRect.left + badgeRadius
                    badgeCy = borderRect.top + badgeRadius
                }
            }
            if (badgeStrokeWidth > 0) {
                canvas.drawCircle(badgeCx, badgeCy, badgeRadius, badgeStrokePaint)
            }
            canvas.drawCircle(badgeCx, badgeCy, badgeRadius - badgeStrokeWidth, badgePaint)
        }
    }

    private fun hasAvatar(): Boolean {
        val drawable = this.drawable
        val hasTransparentDrawable = drawable is ColorDrawable && drawable.alpha == 0
        return !hasTransparentDrawable
    }

    private fun drawBorder(canvas: Canvas) {
        if (isAnimating || isReversedAnimating) {
            val totalDegrees = (270f + animationDrawingState.rotationInDegrees) % 360
            drawArches(totalDegrees, canvas)
            val startOfMainArch = totalDegrees + (animationDrawingState.archesAreaInDegrees)
            canvas.drawArc(
                arcBorderRect,
                startOfMainArch,
                360 - animationDrawingState.archesAreaInDegrees,
                false,
                borderPaint
            )
        } else {
            canvas.drawCircle(borderRect.centerX(), borderRect.centerY(), borderRadius, borderPaint)
        }
    }

    private fun drawArches(totalDegrees: Float, canvas: Canvas) {
        for (i in 0..numberOfArches) {
            val deg =
                totalDegrees + (spaceBetweenArches + individualArcDegreeLength) * i * (animationDrawingState.coercedArchesExpansionProgress)
            canvas.drawArc(arcBorderRect, deg, individualArcDegreeLength, false, borderPaint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        setup()
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        setup()
    }

    private fun findInitials() {
        text?.trim()?.let {
            if (it.isNotBlank()) {
                val words = it.split(' ')
                var initialsCount = 1
                initials = words[0].first().toString()
                if (words.size > 1) {
                    initialsCount++
                    initials = "$initials${words.last().first()}"
                }

                initialsPaint.getTextBounds(initials, 0, initialsCount, initialsRect)
            }
        }
    }

    /**
     * This section makes the elevation settings on Lollipop+ possible,
     * drawing a circlar shadow around the border
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private inner class OutlineProvider : ViewOutlineProvider() {

        override fun getOutline(view: View, outline: Outline) = Rect().let {
            borderRect.roundOut(it)
            outline.setRoundRect(it, it.width() / 2.0f)
        }
    }

    data class AnimationDrawingState(
        private val archesExpansionProgress: Float,
        private val rotationProgress: Float,
    ) {
        val coercedArchesExpansionProgress: Float
        val coercedRotationProgress: Float

        init {
            this.coercedArchesExpansionProgress =
                archesExpansionProgress.coerceIn(MIN_VALUE, MAX_VALUE)
            this.coercedRotationProgress = rotationProgress.coerceIn(MIN_VALUE, MAX_VALUE)
        }

        companion object {
            const val MAX_VALUE = 1f
            const val MIN_VALUE = 0f
        }
    }

    inner class AnimatorInterface {
        private val currentAnimationState: AnimationDrawingState
            get() = animationDrawingState

        fun updateAnimationState(update: (currentState: AnimationDrawingState) -> AnimationDrawingState) {
            animationDrawingState = update(currentAnimationState)
            invalidate()
        }
    }
}