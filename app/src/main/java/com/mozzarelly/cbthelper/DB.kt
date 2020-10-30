package com.mozzarelly.cbthelper

import android.content.Context
import android.util.Log
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.coroutineScope
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import androidx.lifecycle.LiveData

@Dao
interface EntryDao {
    @Query("SELECT * FROM entry WHERE id = :id")
    suspend fun get(id: Int): Entry

    @Query("SELECT * FROM entry WHERE id = :id")
    fun getAsync(id: Int): LiveData<Entry>

    @Insert(entity = Entry::class)
    suspend fun insert(entry: Entry): Long

    @Update
    suspend fun update(entry: Entry)

    @Query("SELECT * FROM entry WHERE complete = 1 ORDER BY date DESC")
    fun getAllCompleteAsync(): LiveData<List<Entry>>

    @Query("SELECT * FROM entry WHERE complete = 1 ORDER BY date DESC")
    suspend fun getAllComplete(): List<Entry>

    @Query("SELECT * FROM entry WHERE complete = 0 ORDER BY date DESC LIMIT 1")
    fun getIncompleteAsync(): LiveData<Entry?>

    @Query("SELECT * FROM entry WHERE complete = 0 ORDER BY date DESC LIMIT 1")
    suspend fun getIncomplete(): Entry?

    @Query("DELETE FROM entry WHERE complete = 0")
    suspend fun deleteIncomplete()

    @Query("DELETE FROM entry WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM entry")
    suspend fun deleteAll()
}

@Dao
interface CogValidDao {

    @Query("SELECT * FROM cogvalid WHERE id = :id")
    suspend fun get(id: Int): CogValid?

    @Insert(entity = CogValid::class)
    suspend fun create(model: CogValid): Long

    @Update
    suspend fun update(from: CogValid)

}

@Database(entities = [Entry::class, CogValid::class], version = 1)
@TypeConverters(Converters::class)
abstract class CBTDatabase : RoomDatabase() {
    abstract fun entryDao(): EntryDao
    abstract fun cogValidDao(): CogValidDao

    companion object {
        @Volatile
        private var instance: CBTDatabase? = null

        fun getDatabase(context: Context): CBTDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        fun getEntryDao(context: Context): EntryDao = getDatabase(context).entryDao()
        fun getCogValidDao(context: Context): CogValidDao = getDatabase(context).cogValidDao()

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): CBTDatabase {
            return Room.databaseBuilder(context, CBTDatabase::class.java, "reader-db")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<PopulateDatabaseWorker>().build()
                        WorkManager.getInstance(context).enqueue(request)
                    }
                })
                .build()
        }

        class PopulateDatabaseWorker(
            context: Context,
            workerParams: WorkerParameters
        ) : CoroutineWorker(context, workerParams) {
            override suspend fun doWork(): Result = coroutineScope {
                try {
                    val dao = getDatabase(applicationContext).entryDao()

                    dao.insert(Entry(
                        id = 1,
                        situation = "We talked about video games and how bad I am at Rocket League.",
                        complete = true,
                        situationType = false,
                        situationDetail = "Aaron",
                        emotion1 = "Rage",
                        emotion2 = "Envy",
                        thoughts = "I need to improve so much at this game.",
                        assumptions = "I assume I can't improve beyond a certain point no matter how much I practice.",
                        relationships = "I think he lost respect for me because my gamer skillz are low.",
                        expression = "I whined and posted some bad memes.",
                        date = LocalDateTime.of(2020, 4, 4, 10, 23)
                    ))

                    dao.insert(Entry(
                        id = 2,
                        complete = false,
                        situationType = true,
                        situationDetail = "Driving my car",
                        situation = "I heard a song on the radio.",
                        date = LocalDateTime.now()
                    ))

                    Result.success()
                }
                catch (ex: Exception) {
                    Log.e("DB", "Error seeding database", ex)
                    Result.failure()
                }
            }
        }
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(it), ZoneId.systemDefault()
            )
        }
    }

    @TypeConverter
    fun LocalDateTimeToTimestamp(date: LocalDateTime?): Long? {
        return date?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }
}