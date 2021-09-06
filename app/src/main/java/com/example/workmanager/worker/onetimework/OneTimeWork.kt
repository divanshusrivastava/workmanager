package com.example.workmanager.worker.onetimework

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.workmanager.utils.INPUTMESSAGE
import com.example.workmanager.utils.OUTPUTMESSAGE
import timber.log.Timber

class OneTimeWork(appContext: Context, workerParameters: WorkerParameters) :
    Worker(appContext, workerParameters) {

    override fun doWork(): Result {
        return try {
            val inputMessage = inputData.getString(INPUTMESSAGE)
            Timber.i("OneTimeWork - Started by {$inputMessage}")
            val outputMessage = workDataOf(OUTPUTMESSAGE to "Completed OneTimeWork")
            Result.success(outputMessage)
        } catch (exception: Exception) {
            Timber.e("OneTimeWork Exception: {$exception}")
            Result.failure()
        }
    }
}