package com.example.coroutines


@SinceKotlin("1.3")
public interface Continuation<in T> {
    /**
     * Resumes the execution of the corresponding coroutine passing a successful or failed [result] as the
     * return value of the last suspension point.
     */
    public fun resumeWith(result: Result<T>)
}


public inline fun <T> Continuation<T>.resume(value: T): Unit =
    resumeWith(Result.success(value))
