package com.wiseowl.notifier.domain.exception

import com.wiseowl.notifier.domain.event.Event

class UnhandledEventException(event: Event): Exception(){
    override val message: String = "Event not handled: ${event::class.simpleName}"
}