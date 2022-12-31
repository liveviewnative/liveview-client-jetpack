package org.phoenixframework.liveview.data.dto

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.phoenixframework.liveview.extensions.isNotEmptyAndIsDigitsOnly


class CardDTO(builder: Builder) : ComposableView() {

    private var modifier: Modifier = Modifier
    private var shape: Shape
    private var backgroundColor: Color
    private var border: BorderStroke?
    private var elevation: Dp

    @Composable
    fun Compose(content: @Composable () -> Unit){
        Card(
            modifier = modifier,
            backgroundColor = backgroundColor,
            elevation = elevation,
            shape = shape,

            ) {
            content()
        }
    }
    class Builder : ComposableBuilder() {

       private var shape: Shape = RoundedCornerShape(0.dp)
       private var backgroundColor: Color = Color.White
       private var border: BorderStroke? = null
       private var elevation: Dp = 1.dp


        fun setShape(shape: String) = apply {
            this.shape = when {
                shape.isNotEmptyAndIsDigitsOnly() -> {
                    RoundedCornerShape(shape.toInt().dp)
                }
                shape.isNotEmpty() && shape == "circle" -> {
                    CircleShape
                }
                shape.isNotEmpty() && shape == "rectangle" -> {
                    RectangleShape
                }
                else -> {
                    RoundedCornerShape(0.dp)
                }
            }
        }



        fun setBackgroundColor(color: String) = apply {
            if (color.isNotEmpty()) {
                this.backgroundColor = Color(java.lang.Long.decode(color))
            }
        }

        fun setCardElevation(elevation: String) = apply {
            if (elevation.isNotEmptyAndIsDigitsOnly()) {
                this.elevation = (elevation.toInt()).dp
            }
        }

        fun getShape() = shape
        fun getCardElevation() =elevation
        fun getBackgroundColor() = backgroundColor

        fun build() = CardDTO(this)


    }

    init {
        modifier = builder.getModifier()
        shape = builder.getShape()
        elevation = builder.getCardElevation()
        modifier = Modifier
        backgroundColor = builder.getBackgroundColor()
        border = null

    }
}