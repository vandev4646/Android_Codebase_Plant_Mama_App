package com.android.example.plantmamaapp_v3.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import java.util.Date


@Entity(
    tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = Plant::class,
            parentColumns = ["id"],
            childColumns = ["plantId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["plantId"])]
)

data class Note (
    @PrimaryKey(autoGenerate = true) val noteId: Int = 0,
    val plantId: Int, //foreign key linked to plant
    val title: String,
    @TypeConverters(Converters::class) val date: Date
)

//A Junction table to handle the Many-to-Many relationship between Notes and Photos
@Entity(
    tableName = "note_photo_cross_ref",
    primaryKeys = ["noteId", "photoId"],
    indices = [
        Index(value = ["noteId"]),
        Index(value = ["photoId"])
    ]
)

data class NotePhotoCrossRef(
    val noteId: Int,
    val photoId: Int
)

data class NoteWithPhotos(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "noteId",
        entityColumn = "id",
        associateBy = Junction(
            NotePhotoCrossRef::class,
            parentColumn = "noteId",
            entityColumn = "photoId"
        )
    )
    val photos: List<Photo>
)

data class PlantWithNotesAndPhotos(
    @Embedded val plant: Plant,
    @Relation(
        entity = Note::class,
        parentColumn = "id",
        entityColumn = "plantId"
    )
    val notesWithPhotos: List<NoteWithPhotos>
)