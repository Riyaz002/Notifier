package com.wiseowl.notifier.domain.event

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

open class Event<T> {
    private val channel: Channel<T> = Channel{  }
    private val subscribers = hashSetOf<(T) -> Unit>()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        coroutineScope.launch {
            channel.consumeAsFlow().collectLatest{ data ->
                subscribers.forEach {
                    it.invoke(data)
                }
            }
        }
    }

    fun subscribe(onEvent: (T) -> Unit) {
        subscribers.add(onEvent)
    }

    fun send(data: T){
        coroutineScope.launch {
            channel.send(data)
        }
    }
}