## AppDatabase.kt
import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Room database for storing user progress.
 */
@Database(entities = [Progress::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun progressDao(): ProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

/**
 * Data access object for the Progress entity.
 */
@Dao
interface ProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: Progress)

    @Query("SELECT * FROM progress WHERE userId = :userId")
    fun loadProgress(userId: String): Flow<Progress?>

    @Update
    suspend fun updateProgress(progress: Progress)

    @Delete
    suspend fun deleteProgress(progress: Progress)
}

/**
 * Entity representing user progress.
 */
@Entity(tableName = "progress")
data class Progress(
    @PrimaryKey val userId: String,
    val progressData: String
)
