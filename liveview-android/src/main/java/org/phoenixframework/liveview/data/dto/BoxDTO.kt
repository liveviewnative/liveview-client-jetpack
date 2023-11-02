package org.phoenixframework.liveview.data.dto

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.phoenixframework.liveview.data.core.CoreAttribute
import org.phoenixframework.liveview.domain.base.ComposableBuilder
import org.phoenixframework.liveview.domain.base.ComposableBuilder.Companion.ATTR_SCROLL
import org.phoenixframework.liveview.domain.base.ComposableView
import org.phoenixframework.liveview.domain.base.ComposableViewFactory
import org.phoenixframework.liveview.domain.base.PushEvent
import org.phoenixframework.liveview.domain.extensions.optional
import org.phoenixframework.liveview.domain.factory.ComposableTreeNode
import org.phoenixframework.liveview.ui.phx_components.PhxLiveView
import org.phoenixframework.liveview.ui.phx_components.paddingIfNotNull

internal class BoxDTO private constructor(builder: Builder) :
    ComposableView(modifier = builder.modifier) {
    private val contentAlignment: Alignment = builder.contentAlignment
    private val propagateMinConstraints = builder.propagateMinConstraints
    private val hasVerticalScroll = builder.hasVerticalScrolling
    private val hasHorizontalScroll = builder.hasHorizontalScrolling

    @Composable
    override fun Compose(
        composableNode: ComposableTreeNode?,
        paddingValues: PaddingValues?,
        pushEvent: PushEvent,
    ) {
        Box(
            contentAlignment = contentAlignment,
            propagateMinConstraints = propagateMinConstraints,
            modifier = modifier
                .paddingIfNotNull(paddingValues)
                .optional(
                    hasVerticalScroll, Modifier.verticalScroll(rememberScrollState())
                )
                .optional(
                    hasHorizontalScroll, Modifier.horizontalScroll(rememberScrollState())
                ),
        ) {
            composableNode?.children?.forEach {
                PhxLiveView(it, pushEvent, composableNode, null, this)
            }
        }
    }

    internal class Builder : ComposableBuilder() {
        var contentAlignment: Alignment = Alignment.TopStart
            private set
        var propagateMinConstraints: Boolean = false
            private set

        /**
         * The default alignment inside the Box.
         *
         * ```
         * <Box contentAlignment="bottomEnd">...</Box>
         * ```
         * @param contentAlignment children's alignment inside the Box. Supported values:
         * `topStart`, `topCenter`, `topEnd`, `centerStart`, `center`, `centerEnd`, `bottomStart`,
         * `bottomCenter`, or `bottomEnd`.
         */
        fun contentAlignment(contentAlignment: String) = apply {
            this.contentAlignment = alignmentFromString(contentAlignment, Alignment.TopStart)
        }

        /**
         * Whether the incoming min constraints should be passed to content.
         *
         * ```
         * <Box propagateMinConstraints="true">...</Box>
         * ```
         * @param value true if the incoming min constraints should be passed to content, false
         * otherwise.
         */
        fun propagateMinConstraints(value: String) = apply {
            this.propagateMinConstraints = value.toBoolean()
        }

        fun build(): BoxDTO = BoxDTO(this)
    }
}

internal object BoxDtoFactory : ComposableViewFactory<BoxDTO, BoxDTO.Builder>() {
    /**
     * Creates a `BoxDTO` object based on the attributes of the input `Attributes` object.
     * Box co-relates to the Box composable
     * @param attributes the `Attributes` object to create the `BoxDTO` object from
     * @return a `BoxDTO` object based on the attributes of the input `Attributes` object
     */
    override fun buildComposableView(
        attributes: Array<CoreAttribute>,
        pushEvent: PushEvent?,
        scope: Any?,
    ): BoxDTO = attributes.fold(BoxDTO.Builder()) { builder, attribute ->
        when (attribute.name) {
            "contentAlignment" -> builder.contentAlignment(attribute.value)
            ATTR_SCROLL -> builder.scrolling(attribute.value)
            "propagateMinConstraints" -> builder.propagateMinConstraints(attribute.value)
            else -> builder.handleCommonAttributes(attribute, pushEvent, scope)
        } as BoxDTO.Builder
    }.build()
}