package org.phoenixframework.liveview.data.dto

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import org.phoenixframework.liveview.data.constants.Attrs.attrEnabled
import org.phoenixframework.liveview.data.constants.Attrs.attrPhxClick
import org.phoenixframework.liveview.data.constants.Attrs.attrSelected
import org.phoenixframework.liveview.data.constants.Attrs.attrSelectedContentColor
import org.phoenixframework.liveview.data.constants.Attrs.attrUnselectedContentColor
import org.phoenixframework.liveview.data.constants.Templates
import org.phoenixframework.liveview.data.core.CoreAttribute
import org.phoenixframework.liveview.domain.base.ComposableBuilder
import org.phoenixframework.liveview.domain.base.ComposableView
import org.phoenixframework.liveview.domain.base.ComposableViewFactory
import org.phoenixframework.liveview.domain.base.PushEvent
import org.phoenixframework.liveview.domain.extensions.toColor
import org.phoenixframework.liveview.domain.factory.ComposableTreeNode
import org.phoenixframework.liveview.ui.phx_components.PhxLiveView

/**
 * Material Design tab.
 * Usually, this component is declared inside of a TabRow.
 */
internal class TabDTO private constructor(builder: Builder) :
    ComposableView(modifier = builder.modifier) {
    private val onClick = builder.onClick
    private val value = builder.value
    private val selected = builder.selected
    private val enabled = builder.enabled
    private val selectedContentColor = builder.selectedContentColor
    private val unselectedContentColor = builder.unselectedContentColor

    @Composable
    override fun Compose(
        composableNode: ComposableTreeNode?, paddingValues: PaddingValues?, pushEvent: PushEvent
    ) {
        val text = remember(composableNode?.children) {
            composableNode?.children?.find { it.node?.template == Templates.templateText }
        }
        val icon = remember(composableNode?.children) {
            composableNode?.children?.find { it.node?.template == Templates.templateIcon }
        }
        val selectedColor = selectedContentColor ?: LocalContentColor.current
        Tab(
            selected = selected,
            onClick = onClickFromString(pushEvent, onClick, value?.toString() ?: ""),
            modifier = modifier,
            enabled = enabled,
            text = text?.let {
                {
                    PhxLiveView(it, pushEvent, composableNode, null)
                }
            },
            icon = icon?.let {
                {
                    PhxLiveView(it, pushEvent, composableNode, null)
                }
            },
            selectedContentColor = selectedColor,
            unselectedContentColor = unselectedContentColor ?: selectedColor,
        )
    }

    internal class Builder : ComposableBuilder() {
        var onClick: String = ""
            private set
        var selected: Boolean = false
            private set
        var enabled: Boolean = true
            private set
        var selectedContentColor: Color? = null
            private set
        var unselectedContentColor: Color? = null
            private set

        /**
         * Sets the event name to be triggered on the server when the tab is clicked.
         *
         * ```
         * <Tab phx-click="yourServerEventHandler">...</Tab>
         * ```
         * @param event event name defined on the server to handle the button's click.
         */
        fun onClick(event: String) = apply {
            this.onClick = event
        }

        /**
         * A boolean value indicating if the component is selected or not.
         *
         * ```
         * <Tab selected="true" />
         * ```
         * @param selected true if the component is selected, false otherwise.
         */
        fun selected(selected: String) = apply {
            this.selected = selected.toBoolean()
        }

        /**
         * A boolean value indicating if the component is enabled or not.
         *
         * ```
         * <Tab enabled="true" />
         * ```
         * @param enabled true if the component is enabled, false otherwise.
         */
        fun enabled(enabled: String) = apply {
            this.enabled = enabled.toBoolean()
        }

        /**
         * The color used for the background of this tab row.
         * ```
         * <Tab selected-content-color="#FFFFFFFF" />
         * ```
         * @param contentColor the background color in AARRGGBB format.
         */
        fun selectedContentColor(contentColor: String) = apply {
            this.selectedContentColor = contentColor.toColor()
        }

        /**
         * The preferred color for content inside this tab row.
         * ```
         * <Tab unselected-content-color="#FF000000" />
         * ```
         * @param contentColor the content color in AARRGGBB format.
         */
        fun unselectedContentColor(contentColor: String) = apply {
            this.unselectedContentColor = contentColor.toColor()
        }

        fun build() = TabDTO(this)
    }
}

internal object TabDtoFactory : ComposableViewFactory<TabDTO, TabDTO.Builder>() {
    /**
     * Creates a `TabDTO` object based on the attributes of the input `Attributes` object.
     * TabDTO co-relates to the Tab composable.
     * @param attributes the `Attributes` object to create the `TabDTO` object from
     * @return a `TabDTO` object based on the attributes of the input `Attributes` object
     **/
    override fun buildComposableView(
        attributes: Array<CoreAttribute>, pushEvent: PushEvent?, scope: Any?
    ): TabDTO = attributes.fold(TabDTO.Builder()) { builder, attribute ->
        when (attribute.name) {
            attrPhxClick -> builder.onClick(attribute.value)
            attrSelected -> builder.selected(attribute.value)
            attrEnabled -> builder.enabled(attribute.value)
            attrSelectedContentColor -> builder.selectedContentColor(attribute.value)
            attrUnselectedContentColor -> builder.unselectedContentColor(attribute.value)
            else -> builder.handleCommonAttributes(attribute, pushEvent, scope)
        } as TabDTO.Builder
    }.build()
}