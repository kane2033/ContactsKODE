package com.kode.contacts.presentation.contacts.list

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.TypedValue
import android.view.View
import androidx.core.graphics.withTranslation
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.kode.contacts.R


class StickyLetterDecoration(context: Context) : RecyclerView.ItemDecoration() {

    companion object {
        private const val NO_POSITION = -1
    }

    private val textPaint: TextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        alpha = 255
        typeface = Typeface.create(
            Typeface.DEFAULT_BOLD,
            Typeface.NORMAL
        )
        textSize = context.resources.getDimensionPixelSize(R.dimen.alphabet_letter_size).toFloat()
        color = TypedValue().apply {
            context.theme.resolveAttribute(R.attr.colorOnSecondary, this, true)
        }.data
        textAlign = Paint.Align.CENTER
    }

    private val drawingSpace: Int =
        context.resources.getDimensionPixelSize(R.dimen.contact_list_margin_start)

    private val extraPadding: Float =
        context.resources.getDimensionPixelOffset(R.dimen.alphabet_extra_padding).toFloat()

    // Map позиции айтема ресайклера и Layout
    private var positionToLayoutMap: Map<Int, StaticLayout> = emptyMap()

    // Создание map "заглавная буква: первое слово, начинающееся на эту букву"
    // пр.: { "A": "Anatoliy", "B": "Barry", "K": "Kane" }
    private fun buildMapWithIndexes(words: List<String>) = words
        .mapIndexed { index, string -> index to string[0].toUpperCase().toString() }
        .distinctBy { it.second }
        .toMap()

    // Map индекса айтема и staticLayout буквы алфавита
    private fun buildPositionToLayoutMap(words: List<String>): Map<Int, StaticLayout> {
        val map = mutableMapOf<Int, StaticLayout>()
        buildMapWithIndexes(words).forEach {
            map[it.key] = buildStaticLayout(it.value)
        }
        return map
    }

    //
    fun setNewWords(words: List<String>) {
        positionToLayoutMap = buildPositionToLayoutMap(words)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        // Флаги, обозначающие, есть ли на экране
        // первое слово в алфавитной группе
        var lastFoundPosition = NO_POSITION
        var previousHeaderTop = Float.MAX_VALUE

        // Итерируем снизу вверх по вью айтмеов ресайклера
        for (viewNo in (parent.size - 1) downTo 0) {
            // Если вью не видно на экране, итерируем дальше
            val view = parent.getChildAt(viewNo)
            if (childOutsideParent(view, parent)) continue

            // Получаем позицию вью в адаптере
            val childPosition: Int = parent.getChildAdapterPosition(view)

            // Если позиция текущего айтема отображаема на экране
            // и является первым словом в алфавитной группе,
            // отрисовываем букву
            positionToLayoutMap[childPosition]?.let { initialLayout ->
                // Верхняя точка (в ресайклере)
                val top = (view.top + view.translationY + extraPadding)
                    // Чтобы не было наложения с другими буквами
                    .coerceAtMost(previousHeaderTop - initialLayout.height)
                    // Чтобы буква всегда оставалась в углу
                    .coerceAtLeast(extraPadding)

                // Отрисовываем букву на канвасе
                canvas.withTranslation(y = top, x = drawingSpace / 8f) {
                    initialLayout.draw(canvas)
                }

                // Запоминаем положение буквы алфавита
                lastFoundPosition = childPosition
                previousHeaderTop = top - extraPadding
            }

        }

        // Если на экране не отображается ни одной буквы
        if (lastFoundPosition == NO_POSITION) {
            // Находим, на какой букве мы находимся
            lastFoundPosition = parent.getChildAdapterPosition(parent.getChildAt(0)) + 1
        }

        // Отрисовка буквы между алфавитными группами
        for (initialsPosition in positionToLayoutMap.keys.reversed()) {
            if (initialsPosition < lastFoundPosition) {
                positionToLayoutMap[initialsPosition]?.let {
                    val top = (previousHeaderTop - it.height)
                        .coerceAtMost(extraPadding)
                    canvas.withTranslation(y = top, x = drawingSpace / 8f) {
                        it.draw(canvas)
                    }
                }
                break
            }
        }
    }


    private fun childOutsideParent(childView: View, parent: RecyclerView): Boolean {
        return childView.bottom < 0
                || (childView.top + childView.translationY.toInt() > parent.height)

    }

    private fun buildStaticLayout(text: String): StaticLayout {
        return if (Build.VERSION.SDK_INT >= 28) {
            StaticLayout.Builder.obtain(text, 0, 1, textPaint, drawingSpace).apply {
                setAlignment(Layout.Alignment.ALIGN_CENTER)
                setLineSpacing(0f, 1f)
                setIncludePad(false)
            }.build()
        } else {
            @Suppress("DEPRECATION")
            StaticLayout(
                text,
                textPaint,
                drawingSpace,
                Layout.Alignment.ALIGN_CENTER,
                1f,
                0f,
                false
            )
        }
    }
}