package com.example.coroutines

import android.os.Looper


public object GlobalScope : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = EmptyCoroutineContext
}

public fun CoroutineScope.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    block: CoroutineScope.() -> Unit
) {
    when (context) {
        is Dispatchers.MainDispatcher -> {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                block()
            } else {
                AppExecutors.newInstance().mainThread().execute {
                    block()
                }
            }
        }
        is Dispatchers.IODispatcher -> {
            AppExecutors.newInstance().otherIO.execute {
                block()
            }
        }
        is Dispatchers.DiskIODispatcher -> {
            AppExecutors.newInstance().diskIO().execute {
                block()
            }
        }
        else -> {
            block()
        }
    }
}


public interface CoroutineScope {

    public val coroutineContext: CoroutineContext
}


public object EmptyCoroutineContext : CoroutineContext {

}

public interface CoroutineContext {

}
