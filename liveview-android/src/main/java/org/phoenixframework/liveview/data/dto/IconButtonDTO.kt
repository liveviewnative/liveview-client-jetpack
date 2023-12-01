package org.phoenixframework.liveview.data.dto

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import org.phoenixframework.liveview.data.constants.Attrs.attrColors
import org.phoenixframework.liveview.data.constants.Attrs.attrEnabled
import org.phoenixframework.liveview.data.constants.Attrs.attrPhxClick
import org.phoenixframework.liveview.data.constants.ColorAttrs.colorAttrContainerColor
import org.phoenixframework.liveview.data.constants.ColorAttrs.colorAttrContentColor
import org.phoenixframework.liveview.data.constants.ColorAttrs.colorAttrDisabledContainerColor
import org.phoenixframework.liveview.data.constants.ColorAttrs.colorAttrDisabledContentColor
import org.phoenixframework.liveview.data.core.CoreAttribute
import org.phoenixframework.liveview.data.mappers.JsonParser
import org.phoenixframework.liveview.domain.ThemeHolder.disabledContentAlpha
import org.phoenixframework.liveview.domain.base.ComposableBuilder
import org.phoenixframework.liveview.domain.base.ComposableView
import org.phoenixframework.liveview.domain.base.ComposableViewFactory
import org.phoenixframework.liveview.domain.base.PushEvent
import org.phoenixframework.liveview.domain.extensions.toColor
import org.phoenixframework.liveview.domain.factory.ComposableTreeNode
import org.phoenixframework.liveview.ui.phx_components.PhxLiveView

/**
 * Material Design standard icon button.
 * ```
 * <IconButton phx-click="iconButtonHandleAction">
 *   <Icon image-vector="filled:Add" />
 * </IconButton>
 * ```
 */
internal class IconButtonDTO private constructor(builder: Builder) :
    ComposableView(modifier = builder.modifier) {
    private val onClick: () -> Unit = builder.onClick
    private val enabled: Boolean = builder.enabled
    private val colors: ImmutableMap<String, String>? = builder.colors?.toImmutableMap()

    @Composable
    override fun Compose(
        composableNode: ComposableTreeNode?,
        paddingValues: PaddingValues?,
        pushEvent: PushEvent,
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            colors = getIconButtonColors(colors),
        ) {
            composableNode?.children?.forEach {
                PhxLiveView(it, pushEvent, composableNode, null)
            }
        }
    }

    @Composable
    private fun getIconButtonColors(colors: ImmutableMap<String, String>?): IconButtonColors {
        val defaultValue = IconButtonDefaults.iconButtonColors()
        return if (colors == null) {
            defaultValue
        } else {
            IconButtonDefaults.iconButtonColors(
                containerColor = colors[colorAttrContainerColor]?.toColor() ?: Color.Transparent,
                contentColor = colors[colorAttrContentColor]?.toColor()
                    ?: LocalContentColor.current,
                disabledContainerColor = colors[colorAttrDisabledContainerColor]?.toColor()
                    ?: Color.Transparent,
                disabledContentColor = colors[colorAttrDisabledContentColor]?.toColor()
                    ?: (colors[colorAttrContentColor]?.toColor() ?: LocalContentColor.current).copy(
                        alpha = disabledContentAlpha
                    ),
            )
        }
    }

    internal class Builder : ComposableBuilder() {
        var onClick: () -> Unit = {}
            private set
        var enabled: Boolean = true
            private set
        var colors: Map<String, String>? = null
            private set

        /**
         * Sets the event name to be triggered on the server when the button is clicked.
         *
         * ```
         * <IconButton phx-click="yourServerEventHandler">...</IconButton>
         * ```
         * @param event event name defined on the server to handle the button's click.
         * @param pushEvent function responsible to dispatch the server call.
         */
        fun onClick(event: String, pushEvent: PushEvent?) = apply {
            this.onClick = {
                pushEvent?.invoke(EVENT_TYPE_CLICK, event, "", null)
            }
        }

        /**
         * Defines if the button is enabled.
         *
         * ```
         * <IconButton enabled="true">...</IconButton>
         * ```
         * @param enabled true if the button is enabled, false otherwise.
         */
        fun enabled(enabled: String): Builder = apply {
            this.enabled = enabled.toBoolean()
        }

        /**
         * Set IconButton elevations.
         * ```
         * <IconButton
         *   colors="{'containerColor': '#FFFF0000', 'contentColor': '#FFFFFFFF'}">
         *   ...
         * </IconButton>
         * ```
         * @param colors an JSON formatted string, containing the button colors. The colors
         * supported keys are: `containerColor`, `contentColor`, `disabledContainerColor`, and
         * `disabledContentColor`.
         */
        fun colors(colors: String): Builder = apply {
            if (colors.isNotEmpty()) {
                try {
                    this.colors = JsonParser.parse(colors)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun build() = IconButtonDTO(this)
    }
}

internal object IconButtonDtoFactory :
    ComposableViewFactory<IconButtonDTO, IconButtonDTO.Builder>() {
    /**
     * Creates a `IconButtonDTO` object based on the attributes of the input `Attributes` object.
     * IconButtonDTO co-relates to the IconButton composable
     * @param attributes the `Attributes` object to create the `IconButtonDTO` object from
     * @return a `IconButtonDTO` object based on the attributes of the input `Attributes` object
     */
    override fun buildComposableView(
        attributes: Array<CoreAttribute>,
        pushEvent: PushEvent?,
        scope: Any?,
    ): IconButtonDTO = attributes.fold(
        IconButtonDTO.Builder()
    ) { builder, attribute ->
        when (attribute.name) {
            attrColors -> builder.colors(attribute.value)
            attrEnabled -> builder.enabled(attribute.value)
            attrPhxClick -> builder.onClick(attribute.value, pushEvent)
            else -> builder.handleCommonAttributes(attribute, pushEvent, scope)
        } as IconButtonDTO.Builder
    }.build()
}