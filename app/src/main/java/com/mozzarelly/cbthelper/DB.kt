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
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {
    @Query("SELECT * FROM entry WHERE id = :id")
    suspend fun get(id: Int): Entry?

    @Query("SELECT * FROM entry WHERE id = :id")
    fun getAsync(id: Int): LiveData<Entry?>

    @Insert(entity = Entry::class)
    suspend fun insert(entry: Entry): Long

    @Update
    suspend fun update(entry: Entry)

    @Query("SELECT * FROM entry WHERE complete = 1 AND deleted = 0 ORDER BY date DESC")
    fun getAllCompleteAsync(): LiveData<List<Entry>>

    @Query("SELECT * FROM entry WHERE complete = 1 AND deleted = 0 ORDER BY date DESC")
    suspend fun getAllComplete(): List<Entry>

    @Query("SELECT * FROM entry WHERE complete = 1 AND deleted = 0 ORDER BY date DESC")
    fun getAllCompleteFlow(): Flow<List<Entry>>

    @Query("SELECT * FROM entry WHERE complete = 0 AND deleted = 0 ORDER BY date DESC LIMIT 1")
    fun getIncompleteAsync(): LiveData<Entry?>

    @Query("SELECT * FROM entry WHERE complete = 0 AND deleted = 0 ORDER BY date DESC LIMIT 1")
    suspend fun getIncomplete(): Entry?

    @Query("DELETE FROM entry WHERE complete = 0")
    suspend fun deleteIncomplete()

    @Query("UPDATE entry set deleted = 1 WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("UPDATE entry set deleted = 0 WHERE id = :id")
    suspend fun undelete(id: Int)

    @Query("DELETE FROM entry")
    suspend fun deleteAll()
}

@Dao
interface CogValidDao {

    @Query("SELECT * FROM cogvalid WHERE id = :id")
    suspend fun get(id: Int): CogValid?

    @Query("SELECT * FROM cogvalid WHERE id = :id")
    fun getAsync(id: Int): LiveData<CogValid?>

    @Insert(entity = CogValid::class)
    suspend fun create(model: CogValid): Long

    @Update
    suspend fun update(from: CogValid)

    @Query("DELETE FROM cogvalid")
    suspend fun deleteAll()

}

@Dao
interface RatRepDao {

    @Query("SELECT * FROM ratrep WHERE id = :id")
    suspend fun get(id: Int): RatRep?

    @Query("SELECT * FROM ratrep WHERE id = :id")
    fun getAsync(id: Int): LiveData<RatRep?>

    @Insert(entity = RatRep::class)
    suspend fun create(model: RatRep): Long

    @Update
    suspend fun update(from: RatRep)

    @Query("DELETE FROM ratrep")
    suspend fun deleteAll()

}

@Dao
interface BehaviorDao {

    @Query("SELECT * FROM behavior WHERE id = :id")
    suspend fun get(id: Int): Behavior?

    @Query("SELECT * FROM behavior WHERE id = :id")
    fun getAsync(id: Int): LiveData<Behavior?>

    @Insert(entity = Behavior::class)
    suspend fun create(model: Behavior): Long

    @Update
    suspend fun update(from: Behavior)

    @Query("DELETE FROM behavior")
    suspend fun deleteAll()

}

@Database(entities = [Entry::class, CogValid::class, RatRep::class, Behavior::class], version = 1)
@TypeConverters(Converters::class)
abstract class CBTDatabase : RoomDatabase() {
    abstract fun entryDao(): EntryDao
    abstract fun cogValidDao(): CogValidDao
    abstract fun ratRepDao(): RatRepDao
    abstract fun behaviorDao(): BehaviorDao

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
        fun getRatRepDao(context: Context): RatRepDao = getDatabase(context).ratRepDao()
        fun getBehaviorDao(context: Context): BehaviorDao = getDatabase(context).behaviorDao()

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): CBTDatabase {
            return Room.databaseBuilder(context, CBTDatabase::class.java, "cbtFl-db")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<PopulateDatabaseWorker>().build()
                        WorkManager.getInstance(context).enqueue(request)
                    }
                })
                .build()
        }

        suspend fun CBTDatabase.addEntities(
            entry: Entry,
            cogval: CogValid? = null,
            ratrep: RatRep? = null,
            behavior: Behavior? = null
        ){
            val id = entryDao().insert(entry).toInt()

            if (cogval != null) {
                cogval.id = id
                cogValidDao().create(cogval)
            }

            if (ratrep != null) {
                ratrep.id = id
                ratRepDao().create(ratrep)
            }

            if (behavior != null) {
                behavior.id = id
                behaviorDao().create(behavior)
            }
        }

        class PopulateDatabaseWorker(
            val context: Context,
            workerParams: WorkerParameters
        ) : CoroutineWorker(context, workerParams) {
            override suspend fun doWork(): Result = coroutineScope {
                try {
                    if (BuildConfig.DEBUG) {
                        CBTDatabase.getDatabase(context).populateSampleData()
                    }

                    Result.success()
                }
                catch (ex: Exception) {
                    Log.e("DB", "Error seeding database", ex)
                    Result.failure()
                }
            }
        }
    }

    private suspend fun populateSampleData() {
        var month = 1

        addEntities(
            Entry(
                situationDetail = "DEBUG:E! C! R! B!",
                situation = "We talked about video games and how bad I am at Rocket League.",
                complete = true,
                situationType = true,
                emotion1Name = "Rage",
                emotion1Intensity = 7,
                emotion2Name = "Envy",
                emotion2Intensity = 3,
                thoughts = "I need to improve so much at this game.",
                assumptions = "I assume I can't improve beyond a certain point no matter how much I practice.",
                relationships = "I think he lost respect for me because my gamer skillz are low.",
                expression = "I whined and posted some bad memes.",
                date = LocalDateTime.of(2020, month.also { month += 1 }, 4, 10, 23)
            ), CogValid(
                answer1 = 1,
                answer2 = 2,
                answer3 = 1,
                answer4 = 1,
                answer5 = 2,
                answer6 = 2,
                answer7 = 1,
                answer8 = "Jimbo",
                answer9 = 1,
                answer10 = 1
            ), RatRep(
                believe = 1,
                instead = "Do this instead",
                emotion1Name = "Happiness",
                wouldHaveDone = "Talk better",
                wouldHaveAffected = "They would be happier.",
                comparison = "The last one is better."
            ), Behavior(
                honest = 1,
                person = "Beauregard",
                disappointed = 1,
                disapprove = 1,
                embarrassed = 0,
                relationships = 1,
                occupations = 0,
                health = 1,
                other = 0,
                rational = 1
            )
        )
        addEntities(
            Entry(
                situationDetail = "DEBUG:E! C! R! B...",
                situation = "We talked about video games and how bad I am at Rocket League.",
                complete = true,
                situationType = true,
                emotion1Name = "Rage",
                emotion1Intensity = 7,
                emotion2Name = "Envy",
                emotion2Intensity = 3,
                thoughts = "I need to improve so much at this game.",
                assumptions = "I assume I can't improve beyond a certain point no matter how much I practice.",
                relationships = "I think he lost respect for me because my gamer skillz are low.",
                expression = "I whined and posted some bad memes.",
                date = LocalDateTime.of(2020, month.also { month += 1 }, 4, 10, 23)
            ), CogValid(
                answer1 = 1,
                answer2 = 2,
                answer3 = 1,
                answer4 = 1,
                answer5 = 2,
                answer6 = 2,
                answer7 = 1,
                answer8 = "Jimbo",
                answer9 = 1,
                answer10 = 1
            ), RatRep(
                believe = 1,
                instead = "Do this instead",
                emotion1Name = "Happiness",
                wouldHaveDone = "Talk better",
                wouldHaveAffected = "They would be happier.",
                comparison = "The last one is better."
            ), Behavior(
                honest = 1,
                person = "Beauregard",
                disappointed = 1,
                disapprove = 1,
                embarrassed = 0,
                relationships = 1
            )
        )

        addEntities(
            Entry(
                situationDetail = "DEBUG:E! C! R!",
                situation = "We talked about video games and how bad I am at Rocket League.",
                complete = true,
                situationType = true,
                emotion1Name = "Rage",
                emotion1Intensity = 7,
                emotion2Name = "Envy",
                emotion2Intensity = 3,
                thoughts = "I need to improve so much at this game.",
                assumptions = "I assume I can't improve beyond a certain point no matter how much I practice.",
                relationships = "I think he lost respect for me because my gamer skillz are low.",
                expression = "I whined and posted some bad memes.",
                date = LocalDateTime.of(2020, month.also { month += 1 }, 4, 10, 23)
            ), CogValid(
                answer1 = 1,
                answer2 = 2,
                answer3 = 1,
                answer4 = 1,
                answer5 = 2,
                answer6 = 2,
                answer7 = 1,
                answer8 = "Jimbo",
                answer9 = 1,
                answer10 = 1
            ), RatRep(
                believe = 1,
                instead = "Do this instead",
                emotion1Name = "Happiness",
                wouldHaveDone = "Talk better",
                wouldHaveAffected = "They would be happier.",
                comparison = "The last one is better."
            )
        )

        addEntities(
            Entry(
                situationDetail = "DEBUG:E! C! R…",
                situation = "We talked about video games and how bad I am at Rocket League.",
                complete = true,
                situationType = true,
                emotion1Name = "Rage",
                emotion1Intensity = 7,
                emotion2Name = "Envy",
                emotion2Intensity = 3,
                thoughts = "I need to improve so much at this game.",
                assumptions = "I assume I can't improve beyond a certain point no matter how much I practice.",
                relationships = "I think he lost respect for me because my gamer skillz are low.",
                expression = "I whined and posted some bad memes.",
                date = LocalDateTime.of(2020, month.also { month += 1 }, 4, 10, 23)
            ), CogValid(
                answer1 = 1,
                answer2 = 2,
                answer3 = 1,
                answer4 = 1,
                answer5 = 2,
                answer6 = 2,
                answer7 = 1,
                answer8 = "Jimbo",
                answer9 = 1,
                answer10 = 1
            ), RatRep(
                believe = 1,
                instead = "Do this instead",
                emotion1Name = "Happiness",
                emotion1Intensity = 1,
                wouldHaveDone = "smarter things",
            )
        )

        addEntities(
            Entry(
                situationDetail = "DEBUG:E! C!",
                situation = "We talked about video games and how bad I am at Rocket League.",
                complete = true,
                situationType = true,
                emotion1Name = "Rage",
                emotion1Intensity = 7,
                emotion2Name = "Envy",
                emotion2Intensity = 3,
                thoughts = "I need to improve so much at this game.",
                assumptions = "I assume I can't improve beyond a certain point no matter how much I practice.",
                relationships = "I think he lost respect for me because my gamer skillz are low.",
                expression = "I whined and posted some bad memes.",
                date = LocalDateTime.of(2020, month.also { month += 1 }, 4, 10, 23)
            ), CogValid(
                answer1 = 1,
                answer2 = 2,
                answer3 = 1,
                answer4 = 1,
                answer5 = 2,
                answer6 = 2,
                answer7 = 1,
                answer8 = "Jimbo",
                answer9 = 1,
                answer10 = 1
            )        )

        addEntities(
            Entry(
                situationDetail = "DEBUG:E! C…",
                situation = "We talked about video games and how bad I am at Hades.",
                complete = true,
                situationType = true,
                emotion1Name = "Rage",
                emotion1Intensity = 7,
                emotion2Name = "Envy",
                emotion2Intensity = 3,
                thoughts = "I need to improve so much at this game.",
                assumptions = "I assume I can't improve beyond a certain point no matter how much I practice.",
                relationships = "I think he lost respect for me because my gamer skillz are low.",
                expression = "I whined and posted some bad memes.",
                date = LocalDateTime.of(2020, month.also { month += 1 }, 14, 1, 3)
            ), CogValid(
                answer1 = 1,
                answer2 = 2
            )
        )

        addEntities(
            Entry(
                situationDetail = "DEBUG:E!",
                situation = "We talked about video games and how bad I am at Factorio.",
                complete = true,
                situationType = true,
                emotion1Name = "Rage",
                emotion1Intensity = 7,
                emotion2Name = "Envy",
                emotion2Intensity = 3,
                thoughts = "I need to improve so much at this game.",
                assumptions = "I assume I can't improve beyond a certain point no matter how much I practice.",
                relationships = "I think he lost respect for me because my gamer skillz are low.",
                expression = "I whined and posted some bad memes.",
                date = LocalDateTime.of(2020, month.also { month += 1 }, 14, 8, 13)
            )
        )

        addEntities(
            Entry(
                situationDetail = "DEBUG:E…",
                complete = false,
                situationType = true,
                situation = "I heard a song on the radio.",
                date = LocalDateTime.of(2019, month.also { month += 1 }, 14, 8, 13)
            )
        )

    }

    suspend fun Context.clean(addSampleData: Boolean = false){
        behaviorDao().deleteAll()
        ratRepDao().deleteAll()
        cogValidDao().deleteAll()
        entryDao().deleteAll()

        if (addSampleData){
            val request = OneTimeWorkRequestBuilder<PopulateDatabaseWorker>().build()
            WorkManager.getInstance(this).enqueue(request)
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