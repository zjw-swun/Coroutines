package com.example.coroutines

public object Dispatchers {
    @JvmStatic
    public val Default: EmptyCoroutineContext = EmptyCoroutineContext

    @JvmStatic
    public val Main: MainDispatcher = MainDispatcher()

    @JvmStatic
    public val IO: IODispatcher = IODispatcher()

    public val DiskIO: DiskIODispatcher = DiskIODispatcher()


    public class MainDispatcher : CoroutineContext
    public class IODispatcher : CoroutineContext
    public class DiskIODispatcher : CoroutineContext

}


