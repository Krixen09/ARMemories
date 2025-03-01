## MainActivity.kt
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

/**
 * Entry point of the app, initializes UI and navigates to ARActivity, UserSettingsActivity, and TutorialActivity.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }

    /**
     * Initializes the user interface and sets up navigation buttons.
     */
    private fun initUI() {
        val arButton: MaterialButton = findViewById(R.id.arButton)
        val settingsButton: MaterialButton = findViewById(R.id.settingsButton)
        val tutorialButton: MaterialButton = findViewById(R.id.tutorialButton)

        arButton.setOnClickListener {
            navigateToARActivity()
        }

        settingsButton.setOnClickListener {
            navigateToUserSettingsActivity()
        }

        tutorialButton.setOnClickListener {
            navigateToTutorialActivity()
        }
    }

    /**
     * Navigates to ARActivity.
     */
    private fun navigateToARActivity() {
        val intent = Intent(this, ARActivity::class.java)
        startActivity(intent)
    }

    /**
     * Navigates to UserSettingsActivity.
     */
    private fun navigateToUserSettingsActivity() {
        val intent = Intent(this, UserSettingsActivity::class.java)
        startActivity(intent)
    }

    /**
     * Navigates to TutorialActivity.
     */
    private fun navigateToTutorialActivity() {
        val intent = Intent(this, TutorialActivity::class.java)
        startActivity(intent)
    }
}
