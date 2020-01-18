package com.clean.domain.baseusecase

import androidx.lifecycle.LiveData
import com.clean.data.utils.executors.AppExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * Base class of use cases (Feature set inside the application)
 *
 * @param T return type from Repository (@data) layer
 * @param Params input parameter for Repository (@data) layer
 * @property appExecutors for switching the threads
 */
abstract class UseCase<T, in Params>(private val appExecutors: AppExecutors) {


    protected abstract fun buildUseCase(appExecutors: AppExecutors, params: Params?): LiveData<T>

    fun execute(scope: CoroutineScope, params: Params? = null): LiveData<T> {
        appExecutors.injectScope(scope)
        return runBlocking {
            doSomething(params)
        }
    }

    private suspend fun doSomething(params: Params?) = withContext(Dispatchers.IO) {
        buildUseCase(appExecutors, params)
    }

}
