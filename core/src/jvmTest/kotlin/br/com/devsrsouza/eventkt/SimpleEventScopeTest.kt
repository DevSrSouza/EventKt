package br.com.devsrsouza.eventkt

import br.com.devsrsouza.eventkt.scopes.LocalEventScope
import br.com.devsrsouza.eventkt.scopes.asSimple
import br.com.devsrsouza.eventkt.scopes.listen
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleEventScopeTest {

    @Test
    fun `should listen to publish of a String in simple scope`() = runBlockingTest {
        val simpleScope = LocalEventScope().asSimple()
        val coroutineScope = TestCoroutineScope()

        val receiveList = mutableListOf<String>()

        simpleScope.listen<String>(this, coroutineScope) {
            receiveList.add(it)
        }

        assertEquals(listOf<String>(), receiveList)

        simpleScope.publish("test string")

        assertEquals(listOf<String>("test string"), receiveList)
    }
}