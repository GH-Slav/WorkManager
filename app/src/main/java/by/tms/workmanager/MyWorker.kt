package by.tms.workmanager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyWorker(context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {
    override fun doWork(): Result {
        val dataToSave = inputData.getString(WORKER_DATA)
        Log.e("MY_WORK", "Start saving work. DATA: $dataToSave")
        Thread.sleep(7000)

        val sp = applicationContext.getSharedPreferences(SHARED_PREF_FILENAME, Context.MODE_PRIVATE)
        sp.edit().putString(SAVED_WORK, dataToSave).apply()


        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(applicationContext, "Work complete.", Toast.LENGTH_SHORT).show()
        }
        Log.e("MY_WORK", "Work complete.")
        return Result.success()
    }
}