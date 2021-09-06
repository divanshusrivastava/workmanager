package com.example.workmanager.worker.onetimework

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.workmanager.utils.INPUTMESSAGE
import com.example.workmanager.utils.OUTPUTMESSAGE
import timber.log.Timber

class OneTimeWorkWithRetry(appContext: Context, workerParameters: WorkerParameters) :
    Worker(appContext, workerParameters) {

    companion object {
        private var numberOfRetries = 1
    }

    override fun doWork(): Result {
        return try {
            val inputMessage = inputData.getString(INPUTMESSAGE)
            Timber.i("OneTimeWorkWithRetry - Started by {$inputMessage} with retry number {$numberOfRetries}")
            if (numberOfRetries == 3)
                return Result.success(workDataOf(OUTPUTMESSAGE to "Completed OneTimeWorkWithRetry after 3 retries"))
            else {
                numberOfRetries++
                Result.retry()
            }
        } catch (exception: Exception) {
            Timber.e("OneTimeWorkWithRetry Exception: {$exception}")
            Result.failure()
        }
    }
}