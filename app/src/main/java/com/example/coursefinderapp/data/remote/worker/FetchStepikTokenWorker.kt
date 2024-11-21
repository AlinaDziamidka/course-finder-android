package com.example.coursefinderapp.data.remote.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.coursefinderapp.domain.usecase.FetchStepikTokenUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

@HiltWorker
class FetchStepikTokenWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val fetchStepikTokenUseCase: FetchStepikTokenUseCase
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            fetchStepikTokenUseCase.invoke()
            Result.success()
        } catch (e: Exception) {
            Log.e("FetchStepikTokenWorker", "Error in doWork", e)
            Result.retry()
        }
    }
}

