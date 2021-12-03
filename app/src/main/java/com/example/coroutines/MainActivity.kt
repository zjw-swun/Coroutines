package com.example.coroutines

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

/** 协程简易实现
 *suspend函数编译以后的格式详见
 * https://github.com/yujinyan/kotlin-playground/blob/master/src/main/kotlin/coroutine/continuation/ManualContinuationExercise.kt
 **/
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("TAG", "time is " + getCurrentTime())
        //协程的特性是代码中调用一次 实际会执行2次 第一次如果不满足条件先return一个状态  当满足条件的时候线程池会回调该方法执行第二次
        GlobalScope.launch(Dispatchers.Main){
            //延迟3秒出结果
            foo()
        }
        Log.e("TAG", "不等foo完成也能继续执行")
    }


    //协程代码
    //suspend fun foo() :Any{
    //  delay(3000L)
    //   val value =getCurrentTime()
    //   Log.e("TAG", "result is $value")
    //}
    //等价代码
    @suspend fun foo() {
        foo(object : Continuation<Any> {
            override fun resumeWith(result: Result<Any>) {
                val value = result.getOrThrow()
                Log.e("TAG", "result is $value")
            }
        })
    }

    @suspend fun foo(continuation: Continuation<Any>): Any {
        class FooContinuation : Continuation<Any> {
            var label: Int = 0

            override fun resumeWith(result: Result<Any>) {
                val outcome = invokeSuspend()
                if (outcome === COROUTINE_SUSPENDED) return
                continuation.resume(result.getOrThrow())
            }

            fun invokeSuspend(): Any {
                return foo(this)
            }
        }

        val cont = (continuation as? FooContinuation) ?: FooContinuation()
        return when (cont.label) {
            0 -> {
                cont.label++
                //异步延时任务
                AppExecutors.newInstance().otherIO.execute {
                    Thread.sleep(3000L)
                    val value = getCurrentTime()
                    cont.resume(value)
                }
                COROUTINE_SUSPENDED
            }
            1 -> 1 // return 1
            else -> error("shouldn't happen")
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentTime(): String {
        val now = Date() // 创建一个Date对象，获取当前时间
        // 指定格式化格式
        var f = SimpleDateFormat("今天是 " + "yyyy 年 MM 月 dd 日 E HH 点 mm 分 ss 秒")
        var value = f.format(now)
        return value
    }
}