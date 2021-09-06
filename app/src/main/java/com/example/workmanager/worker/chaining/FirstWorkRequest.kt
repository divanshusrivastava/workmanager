package com.example.workmanager.worker.chaining

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.workmanager.utils.OUTPUTMESSAGE
import timber.log.Timber

class FirstWorkRequest(appContext: Context, workerParameters: WorkerParameters) :
    Worker(appContext, workerParameters) {
    override fun doWork(): Result {
        return try {
            Timber.i("Started ChainOfWorkRequests: FirstWorkRequest running in parallel")
            val outputMessage = workDataOf(OUTPUTMESSAGE to "Output data from FirstWorkRequest")
            Result.success(outputMessage)
        } catch (exception: Exception) {
            Timber.i("Started ChainOfWorkRequests Exception: {$exception}")
            Result.failure()
        }

    }
}