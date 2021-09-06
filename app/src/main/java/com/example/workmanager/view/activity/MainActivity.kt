package com.example.workmanager.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.workmanager.R
import com.example.workmanager.utils.*
import com.example.workmanager.worker.chaining.FirstWorkRequest
import com.example.workmanager.worker.chaining.SecondWorkRequest
import com.example.workmanager.worker.chaining.ThirdWorkRequest
import com.example.workmanager.worker.onetimework.OneTimeWork
import com.example.workmanager.worker.onetimework.OneTimeWorkWithRetry
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var oneTimeWorkRequest: OneTimeWorkRequest
    private lateinit var oneTimeWorkRequestWithRetry: OneTimeWorkRequest
    private lateinit var firstWorkRequest: OneTimeWorkRequest
    private lateinit var secondWorkRequest: OneTimeWorkRequest
    private lateinit var thirdWorkRequest: OneTimeWorkRequest
    private val inputData by lazy {
        workDataOf(INPUTMESSAGE to "Divanshu Srivastava")
    }
    private val constraints by lazy {
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOnClickListeners()
        setOneTimeWorker()
        setOneTimeWorkerWithRetry()
        setChainsOfWorkRequests()
        setWorkerObservers()
    }

    private fun setOnClickListeners() {
        btn_one_time_work.setOnClickListener {
            WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)
        }
        btn_one_time_work_with_retry.setOnClickListener {
            WorkManager.getInstance(this).enqueue(oneTimeWorkRequestWithRetry)
        }
        btn_chain_of_work.setOnClickListener {
            WorkManager.getInstance(this)
                .beginWith(listOf(firstWorkRequest, secondWorkRequest))
                .then(thirdWorkRequest)
                .enqueue()
        }
        btn_cancel_work.setOnClickListener {
            Timber.i("Cancelled all work with tag : {$WorkManagerTag}")
            WorkManager.getInstance(this).cancelAllWorkByTag(WorkManagerTag)
        }
    }

    private fun setOneTimeWorker() {
        oneTimeWorkRequest = OneTimeWorkRequestBuilder<OneTimeWork>()
            .addTag(WorkManagerTag)
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()
    }

    private fun setOneTimeWorkerWithRetry() {
        oneTimeWorkRequestWithRetry = OneTimeWorkRequest.Builder(OneTimeWorkWithRetry::class.java)
            .addTag(WorkManagerTag)
            .setInitialDelay(INITIALDELAY, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR, BACKOFFDELAY,
                TimeUnit.MILLISECONDS
            )
            .build()
    }

    private fun setChainsOfWorkRequests() {
        firstWorkRequest = OneTimeWorkRequest.Builder(FirstWorkRequest::class.java)
            .setConstraints(constraints)
            .build()
        secondWorkRequest = OneTimeWorkRequest.Builder(SecondWorkRequest::class.java)
            .setConstraints(constraints)
            .build()
        thirdWorkRequest = OneTimeWorkRequest.Builder(ThirdWorkRequest::class.java)
            .setConstraints(constraints)
            .setInputMerger(ArrayCreatingInputMerger::class.java)
            .build()
    }

    private fun setWorkerObservers() {
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    val outputData = workInfo.outputData.getString(OUTPUTMESSAGE)
                    Timber.i("OneTimeWork - Output data: {$outputData}")
                }
            })
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeWorkRequestWithRetry.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    val outputData = workInfo.outputData.getString(OUTPUTMESSAGE)
                    Timber.i("OneTimeWorkWithRetry - Output data: {$outputData}")
                }
            })
    }

}
