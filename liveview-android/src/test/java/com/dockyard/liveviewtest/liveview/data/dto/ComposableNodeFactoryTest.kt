package com.dockyard.liveviewtest.liveview.data.dto

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jsoup.nodes.Attributes
import org.jsoup.nodes.Element
import org.junit.Test
import org.phoenixframework.liveview.data.dto.*
import org.phoenixframework.liveview.domain.factory.ComposableNodeFactory
/*

class ComposableNodeFactoryTest {

    private val attributes = Attributes()

    @Test
    fun `test buildComposable with ComposableTypes column`() {

        val element = Element("column")
        val composableTreeNode = ComposableNodeFactory.buildComposable(element)

        assert(composableTreeNode.value is ColumnDTO)
    }

    @Test
    fun `test buildComposable with ComposableTypes row`() {
        val element = Element("row")
        val composableTreeNode = ComposableNodeFactory.buildComposable(element)

        assert(composableTreeNode.value is RowDTO)
    }

    @Test
    fun `test buildComposable with ComposableTypes lazyColumn`() {
        val element = Element("lazyColumn")
        val composableTreeNode = ComposableNodeFactory.buildComposable(element)

        assert(composableTreeNode.value is TextDTO)
    }

    @Test
    fun `test buildComposable with ComposableTypes lazyRow`() {
        val element = Element("lazyRow", )
        val composableTreeNode = ComposableNodeFactory.buildComposable(element)

        assert(composableTreeNode.value is TextDTO)
    }

    @Test
    fun `test buildComposable with ComposableTypes text`() {
        val element = Element("text", )
        val composableTreeNode = ComposableNodeFactory.buildComposable(element)

        assert(composableTreeNode.value is TextDTO)
    }

    @Test
    fun `test buildComposable with ComposableTypes card`() {
        val element = Element("card", )
        val composableTreeNode = ComposableNodeFactory.buildComposable(element)

        assert(composableTreeNode.value is CardDTO)
    }

    @Test
    fun `test buildComposable with ComposableTypes asyncImage`() {
        val element = Element("asyncImage")
        val composableTreeNode = ComposableNodeFactory.buildComposable(element)

        assert(composableTreeNode.value is AsyncImageDTO)
    }

    @Test
    fun `test buildComposable with default case`() {
        val element = Element("someOtherTag", )
        val composableTreeNode = ComposableNodeFactory.buildComposable(element)

        assert(composableTreeNode.value is TextDTO)
    }

    @Test
    fun `test buildCardNode`() {
        attributes.put("shape", "circle")
        attributes.put("background-color", "#ffffff")
        attributes.put("elevation", "5")
        */
/*  attributes.put("size", "45")
          attributes.put("height", "100")
          attributes.put("width", "200")*//*


        val composableView = ComposableNodeFactory.buildCardNode(attributes)

        assert(composableView is CardDTO)
        if (composableView is CardDTO){
            assert(composableView.shape == CircleShape)
            assert(composableView.backgroundColor == Color(0xFFFFFFF))
            assert(composableView.elevation == 5.dp)
            */
/*  assert(composableView. == "large")
              assert(composableView.height == "100")
              assert(composableView.width == "200")*//*

        }else{
            assert(false)
        }

    }

}*/
