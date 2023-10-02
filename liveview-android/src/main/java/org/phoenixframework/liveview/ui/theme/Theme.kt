package org.phoenixframework.liveview.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun LiveViewNativeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    themeData: Map<String, Any> = emptyMap(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = getColorScheme(darkTheme, dynamicColor, themeData),
        typography = typographyFromThemeData(
            themeData["typography"] as? Map<String, Any> ?: emptyMap()
        ),
        shapes = Shapes,
        content = content
    )
}

@Composable
private fun getColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean = false,
    themeData: Map<String, Any> = emptyMap(),
): ColorScheme {
    val colorsData = themeData["colors"] as? Map<String, Any> ?: emptyMap()
    val lightColors = colorsData["lightColors"] as? Map<String, Any> ?: emptyMap()
    val darkColors = colorsData["darkColors"] as? Map<String, Any> ?: emptyMap()
    return when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> colorSchemeFromThemeData(darkColors, true)
        else -> colorSchemeFromThemeData(lightColors, false)
    }
}