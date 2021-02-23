package com.zaeem.filesdownloader.data

sealed class State
object NOT_STARTED : State()
object STARTED : State()
object COMPLETED : State()