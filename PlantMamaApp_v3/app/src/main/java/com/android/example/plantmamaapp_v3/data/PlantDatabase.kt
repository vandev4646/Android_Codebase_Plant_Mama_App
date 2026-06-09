package com.android.example.plantmamaapp_v3.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Database class with a singleton Instance object.
 */
@Database(
    entities = [Plant::class, Reminder::class, Photo::class, Note::class, NotePhotoCrossRef::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PlantDatabase : RoomDatabase() {

    abstract fun plantDao(): PlantDao
    abstract fun reminderDao(): ReminderDao
    abstract fun photoDao(): PhotoDao

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var Instance: PlantDatabase? = null

        private  val MIGRATION_2_3 = object : Migration(2,3){
            override fun migrate(db: SupportSQLiteDatabase) {
                //Rename the 'notes' column to 'description'
                db.execSQL("ALTER TABLE plants RENAME COLUMN notes TO description")

                // Add the new datePurchased column as a non-null integer (Long) defaulting to 0
                db.execSQL("ALTER TABLE plants ADD COLUMN datePurchased INTEGER NOT NULL DEFAULT 0")

                // Estimate purchase date based on age in YEARS
                // Formula: Current Unix Time Milliseconds - (age * Milliseconds in a 365-day Year)
                // 1 day = 86,400,000 ms
                // 1 year (365 days) = 31,536,000,000 ms
                db.execSQL("""
            UPDATE plants 
            SET datePurchased = (strftime('%s', 'now') * 1000) - (age * 31536000000)
        """)

                // Drop the old 'age' column
                db.execSQL("ALTER TABLE plants DROP COLUMN age")

                //Create the notes table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `notes` (
                        `noteId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `plantId` INTEGER NOT NULL, 
                        `title` TEXT NOT NULL, 
                        `date` INTEGER NOT NULL,
                        FOREIGN KEY(`plantId`) REFERENCES `plants`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE 
                    )
                """)

                db.execSQL("CREATE INDEX IF NOT EXISTS `index_notes_plantId` ON `notes` (`plantId`)")

                // Create the junction cross-reference table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `note_photo_cross_ref` (
                        `noteId` INTEGER NOT NULL, 
                        `photoId` INTEGER NOT NULL, 
                        PRIMARY KEY(`noteId`, `photoId`)
                    )
                """)

                db.execSQL("CREATE INDEX IF NOT EXISTS `index_note_photo_cross_ref_noteId` ON `note_photo_cross_ref` (`noteId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_note_photo_cross_ref_photoId` ON `note_photo_cross_ref` (`photoId`)")


            }
        }

        fun getDatabase(context: Context): PlantDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, PlantDatabase::class.java, "plant_database")
                    //Migration for adding notes, updating from plant notes to description, changing from age to date purchased
                    .addMigrations(MIGRATION_2_3)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}