package com.wiseowl.notifier.ui.navigation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

object Navigator {
    private val channels = Channel<Action> {  }
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var subscriber: ((Action) -> Unit)? = null
    init {
        coroutineScope.launch {
            channels.consumeAsFlow().collect{ screen ->
                coroutineScope.launch(Dispatchers.Main) {
                    subscriber?.invoke(screen)
                }
            }
        }
    }

    fun navigate(action: Action){
        coroutineScope.launch {
            channels.send(action)
        }
    }

    fun popBackStack(action: Action){
        coroutineScope.launch {
            channels.send(action)
        }
    }

    fun observe(onNavigate: (Action) -> Unit){
        subscriber = onNavigate
    }
}