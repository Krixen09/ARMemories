## TutorialActivity.kt
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/**
 * Manages the tutorial flow for new users, guides users through app functionalities.
 */
class TutorialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        startTutorial()
    }

    /**
     * Starts the tutorial for the user.
     * This method sets up the tutorial view and initializes any necessary components.
     * For simplicity, this example will just have a button to end the tutorial.
     */
    private fun startTutorial() {
        val endTutorialButton: Button = findViewById(R.id.endTutorialButton)
        endTutorialButton.setOnClickListener {
            endTutorial()
        }
    }

    /**
     * Ends the tutorial and returns to the MainActivity.
     * This method should be called when the user completes or skips the tutorial.
     */
    private fun endTutorial() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close the tutorial activity
    }
}
