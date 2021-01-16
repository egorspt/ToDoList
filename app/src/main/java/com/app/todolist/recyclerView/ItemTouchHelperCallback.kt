package com.app.todolist.recyclerView

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.END
import androidx.recyclerview.widget.ItemTouchHelper.START
import androidx.recyclerview.widget.RecyclerView
import com.app.todolist.R

class ItemTouchHelperCallback(
    private val adapter: SwipeListener
) : ItemTouchHelper.SimpleCallback(0, START or END) {

    private val paint = Paint()

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onSwipe(viewHolder.adapterPosition, direction)
    }

    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView

            paint.color = Color.RED

            val rectF: RectF
            val x: Float
            rectF = if (dX > 0)
                RectF(
                    itemView.left.toFloat(),
                    itemView.top.toFloat() - 5,
                    itemView.left + dX,
                    itemView.bottom.toFloat() + 5
                )
            else
                RectF(
                    itemView.right + dX,
                    itemView.top.toFloat() - 5,
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat() + 5
                )

            c.drawRect(rectF, paint)
            drawText("Удалить", c, rectF, paint)
        } else
            super.onChildDrawOver(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
    }

    private fun drawText(
        text: String,
        c: Canvas,
        button: RectF,
        p: Paint
    ) {
        val textSize = 40f
        p.apply {
            color = Color.WHITE
            isAntiAlias = true
            this.textSize = textSize
        }
        val textWidth = if (button.left == 0f) p.measureText(text) else 0f
        c.drawText(text, button.centerX() - textWidth, button.centerY() + textSize / 4, p)
    }
}