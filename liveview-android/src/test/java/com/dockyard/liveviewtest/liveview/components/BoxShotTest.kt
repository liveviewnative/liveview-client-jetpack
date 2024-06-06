package com.dockyard.liveviewtest.liveview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dockyard.liveviewtest.liveview.util.LiveViewComposableTest
import org.junit.Test
import org.phoenixframework.liveview.data.constants.AlignmentValues.bottomEnd
import org.phoenixframework.liveview.data.constants.AlignmentValues.bottomStart
import org.phoenixframework.liveview.data.constants.AlignmentValues.center
import org.phoenixframework.liveview.data.constants.AlignmentValues.topEnd
import org.phoenixframework.liveview.data.constants.AlignmentValues.topStart
import org.phoenixframework.liveview.data.constants.Attrs.attrAlign
import org.phoenixframework.liveview.data.constants.Attrs.attrImageVector
import org.phoenixframework.liveview.data.constants.Attrs.attrStyle
import org.phoenixframework.liveview.data.constants.ModifierNames.modifierBackground
import org.phoenixframework.liveview.data.constants.ModifierNames.modifierHeight
import org.phoenixframework.liveview.data.constants.ModifierNames.modifierSize
import org.phoenixframework.liveview.data.constants.ModifierNames.modifierWidth
import org.phoenixframework.liveview.data.constants.ModifierTypes.typeColor
import org.phoenixframework.liveview.data.constants.ModifierTypes.typeDp
import org.phoenixframework.liveview.data.constants.SystemColorValues.Blue
import org.phoenixframework.liveview.data.constants.SystemColorValues.Red
import org.phoenixframework.liveview.domain.base.ComposableTypes.box
import org.phoenixframework.liveview.domain.base.ComposableTypes.icon
import org.phoenixframework.liveview.domain.base.ComposableTypes.text

class BoxShotTest : LiveViewComposableTest() {
    @Test
    fun boxWithChildrenAligned1Test() {
        compareNativeComposableWithTemplate(
            nativeComposable = {
                Box(
                    Modifier
                        .size(100.dp)
                        .background(Color.Red)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Android,
                        contentDescription = "",
                        modifier = Modifier.align(
                            Alignment.TopStart
                        )
                    )
                    Text(text = "Text", Modifier.align(Alignment.Center))
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "",
                        modifier = Modifier.align(
                            Alignment.BottomEnd
                        )
                    )
                }
            },
            template = """
              <$box $attrStyle="$modifierSize($typeDp(100));$modifierBackground($typeColor.$Red)">
                <$icon $attrImageVector="filled:Android" $attrAlign="$topStart"/>
                <$text $attrAlign="$center">Text</$text>
                <$icon $attrImageVector="filled:Share" $attrAlign="$bottomEnd"/>
              </$box>                
            """
        )
    }

    @Test
    fun boxWithChildrenAligned2Test() {
        compareNativeComposableWithTemplate(
            nativeComposable = {
                Box(
                    Modifier
                        .width(150.dp)
                        .height(100.dp)
                        .background(Color.Blue)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Android,
                        contentDescription = "",
                        modifier = Modifier.align(
                            Alignment.TopEnd
                        )
                    )
                    Text(text = "Text Center", Modifier.align(Alignment.Center))
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "",
                        modifier = Modifier.align(
                            Alignment.BottomStart
                        )
                    )
                }
            },
            template = """
              <$box $attrStyle="$modifierWidth($typeDp(150));$modifierHeight($typeDp(100));$modifierBackground($typeColor.$Blue)">
                <$icon $attrImageVector="filled:Android" $attrAlign="$topEnd"/>
                <$text $attrAlign="$center">Text Center</$text>
                <$icon $attrImageVector="filled:Share" $attrAlign="$bottomStart"/>
              </$box>                
            """
        )
    }
}