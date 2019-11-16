package br.com.devsrsouza.eventkt.scopes

import br.com.devsrsouza.eventkt.EventScope

object GlobalEventScope : EventScope by LocalEventScope()