package com.example.carlog.userInterface.auth.uiModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val carCount: Int = 0,
    val totalMaintenanceCount: Int = 0,
    val muayeneNotify: Boolean = true,
    val sigortaNotify: Boolean = true
)

class UserViewModel(
    private var auth: FirebaseAuth = Firebase.auth,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchUserProfile()
    }

    fun fetchUserProfile() {
        val currentUser = auth.currentUser
        if (currentUser == null) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Araç sayısını çekelim
                val carsSnapshot = firestore.collection("users")
                    .document(currentUser.uid)
                    .collection("cars")
                    .get()
                    .await()
                
                val carCount = carsSnapshot.size()
                
                // Tüm bakımları sayalım
                var totalMaintenance = 0
                for (doc in carsSnapshot.documents) {
                    val maintenanceList = doc.get("maintenanceList") as? List<*>
                    totalMaintenance += maintenanceList?.size ?: 0
                }

                // Kullanıcı ismini ve bildirim tercihlerini çekelim
                val userDoc = firestore.collection("users").document(currentUser.uid).get().await()
                val name = userDoc.getString("name") ?: currentUser.displayName ?: "Kullanıcı"
                val muayeneNotify = userDoc.getBoolean("muayeneNotify") ?: true
                val sigortaNotify = userDoc.getBoolean("sigortaNotify") ?: true
                
                _userProfile.value = UserProfile(
                    name = name,
                    email = currentUser.email ?: "",
                    carCount = carCount,
                    totalMaintenanceCount = totalMaintenance,
                    muayeneNotify = muayeneNotify,
                    sigortaNotify = sigortaNotify
                )
            } catch (e: Exception) {
                // Hata durumunda en azından auth bilgisini gösterelim
                _userProfile.value = UserProfile(
                    name = currentUser.displayName ?: "Kullanıcı",
                    email = currentUser.email ?: "",
                    carCount = 0,
                    totalMaintenanceCount = 0,
                    muayeneNotify = true,
                    sigortaNotify = true
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateNotificationSetting(type: String, enabled: Boolean) {
        val currentUser = auth.currentUser ?: return
        
        // Önce yerel state'i anında güncelle (Optimistic UI)
        _userProfile.value = _userProfile.value?.let {
            if (type == "muayeneNotify") it.copy(muayeneNotify = enabled)
            else it.copy(sigortaNotify = enabled)
        }

        viewModelScope.launch {
            try {
                // Döküman yoksa oluşturması için set + merge kullanıyoruz
                firestore.collection("users").document(currentUser.uid)
                    .set(mapOf(type to enabled), SetOptions.merge()).await()
            } catch (e: Exception) {
                // Hata durumunda verileri tekrar çekerek senkronize et
                fetchUserProfile()
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            auth.signOut()
            _userProfile.value = null
        }
    }
}
