package br.com.devsrsouza.eventkt

import br.com.devsrsouza.eventkt.scopes.LocalEventScope
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlin.test.Test

class LocalEventScopeTest {

    @Test
    fun `should listen to publish of a String`() {
        val localEventScope = LocalEventScope()

        val onReceiveMock = mockk<suspend (String) -> Unit>()
        coEvery { onReceiveMock.invoke(any<String>()) } returns Unit

        localEventScope.listen<String>(this, Dispatchers.Default, onReceiveMock)

        localEventScope.publish("test string")

        coVerify { onReceiveMock.invoke(any()) }
    }

    @Test
    fun `should listen to publish of a data class`() {
        data class TestDataClass(val x: String, val y: Int)

        val localEventScope = LocalEventScope()

        val onReceiveMock = mockk<suspend (TestDataClass) -> Unit>()
        coEvery { onReceiveMock.invoke(any<TestDataClass>()) } returns Unit

        localEventScope.listen<TestDataClass>(this, Dispatchers.Default, onReceiveMock)

        localEventScope.publish(TestDataClass("test x", 5))

        coVerify { onReceiveMock.invoke(any<TestDataClass>()) }
    }
}