package org.phoenixframework.liveview.ui.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableMap
import org.phoenixframework.liveview.data.constants.Attrs.attrClass
import org.phoenixframework.liveview.data.constants.Attrs.attrPhxClick
import org.phoenixframework.liveview.data.constants.Attrs.attrPhxValue
import org.phoenixframework.liveview.data.constants.Attrs.attrPhxValueNamed
import org.phoenixframework.liveview.data.constants.Attrs.attrStyle
import org.phoenixframework.liveview.data.core.CoreAttribute
import org.phoenixframework.liveview.ui.modifiers.ModifiersParser.fromStyleName
import org.phoenixframework.liveview.ui.base.ComposableBuilder.Companion.KEY_PHX_VALUE
import org.phoenixframework.liveview.domain.data.ComposableTreeNode
import org.phoenixframework.liveview.ui.composables.onClickFromString

/**
 *  A `ComposableView` is the parent class of all components. Subclasses must implement the
 *  `Composable` function in order to call the real composable. The `modifier` param and all
 *  necessary params for the actual component implementation should be provided by a subclass of
 *  `ComposableBuilder`. In order to make a component available, a subclass of
 *  `ComposableViewFactory` must be implemented, the it must be registered on `ComposableRegistry`
 *  object informing the respective tag for the composable.
 */
abstract class ComposableView<CP : ComposableProperties>(protected open val props: CP) {

    @Composable
    abstract fun Compose(
        composableNode: ComposableTreeNode?, paddingValues: PaddingValues?, pushEvent: PushEvent
    )

    // This function is used to merge/join "on changed" values with the component value(s)
    // (phx-value and phx-value-*). For example, when a checkbox changes its checked state, the new
    // checked value (true/false) and the checkbox values (phx-value/phx-value-*) are sent to server
    protected fun mergeValueWithPhxValue(key: String, value: Any): Any {
        val currentPhxValue = props.commonProps.phxValue
        return if (currentPhxValue == null) {
            if (key == KEY_PHX_VALUE) {
                value
            } else {
                mapOf(key to value)
            }
        } else if (props.commonProps.value.size == 1 && props.commonProps.value.containsKey(
                KEY_PHX_VALUE
            )
        ) {
            if (key == KEY_PHX_VALUE) {
                value
            } else {
                val newMap = props.commonProps.value.toMutableMap()
                newMap[key] = value
                newMap
            }
        } else {
            val newMap = props.commonProps.value.toMutableMap()
            newMap[key] = value
            newMap
        }
    }
}

interface ComposableProperties {
    val commonProps: CommonComposableProperties
}

@Stable
data class CommonComposableProperties(
    val modifier: Modifier,
    val value: ImmutableMap<String, Any>
) {
    val phxValue: Any?
        get() = if (value.isEmpty())
            null
        else if (value.size == 1 && value.containsKey(KEY_PHX_VALUE)) {
            value[KEY_PHX_VALUE]
        } else {
            value
        }
}

/**
 *  A `ComposableBuilder` is responsible to handle attributes declared in the LiveView tags. This
 *  class must parse and convert the `CoreAttribute` value in a corresponding property or `Modifier`
 *  to be used in the `ComposableView` implementation. All the required information required by a
 *  `ComposableView` must be provided by the respective `ComposableBuilder`.
 */
abstract class ComposableBuilder {
    var commonProps = CommonComposableProperties(
        modifier = Modifier,
        value = persistentMapOf()
    )
        private set

    private fun setStyleFromAttr(style: String, scope: Any?, pushEvent: PushEvent?) = apply {
        val modifier = this.commonProps.modifier
        this.commonProps = this.commonProps.copy(
            modifier = style
                .split(";")
                .map { it.trim() }
                .fold(modifier) { acc, modifierKey ->
                    acc.then(Modifier.fromStyleName(modifierKey, scope, pushEvent))
                }
        )
    }

    /**
     * Sets the event name to be triggered on the server when the composable is clicked.
     *
     * ```
     * <Composable phx-click="yourServerEventHandler" />
     * ```
     * @param event event name defined on the server to handle the composable's click.
     * @param pushEvent function responsible to dispatch the server call.
     */
    private fun setPhxClickFromAttr(event: String, pushEvent: PushEvent?) = apply {
        val modifier = this.commonProps.modifier
        this.commonProps = this.commonProps.copy(
            modifier = modifier.then(
                Modifier.clickable {
                    onClickFromString(pushEvent, event, this.commonProps.phxValue).invoke()
                }
            )
        )
    }

    /**
     * Sets the phx-value binding.
     *
     * ```
     * <Composable phx-value="someValue" />
     * ```
     * @param value event name defined on the server to handle the composable's click.
     */
    internal fun setPhxValueFromAttr(attributeName: String, value: Any) = apply {
        if (attributeName == attrPhxValue) {
            val newMap = this.commonProps.value.toMutableMap()
            newMap[KEY_PHX_VALUE] = value
            this.commonProps = this.commonProps.copy(value = newMap.toImmutableMap())
        } else if (attributeName.startsWith(attrPhxValueNamed)) {
            val phxValueKey = attributeName.substring(attrPhxValueNamed.length)
            val newMap = this.commonProps.value.toMutableMap()
            newMap[phxValueKey] = value
            this.commonProps = this.commonProps.copy(value = newMap.toImmutableMap())
        }
    }

    private fun setClassFromAttr(string: String, scope: Any?, pushEvent: PushEvent?) = apply {
        val modifier = this.commonProps.modifier
        this.commonProps = this.commonProps.copy(
            modifier = modifier.then(Modifier.fromStyleName(string, scope, pushEvent))
        )
    }

    /**
     * Handle the properties that are common for most of composables.
     * @param attribute a `CoreAttribute` to be handled.
     * @param pushEvent function responsible to dispatch the server call.
     * @param scope some attributes are composable specific, the scope determine what parent
     * composable (e.g.: `Column`, `Row`, `Box`).
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun handleCommonAttributes(
        attribute: CoreAttribute,
        pushEvent: PushEvent?,
        scope: Any?,
    ): ComposableBuilder {
        when (attribute.name) {
            attrClass -> setClassFromAttr(attribute.value, scope, pushEvent)
            attrPhxClick -> setPhxClickFromAttr(attribute.value, pushEvent)
            attrPhxValue -> setPhxValueFromAttr(attrPhxValue, attribute.value)
            attrStyle -> setStyleFromAttr(attribute.value, scope, pushEvent)
            else ->
                if (attribute.name.startsWith(attrPhxValueNamed)) {
                    setPhxValueFromAttr(attribute.name, attribute.value)
                }
        }
        return this
    }

    companion object {
        internal const val EVENT_TYPE_CHANGE = "change"
        internal const val EVENT_TYPE_CLICK = "click"
        internal const val EVENT_TYPE_DOUBLE_CLICK = "double-click"
        internal const val EVENT_TYPE_FOCUS_CHANGED = "focus-changed"
        internal const val EVENT_TYPE_FOCUS_EVENT = "focus-event"
        internal const val EVENT_TYPE_KEY_UP = "keyup"
        internal const val EVENT_TYPE_LONG_CLICK = "long-click"
        internal const val EVENT_TYPE_BLUR = "blur"
        internal const val EVENT_TYPE_SUBMIT = "submit"

        internal const val KEY_PHX_VALUE = "value"
    }
}

/**
 * A `ComposableViewFactory` is responsible to create a `ComposableView` using a list of attributes.
 */
abstract class ComposableViewFactory<CV : ComposableView<*>> {

    /**
     * Create a new instance of a `ComposableView`. Subclasses of this class must override this
     * method and handle specific attributes. Common attributes are handled by the
     * `handleCommonAttributes` declared in the `ComposableBuilder` class and should be called.
     * @param attributes a list of `CoreAttribute` to be handled.
     * @param pushEvent function responsible to dispatch the server call.
     * @param scope some attributes are composable specific, the scope determine what parent
     * composable (e.g.: `Column`, `Row`, `Box`).
     */
    abstract fun buildComposableView(
        attributes: ImmutableList<CoreAttribute>,
        pushEvent: PushEvent?,
        scope: Any?
    ): CV

    /**
     * Subclasses of ComposableViewFactory can register subtags specific for a particular component.
     * See ComposableRegistry and ComposableNodeFactory for more details.
     */
    open fun subTags(): Map<String, ComposableViewFactory<*>> = emptyMap()
}