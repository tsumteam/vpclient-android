package ru.mercury.vpclient.core.coroutines.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class CoroutineViewModel: ViewModel(), CoroutineScope {

    private val scopeJob: Job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable -> catch(throwable) }

    override val coroutineContext: CoroutineContext = scopeJob + Dispatchers.Main.immediate + exceptionHandler

    override fun onCleared() {
        coroutineContext.cancelChildren()
        super.onCleared()
    }

    @CallSuper
    protected open fun catch(throwable: Throwable) {
        Timber.d(throwable)
    }
}
