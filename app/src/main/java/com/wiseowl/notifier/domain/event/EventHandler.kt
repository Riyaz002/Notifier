package com.wiseowl.notifier.domain.event

import com.wiseowl.notifier.ui.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

object EventHandler {
    private val channel: Channel<Event> = Channel{  }
    private var subscribers: ((Event) -> Unit)? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        coroutineScope.launch {
            channel.consumeAsFlow().collectLatest{ data ->
                MainScope().launch {
                    subscribers?.invoke(data)
                }
            }
        }
    }

    fun subscribe(onEvent: (Event) -> Unit) {
        subscribers = onEvent
    }

    fun send(event: Event){
        coroutineScope.launch {
            channel.send(event)
        }
    }
}