package org.phoenixframework.liveview.data.dto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import org.phoenixframework.liveview.domain.base.ComposableBuilder
import org.phoenixframework.liveview.domain.base.ComposableView
import org.phoenixframework.liveview.domain.extensions.isNotEmptyAndIsDigitsOnly
import org.phoenixframework.liveview.domain.factory.ComposableTreeNode
import org.phoenixframework.liveview.ui.phx_components.paddingIfNotNull

class LazyRowDTO private constructor(builder: Builder) :
    ComposableView(modifier = builder.modifier) {
    private var horizontalArrangement: Arrangement.Horizontal = builder.horizontalArrangement
    private var verticalAlignment: Alignment.Vertical = builder.verticalAlignment

    // content padding is a pair of pairs,
    // the first pair is the horizontal padding,
    // the second pair is the vertical padding
    private val contentPadding: Pair<Pair<Int, Int>, Pair<Int, Int>> = builder.contentPadding

    private val reverseLayout: Boolean = builder.reverseLayout

    @Composable
    fun ComposeLazyItems(
        items: MutableList<ComposableTreeNode>,
        paddingValues: PaddingValues?,
        drawContent: @Composable (node: ComposableTreeNode) -> Unit
    ) {
        LazyRow(
            modifier = modifier.paddingIfNotNull(paddingValues),
            reverseLayout = reverseLayout,
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            contentPadding = PaddingValues(
                contentPadding.first.first.dp,
                contentPadding.second.first.dp,
                contentPadding.first.second.dp,
                contentPadding.second.second.dp
            ),
            content = {
                items(items, key = { item -> item.id }) { item -> drawContent(item) }
            }
        )
    }

    class Builder : ComposableBuilder() {
        var horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceAround
        var verticalAlignment: Alignment.Vertical = Alignment.CenterVertically
        var contentPadding: Pair<Pair<Int, Int>, Pair<Int, Int>> = Pair(Pair(0, 0), Pair(0, 0))
        var reverseLayout: Boolean = false

        fun reverseLayout(isReverseLayout: String) = apply {
            if (isReverseLayout.isNotEmpty()) {
                this.reverseLayout = isReverseLayout.toBoolean()
            }
        }

        fun horizontalArrangement(horizontalArrangement: String) = apply {
            if (horizontalArrangement.isNotEmpty()) {
                this.horizontalArrangement = when (horizontalArrangement) {
                    "spaced-evenly" -> Arrangement.SpaceEvenly
                    "space-around" -> Arrangement.SpaceAround
                    "space-between" -> Arrangement.SpaceBetween
                    "start" -> Arrangement.Start
                    "end" -> Arrangement.End
                    else -> if (horizontalArrangement.isNotEmptyAndIsDigitsOnly()) {
                        Arrangement.spacedBy(horizontalArrangement.toInt().dp)
                    } else {
                        Arrangement.Center
                    }
                }
            }
        }

        fun verticalAlignment(verticalAlignment: String) = apply {
            if (verticalAlignment.isNotEmpty()) {
                this.verticalAlignment = when (verticalAlignment) {
                    "top" -> Alignment.Top
                    "center" -> Alignment.CenterVertically
                    else -> Alignment.Bottom
                }
            }
        }

        fun rightPadding(paddingValue: String) = apply {
            if (paddingValue.isNotEmptyAndIsDigitsOnly()) {
                contentPadding = Pair(
                    Pair(contentPadding.first.first, paddingValue.toInt()),
                    Pair(contentPadding.second.first, contentPadding.second.second)
                )
            }
        }

        fun leftPadding(paddingValue: String) = apply {
            if (paddingValue.isNotEmptyAndIsDigitsOnly()) {
                contentPadding = Pair(
                    Pair(paddingValue.toInt(), contentPadding.first.second),
                    Pair(contentPadding.second.first, contentPadding.second.second)
                )
            }
        }

        fun topPadding(paddingValue: String) = apply {
            if (paddingValue.isNotEmptyAndIsDigitsOnly()) {
                contentPadding = Pair(
                    Pair(contentPadding.first.first, contentPadding.first.second),
                    Pair(paddingValue.toInt(), contentPadding.second.second)
                )
            }
        }

        fun bottomPadding(paddingValue: String) = apply {
            if (paddingValue.isNotEmptyAndIsDigitsOnly()) {
                contentPadding = Pair(
                    Pair(contentPadding.first.first, contentPadding.first.second),
                    Pair(contentPadding.second.first, paddingValue.toInt())
                )
            }
        }

        fun lazyRowItemPadding(paddingValue: String) = apply {
            if (paddingValue.isNotEmptyAndIsDigitsOnly()) {
                contentPadding = Pair(
                    Pair(paddingValue.toInt(), paddingValue.toInt()),
                    Pair(paddingValue.toInt(), paddingValue.toInt())
                )
            }
        }

        override fun size(size: String): Builder = apply { super.size(size) }

        override fun padding(padding: String): Builder = apply { super.padding(padding) }

        override fun verticalPadding(padding: String): Builder = apply {
            super.verticalPadding(padding)
        }

        override fun horizontalPadding(padding: String): Builder = apply {
            super.horizontalPadding(padding)
        }

        override fun height(height: String): Builder = apply { super.height(height) }

        override fun width(width: String): Builder = apply { super.width(width) }

        fun build() = LazyRowDTO(this)
    }
}