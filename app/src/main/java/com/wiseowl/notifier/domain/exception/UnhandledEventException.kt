package com.wiseowl.notifier.domain.exception

class UnhandledEventException(eventName: String): Exception(){
    override val message: String = "Event not handled: $eventName"
}