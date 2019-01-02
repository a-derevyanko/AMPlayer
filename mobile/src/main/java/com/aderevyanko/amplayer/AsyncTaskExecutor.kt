package com.aderevyanko.amplayer

import android.os.AsyncTask
import java.util.concurrent.Callable

internal class AsyncTaskExecutor<V>(private val callback: () -> V,
                                    private val postExecuteCallBack: (V) -> Unit) : AsyncTask<Callable<V>, Void, V>() {
    init {
        execute()
    }

    override fun doInBackground(vararg params: Callable<V>?): V {
        return callback.invoke()
    }

    override fun onPostExecute(result: V) {
        if (!isCancelled) {
            postExecuteCallBack.invoke(result)
        }
    }
}