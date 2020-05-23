package br.com.devsrsouza.eventkt

import br.com.devsrsouza.eventkt.scopes.LocalEventScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalEventScopeTest {

    @Test
    fun `should listen to publish of a String`() = runBlockingTest {
        val localEventScope = LocalEventScope()

        val receiveList = mutableListOf<String>()

        localEventScope.listen<String>()
            .onEach { receiveList.add(it) }
            .launchIn(TestCoroutineScope())

        assertEquals(listOf<String>(), receiveList)

        localEventScope.publish("test string")

        assertEquals(listOf<String>("test string"), receiveList)
    }

    @Test
    fun `should listen to publish of a data class`() {
        data class TestDataClass(val x: String, val y: Int)

        val localEventScope = LocalEventScope()

        val receiveList = mutableListOf<TestDataClass>()

        localEventScope.listen<TestDataClass>()
            .onEach { receiveList.add(it) }
            .launchIn(TestCoroutineScope())

        assertEquals(listOf<TestDataClass>(), receiveList)

        localEventScope.publish(TestDataClass("test x", 5))

        assertEquals(listOf<TestDataClass>(TestDataClass("test x", 5)), receiveList)
    }

    @Test
    fun `should listen data class and should not trigger when String is publish`() {
        data class TestDataClass(val x: String, val y: Int)

        val localEventScope = LocalEventScope()

        val receiveList = mutableListOf<Any>()

        localEventScope.listen<TestDataClass>()
            .onEach { receiveList.add(it) }
            .launchIn(TestCoroutineScope())

        assertEquals(listOf<Any>(), receiveList)

        localEventScope.publish("Test")

        assertEquals(listOf<Any>(), receiveList)
    }
}
