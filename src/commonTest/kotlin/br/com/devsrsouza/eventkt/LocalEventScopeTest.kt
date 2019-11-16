package br.com.devsrsouza.eventkt

import br.com.devsrsouza.eventkt.scopes.LocalEventScope
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test

class LocalEventScopeTest {

    @Test
    fun `should listen to publish of a String`() {
        val localEventScope = LocalEventScope()

        val onReceiveMock = mockk<(String) -> Unit>()
        every { onReceiveMock.invoke(any<String>()) } returns Unit

        localEventScope.listen<String>(this, onReceiveMock)

        localEventScope.publish("test string")

        verify { onReceiveMock.invoke(any<String>()) }
    }

    @Test
    fun `should listen to publish of a data class`() {
        data class TestDataClass(val x: String, val y: Int)

        val localEventScope = LocalEventScope()

        val onReceiveMock = mockk<(TestDataClass) -> Unit>()
        every { onReceiveMock.invoke(any<TestDataClass>()) } returns Unit

        localEventScope.listen<TestDataClass>(this, onReceiveMock)

        localEventScope.publish(TestDataClass("test x", 5))

        verify { onReceiveMock.invoke(any<TestDataClass>()) }
    }
}