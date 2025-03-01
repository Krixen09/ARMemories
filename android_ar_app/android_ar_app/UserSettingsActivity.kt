## UserSettingsActivity.kt
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.slider.Slider
import kotlinx.coroutines.*

/**
 * Manages user settings, loads and saves user settings using FirebaseManager.
 */
class UserSettingsActivity : AppCompatActivity() {

    private lateinit var volumeSlider: Slider
    private lateinit var brightnessSlider: Slider
    private val firebaseManager = FirebaseManager()
    private val userSettingsLiveData = MutableLiveData<Settings>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_settings)
        initUI()
        loadUserSettings()
    }

    /**
     * Initializes the user interface components and sets their listeners.
     */
    private fun initUI() {
        volumeSlider = findViewById(R.id.volumeSlider)
        brightnessSlider = findViewById(R.id.brightnessSlider)

        volumeSlider.addOnChangeListener { _, _, _ ->
            debounceSaveUserSettings()
        }

        brightnessSlider.addOnChangeListener { _, _, _ ->
            debounceSaveUserSettings()
        }

        userSettingsLiveData.observe(this, Observer { settings ->
            volumeSlider.value = settings.volume.toFloat()
            brightnessSlider.value = settings.brightness.toFloat()
        })
    }

    /**
     * Loads user settings from Firebase and updates the UI accordingly.
     */
    private fun loadUserSettings() {
        firebaseManager.getCurrentUserId()?.let { userId ->
            coroutineScope.launch {
                try {
                    val user = withContext(Dispatchers.IO) { firebaseManager.fetchUserData(userId) }
                    user?.settings?.let { settings ->
                        userSettingsLiveData.value = settings
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@UserSettingsActivity, "Failed to load settings: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } ?: run {
            Toast.makeText(this, "Error: User not logged in.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Debounces the save user settings operation.
     */
    private fun debounceSaveUserSettings() {
        coroutineScope.launch {
            delay(1000) // Debounce time in milliseconds
            saveUserSettings()
        }
    }

    /**
     * Saves the user settings to Firebase.
     */
    private fun saveUserSettings() {
        firebaseManager.getCurrentUserId()?.let { userId ->
            val settings = Settings(volume = volumeSlider.value.toInt(), brightness = brightnessSlider.value.toInt())
            val user = User(userId = userId, settings = settings)
            coroutineScope.launch {
                try {
                    withContext(Dispatchers.IO) { firebaseManager.saveUserData(user) }
                    Toast.makeText(this@UserSettingsActivity, "Settings saved successfully.", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@UserSettingsActivity, "Failed to save settings: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } ?: run {
            Toast.makeText(this, "Error: User not logged in.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel() // Cancel all coroutines when the activity is destroyed
    }
}
