package org.phoenixframework.liveview.data.mappers.modifiers

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import org.phoenixframework.liveview.data.constants.ModifierArgs.argFraction
import org.phoenixframework.liveview.data.constants.ModifierArgs.argHeight
import org.phoenixframework.liveview.data.constants.ModifierArgs.argIntrinsicSize
import org.phoenixframework.liveview.data.constants.ModifierArgs.argMatchHeightConstraintFirst
import org.phoenixframework.liveview.data.constants.ModifierArgs.argMax
import org.phoenixframework.liveview.data.constants.ModifierArgs.argMin
import org.phoenixframework.liveview.data.constants.ModifierArgs.argRatio
import org.phoenixframework.liveview.data.constants.ModifierArgs.argSize
import org.phoenixframework.liveview.data.constants.ModifierArgs.argWidth

fun Modifier.aspectRatioFromStyle(arguments: List<ModifierDataAdapter.ArgumentData>): Modifier {
    val params = argsOrNamedArgs(arguments)
    val ratio = argOrNamedArg(params, argRatio, 0)?.floatValue
    val matchConstraints = argOrNamedArg(params, argMatchHeightConstraintFirst, 1)?.booleanValue
    return ratio?.let {
        this.then(Modifier.aspectRatio(it, matchConstraints ?: false))
    } ?: this
}

fun Modifier.fillMaxHeightFromStyle(arguments: List<ModifierDataAdapter.ArgumentData>): Modifier {
    val args = argsOrNamedArgs(arguments)
    val fraction = argOrNamedArg(args, argFraction, 0)?.floatValue ?: 1f
    return this.then(Modifier.fillMaxHeight(fraction))
}

fun Modifier.fillMaxSizeFromStyle(arguments: List<ModifierDataAdapter.ArgumentData>): Modifier {
    val args = argsOrNamedArgs(arguments)
    val fraction = argOrNamedArg(args, argFraction, 0)?.floatValue ?: 1f
    return this.then(Modifier.fillMaxSize(fraction))
}

fun Modifier.fillMaxWidthFromStyle(arguments: List<ModifierDataAdapter.ArgumentData>): Modifier {
    val args = argsOrNamedArgs(arguments)
    val fraction = argOrNamedArg(args, argFraction, 0)?.floatValue ?: 1f
    return this.then(Modifier.fillMaxWidth(fraction))
}

fun Modifier.heightFromStyle(arguments: List<ModifierDataAdapter.ArgumentData>): Modifier {
    val args = argsOrNamedArgs(arguments)
    val height = argOrNamedArg(args, argHeight, 0)?.let { dpFromStyle(it) }
    if (height != null) {
        return this.then(Modifier.height(height))
    }
    val intrinsicSize =
        argOrNamedArg(args, argIntrinsicSize, 0)?.let { intrinsicSizeFromArgument(it) }
    if (intrinsicSize != null) {
        return this.then(Modifier.height(intrinsicSize = intrinsicSize))
    }
    return this
}

fun Modifier.heightInFromStyle(arguments: List<ModifierDataAdapter.ArgumentData>): Modifier {
    val args = argsOrNamedArgs(arguments)
    val min = argOrNamedArg(args, argMin, 0)?.let { dpFromStyle(it) }
    val max = argOrNamedArg(args, argMax, 1)?.let { dpFromStyle(it) }
    return this.then(
        Modifier.heightIn(
            min = min ?: Dp.Unspecified,
            max = max ?: Dp.Unspecified
        )
    )
}

fun Modifier.matchParentSizeFromStyle(scope: Any?): Modifier {
    return when (scope) {
        is BoxScope -> {
            scope.run {
                this@matchParentSizeFromStyle.then(
                    Modifier.matchParentSize()
                )
            }
        }

        else -> this
    }
}

fun Modifier.sizeFromStyle(arguments: List<ModifierDataAdapter.ArgumentData>): Modifier {
    val params = argsOrNamedArgs(arguments)

    return when (params.size) {
        1 -> {
            argOrNamedArg(params, argSize, 0)?.let { dpFromStyle(it) }?.let {
                this.then(Modifier.size(it))
            } ?: this
        }

        2 -> {
            val width = argOrNamedArg(params, argWidth, 0)?.let { dpFromStyle(it) }
            val height = argOrNamedArg(params, argHeight, 1)?.let { dpFromStyle(it) }
            if (width != null && height != null)
                this.then(Modifier.size(width, height))
            else
                this
        }

        else -> this
    }
}

fun Modifier.widthFromStyle(arguments: List<ModifierDataAdapter.ArgumentData>): Modifier {
    val args = argsOrNamedArgs(arguments)
    val height = argOrNamedArg(args, argWidth, 0)?.let { dpFromStyle(it) }
    if (height != null) {
        return this.then(Modifier.width(height))
    }
    val intrinsicSize =
        argOrNamedArg(args, argIntrinsicSize, 0)?.let { intrinsicSizeFromArgument(it) }
    if (intrinsicSize != null) {
        return this.then(Modifier.width(intrinsicSize = intrinsicSize))
    }
    return this
}

fun Modifier.widthInFromStyle(arguments: List<ModifierDataAdapter.ArgumentData>): Modifier {
    val args = argsOrNamedArgs(arguments)
    val min = argOrNamedArg(args, argMin, 0)?.let { dpFromStyle(it) }
    val max = argOrNamedArg(args, argMax, 1)?.let { dpFromStyle(it) }
    return this.then(
        Modifier.widthIn(
            min = min ?: Dp.Unspecified,
            max = max ?: Dp.Unspecified
        )
    )
}