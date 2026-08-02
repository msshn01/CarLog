package com.example.carlog.data.repository

import com.example.carlog.model.Car
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


class CarRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val currentUserId: String?
        get() = auth.currentUser?.uid


    fun getUserCars(): Flow<List<Car>> = callbackFlow {
        val uid = currentUserId
        if (uid == null) {
            close(Exception("Kullanıcı oturumu bulunamadı!"))
            return@callbackFlow
        }

        // users/{uid}/cars koleksiyonunu dinliyoruz
        val subscription = firestore.collection("users")
            .document(uid)
            .collection("cars")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    // Firestore dokümanlarını Car nesnelerine dönüştürüyoruz
                    val cars = mutableListOf<Car>()
                    for (doc in snapshot.documents) {
                        try {
                            val car = doc.toObject(Car::class.java)
                            if (car != null) {
                                cars.add(car)
                            }
                        } catch (e: Exception) {
                            // Hatalı veya eski formatlı veriyi logla ve atla
                            android.util.Log.e("CarRepository", "Deserialization error for doc ${doc.id}: ${e.message}")
                        }
                    }
                    trySend(cars) // Akışa yeni listeyi fırlatır
                }
            }

        // Flow sonlandığında listener'ı kaldırarak bellek sızıntısını (memory leak) önleriz
        awaitClose { subscription.remove() }
    }



    /**
     * Suspend fonksiyon: Coroutine'i bloklamadan Firestore yanıt verene kadar bekler.
     * Başarısız olursa doğrudan Exception fırlatır (Result pattern veya try-catch ile yakalanır).
     */

    suspend fun changeKm(carId: String, newKm: String) {
        val uid = currentUserId ?: throw Exception("Oturum açık değil! Lütfen giriş yapın.")


        firestore.collection("users")
            .document(uid)
            .collection("cars")
            .document(carId)
            .update("km", newKm) // Sayısal değer olarak günceller
            .await()
    }
    suspend fun saveCar(car: Car) {
        val uid = currentUserId ?: throw Exception("Oturum açık değil! Lütfen giriş yapın.")

        val carMap = hashMapOf(
            "id" to car.id,
            "name" to car.name,
            "model" to car.model,
            "year" to car.year,
            "km" to car.km,
            "maintenanceList" to car.maintenanceList
        )

        // .await() sayesinde callback kalabalığı bitti, kod senkron gibi sırayla akar!
        firestore.collection("users")
            .document(uid)
            .collection("cars")
            .document(car.id)
            .set(carMap)
            .await()
    }

    suspend fun addMaintenance(carId: String, maintenance: com.example.carlog.model.Maintenance) {
        val uid = currentUserId ?: throw Exception("Oturum açık değil!")

        firestore.collection("users")
            .document(uid)
            .collection("cars")
            .document(carId)
            .update("maintenanceList", FieldValue.arrayUnion(maintenance))
            .await()
    }
}