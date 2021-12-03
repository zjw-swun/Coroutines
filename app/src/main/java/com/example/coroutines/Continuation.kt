package com.example.coroutines



@SinceKotlin("1.3")
public interface Continuation<in T> {

    public fun resumeWith(result: Result<T>)
}


public inline fun <T> Continuation<T>.resume(value: T): Unit =
    resumeWith(Result.success(value))
