package com.example.carlog.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.carlog.model.Car
import com.example.carlog.util.NotificationHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class MuayeneCheckWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): androidx.work.ListenableWorker.Result {
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val uid = auth.currentUser?.uid ?: return androidx.work.ListenableWorker.Result.failure()

        val notificationHelper = NotificationHelper(applicationContext)
        notificationHelper.createNotificationChannel()

        try {
            // Kullanıcı tercihlerini çek
            val userDoc = firestore.collection("users").document(uid).get().await()
            val muayeneNotifyEnabled = userDoc.getBoolean("muayeneNotify") ?: true
            val sigortaNotifyEnabled = userDoc.getBoolean("sigortaNotify") ?: true

            val snapshot = firestore.collection("users")
                .document(uid)
                .collection("cars")
                .get()
                .await()

            val cars = snapshot.toObjects(Car::class.java)
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val today = Calendar.getInstance()

            for (car in cars) {
                // Ayın ilk günü kontrolü (Her ay başı bildirim göndermek için)
                if (today.get(Calendar.DAY_OF_MONTH) != 1) continue

                // 1. Muayene Kontrolü
                if (muayeneNotifyEnabled && car.muayeneTarihi.isNotBlank()) {
                    checkAndSendReminder(
                        carName = car.name,
                        dateStr = car.muayeneTarihi,
                        type = "Muayene",
                        sdf = sdf,
                        today = today,
                        notificationHelper = notificationHelper
                    )
                }

                // 2. Sigorta Kontrolü
                if (sigortaNotifyEnabled && car.sigortaTarihi.isNotBlank()) {
                    checkAndSendReminder(
                        carName = car.name,
                        dateStr = car.sigortaTarihi,
                        type = "Sigorta",
                        sdf = sdf,
                        today = today,
                        notificationHelper = notificationHelper
                    )
                }
            }
            return androidx.work.ListenableWorker.Result.success()
        } catch (e: Exception) {
            return androidx.work.ListenableWorker.Result.retry()
        }
    }

    private fun checkAndSendReminder(
        carName: String,
        dateStr: String,
        type: String,
        sdf: SimpleDateFormat,
        today: Calendar,
        notificationHelper: NotificationHelper
    ) {
        try {
            val targetDate = sdf.parse(dateStr) ?: return
            val targetCal = Calendar.getInstance().apply { time = targetDate }

            // 4 ay öncesini hesapla
            val startWarningCal = (targetCal.clone() as Calendar).apply {
                add(Calendar.MONTH, -4)
            }

            // Eğer bugün uyarı dönemindeyse (tarihe 4 ay kalmış ve henüz geçmemiş)
            if (today.after(startWarningCal) && today.before(targetCal)) {
                notificationHelper.sendNotification(
                    "$type Hatırlatması",
                    "$carName aracınızın $type tarihi yaklaşıyor: $dateStr"
                )
            }
        } catch (e: Exception) {
            // Tarih formatı hatalıysa atla
        }
    }
}
