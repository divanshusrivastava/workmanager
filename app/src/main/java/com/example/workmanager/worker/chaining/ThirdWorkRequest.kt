package com.example.workmanager.worker.chaining

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.workmanager.utils.OUTPUTMESSAGE
import timber.log.Timber

class ThirdWorkRequest(appContext: Context, workerParameters: WorkerParameters) :
    Worker(appContext, workerParameters) {
    override fun doWork(): Result {
        return try {
            Timber.i("Started ChainOfWorkRequests: ThirdWorkRequest")
            var inputMessage = ""
            val inputData = inputData.getStringArray(OUTPUTMESSAGE)
            inputData?.forEach {
                inputMessage += "$it "
            }
            Timber.i("Data from parent chain process: {${inputMessage.trim()}}")
            Result.success()
        } catch (exception: Exception) {
            Timber.i("Started ChainOfWorkRequests Exception: {$exception}")
            Result.failure()
        }

    }
}