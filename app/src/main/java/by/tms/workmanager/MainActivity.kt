package by.tms.workmanager

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*

const val SHARED_PREF_FILENAME = "SHARED_PREF_FILENAME"
const val SAVED_WORK = "SAVED_WORK"
const val WORKER_DATA = "WORKER_DATA"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sp = getSharedPreferences(SHARED_PREF_FILENAME, Context.MODE_PRIVATE)
        savedData.text = sp.getString(SAVED_WORK, "nothing saved")
        saveWorkButton.setOnClickListener {
            val userData = workDataOf(Pair(WORKER_DATA, inputData.text.toString()))
            inputData.text.clear()
            val batteryConstraint = Constraints.Builder()
                .setRequiresCharging(true)
                .build()
            val workerRequest = OneTimeWorkRequestBuilder<MyWorker>()
                .setInputData(userData)
                .setConstraints(batteryConstraint)
                .build()
            WorkManager.getInstance(this).enqueue(workerRequest)
            Toast.makeText(this, "Saving data...", Toast.LENGTH_SHORT).show()
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(workerRequest.id)
                .observe(this, Observer {
                    if (it.state == WorkInfo.State.SUCCEEDED) {
                        savedData.text = sp.getString(SAVED_WORK, "nothing saved")
                    }
                })
        }
    }
}

