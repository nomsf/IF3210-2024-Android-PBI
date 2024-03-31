package com.example.myapplication.util

import android.util.Log

object EventBus {

    private var listeners: MutableMap<String, MutableList<() -> Unit>> = mutableMapOf()

    fun subscribe(event: String, listener: () -> Unit) {
        if (listeners.containsKey(event)) {
            // add a new listener to an existing event
            listeners[event]?.add(listener)
        } else {
            // add a new event with a new listener
            listeners[event] = mutableListOf(listener)
        }
    }

    fun publish(event: String) {
        if (listeners.containsKey(event)) {
            // call all listeners for the event
            listeners[event]?.forEach { it() }
        } else {
            // no listeners for the event
            Log.d("Development","No listeners for event: $event")
        }
    }
}