package org.phoenixframework.liveview.data.mappers.modifiers

import androidx.compose.foundation.progressSemantics
import androidx.compose.ui.Modifier
import org.phoenixframework.liveview.data.constants.ModifierArgs.argSteps
import org.phoenixframework.liveview.data.constants.ModifierArgs.argValue
import org.phoenixframework.liveview.data.constants.ModifierArgs.argValueRange

fun Modifier.progressSemanticsFromStyle(arguments: List<ModifierDataAdapter.ArgumentData>): Modifier {
    val args = argsOrNamedArgs(arguments)
    return if (args.isEmpty()) {
        this.then(Modifier.progressSemantics())
    } else {
        val value = argOrNamedArg(args, argValue, 0)?.floatValue
        if (value != null) {
            val valueRange =
                argOrNamedArg(args, argValueRange, 1)?.let { floatRangeFromArgument(it) }
            val steps = argOrNamedArg(args, argSteps, 2)?.intValue
            this.then(
                Modifier.progressSemantics(
                    value = value,
                    valueRange = valueRange ?: 0f..1f,
                    steps = steps ?: 0
                )
            )
        } else this
    }
}