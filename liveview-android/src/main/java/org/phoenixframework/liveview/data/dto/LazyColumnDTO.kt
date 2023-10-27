package org.phoenixframework.liveview.data.dto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import org.phoenixframework.liveview.data.core.CoreAttribute
import org.phoenixframework.liveview.domain.base.ComposableView
import org.phoenixframework.liveview.domain.base.ComposableViewFactory
import org.phoenixframework.liveview.domain.base.PushEvent
import org.phoenixframework.liveview.domain.factory.ComposableTreeNode
import org.phoenixframework.liveview.ui.phx_components.PhxLiveView
import org.phoenixframework.liveview.ui.phx_components.paddingIfNotNull

class LazyColumnDTO private constructor(builder: Builder) :
    ComposableView(modifier = builder.modifier) {
    private val verticalArrangement: Arrangement.Vertical = builder.verticalArrangement
    private val horizontalAlignment: Alignment.Horizontal = builder.horizontalAlignment
    private val contentPadding: ImmutableMap<String, Int> = builder.contentPadding.toImmutableMap()
    private val reverseLayout: Boolean = builder.reverseLayout

    @Composable
    override fun Compose(
        composableNode: ComposableTreeNode?,
        paddingValues: PaddingValues?,
        pushEvent: PushEvent,
    ) {
        LazyColumn(
            modifier = modifier.paddingIfNotNull(paddingValues),
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            contentPadding = PaddingValues(
                (contentPadding[LazyComposableBuilder.LEFT] ?: 0).dp,
                (contentPadding[LazyComposableBuilder.TOP] ?: 0).dp,
                (contentPadding[LazyComposableBuilder.RIGHT] ?: 0).dp,
                (contentPadding[LazyComposableBuilder.BOTTOM] ?: 0).dp
            ),
            content = {
                items(
                    composableNode?.children ?: emptyArray(),
                    key = { item -> item.id },
                ) { item ->
                    PhxLiveView(item, pushEvent, composableNode, null, this)
                }
            },
        )
    }

    class Builder : LazyComposableBuilder() {
        var verticalArrangement: Arrangement.Vertical = Arrangement.Top
        var horizontalAlignment: Alignment.Horizontal = Alignment.Start

        fun verticalArrangement(verticalArrangement: String) = apply {
            this.verticalArrangement = when (verticalArrangement) {
                "top" -> Arrangement.Top
                "spaceEvenly" -> Arrangement.SpaceEvenly
                "spaceAround" -> Arrangement.SpaceAround
                "spaceBetween" -> Arrangement.SpaceBetween
                "bottom" -> Arrangement.Bottom
                "center" -> Arrangement.Center
                else -> Arrangement.spacedBy(verticalArrangement.toInt().dp)
            }
        }

        fun horizontalAlignment(horizontalAlignment: String) = apply {
            this.horizontalAlignment = when (horizontalAlignment) {
                "start" -> Alignment.Start
                "center" -> Alignment.CenterHorizontally
                "end" -> Alignment.End
                else -> Alignment.Start
            }
        }

        fun build() = LazyColumnDTO(this)
    }
}

object LazyColumnDtoFactory : ComposableViewFactory<LazyColumnDTO, LazyColumnDTO.Builder>() {
    override fun buildComposableView(
        attributes: Array<CoreAttribute>,
        pushEvent: PushEvent?,
        scope: Any?,
    ): LazyColumnDTO = attributes.fold(LazyColumnDTO.Builder()) { builder, attribute ->
        when (attribute.name) {
            "horizontalAlignment" -> builder.horizontalAlignment(attribute.value)
            "itemBottomPadding" -> builder.bottomPadding(attribute.value)
            "itemHorizontalPadding" -> builder.horizontalPadding(attribute.value)
            "itemLeftPadding" -> builder.leftPadding(attribute.value)
            "itemPadding" -> builder.lazyListItemPadding(attribute.value)
            "itemRightPadding" -> builder.rightPadding(attribute.value)
            "itemTopPadding" -> builder.topPadding(attribute.value)
            "itemVerticalPadding" -> builder.verticalPadding(attribute.value)
            "reverseLayout" -> builder.reverseLayout(attribute.value)
            "verticalArrangement" -> builder.verticalArrangement(attribute.value)
            else -> builder.processCommonAttributes(scope, attribute, pushEvent)
        } as LazyColumnDTO.Builder
    }.build()
}