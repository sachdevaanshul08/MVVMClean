package com.clean.data.utils.executors

import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 *  Thread pools for the application.
 *  Here Coroutine is taking care of switching the thread based the task performed with in the @scope
 *  passed into it
 *
 */
@Singleton
class AppExecutors @Inject constructor() {

    private lateinit var scope: CoroutineScope

    /**
     *
     *
     * @param scope
     */
    fun injectScope(scope: CoroutineScope) {
        this.scope = scope
    }

    private fun checkScope() {
        if (!scope.isActive) throw ScopeInvalidException("Scope is not valid")
    }

    fun networkIo(task: () -> Unit) {
        checkScope()
        scope.launch {
            performNetworkIoOperations(task)
        }
    }


    fun computation(task: () -> Unit) {
        checkScope()
        scope.launch {
            performComputationOperations(task)
        }
    }

    fun mThread(task: () -> Unit) {
        scope.launch {
            withContext(Dispatchers.Main) {
                task.invoke()
            }
        }
    }

    private suspend fun performNetworkIoOperations(task: () -> Unit) {
        withContext(Dispatchers.IO) {
            task.invoke()
        }
    }

    private suspend fun performComputationOperations(task: () -> Unit) {
        withContext(Dispatchers.Default) {
            task.invoke()
        }
    }

    class ScopeInvalidException(message: String) : Exception(message)
}
