package com.dockyard.liveviewtest.liveview.util

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.dockyard.liveviewtest.liveview.runner.LiveViewTestRunner
import com.karumi.shot.ScreenshotTest
import org.junit.Rule
import org.phoenixframework.liveview.domain.LiveViewCoordinator
import org.phoenixframework.liveview.ui.phx_components.PhxLiveView

abstract class LiveViewComposableTest : ScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    fun compareNativeComposableWithTemplate(
        nativeComposable: @Composable () -> Unit,
        template: String,
        testTag: String? = null,
        delayBeforeScreenshot: Long = 0,
        coordinator: LiveViewCoordinator = LiveViewCoordinator(
            httpBaseUrl = "",
            wsBaseUrl = "",
            onNavigate = { _, _ -> }
        )
    ) {
        composeRule.setContent {
            val state by coordinator.composableTree.collectAsState()
            if (LiveViewTestRunner.isRecording) {
                nativeComposable()
            } else {
                val json = "{\"s\": [\"$template\"]}"
                coordinator.parseTemplate(json)
                Log.d("NGVL", "json=[$json]")
                if (state.children.isNotEmpty()) {
                    PhxLiveView(
                        composableNode = state.children.first(),
                        pushEvent = coordinator::pushEvent
                    )
                }
            }
        }

        if (delayBeforeScreenshot > 0) {
            Thread.sleep(delayBeforeScreenshot)
        }

        // https://github.com/pedrovgs/Shot/issues/305
        if (testTag != null)
            compareScreenshot(composeRule.onNodeWithTag(testTag, useUnmergedTree = true))
        else
            compareScreenshot(composeRule)
    }

    fun String.templateToTest() =
        this.trimIndent().trimMargin().trimEnd().replace("\"", "\\\"").lines().joinToString("")
}
