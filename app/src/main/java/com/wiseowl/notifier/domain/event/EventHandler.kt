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


/**
 * PubSub model to act on global events
 * Subscribe it from a higher level like [MainActivity] to listen to events.
 *
 * One can send [Event] from any part of the app using [EventHandler.send].
 */
object EventHandler {
    private val channel: Channel<Event> = Channel{  }
    private var subscriber: ((Event) -> Unit)? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        coroutineScope.launch {
            channel.consumeAsFlow().collectLatest{ data ->
                MainScope().launch {
                    subscriber?.invoke(data)
                }
            }
        }
    }

    /**
     * Subscribe for the events that are send using [EventHandler.send]
     *
     * Note: Only one subscriber can subscribe for the [Event]s at a time. Since, [Event]s are global, it makes more sense to have only single higher level handler like [MainActivity].
     */
    fun subscribe(onEvent: (Event) -> Unit) {
        subscriber = onEvent
    }


    /**
     * Used to send global event from any part of the app.
     */
    fun send(event: Event){
        coroutineScope.launch {
            channel.send(event)
        }
    }
}