package com.example.coursefinderapp.data.remote.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.coursefinderapp.domain.usecase.SyncCourseUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncCourseUseCase: SyncCourseUseCase
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            syncCourseUseCase.invoke()
            Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "Error in doWork", e)
            Result.retry()
        }
    }
}