package com.patrickhoette.test.android

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import org.junit.jupiter.api.extension.Extension

@SuppressLint("RestrictedApi")
class ArchComponentsExtension : Extension {

    private val taskExecutor = object : TaskExecutor() {

        override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
        override fun postToMainThread(runnable: Runnable) = runnable.run()
        override fun isMainThread(): Boolean = true
    }

    init {
        ArchTaskExecutor.getInstance().setDelegate(taskExecutor)
    }
}
