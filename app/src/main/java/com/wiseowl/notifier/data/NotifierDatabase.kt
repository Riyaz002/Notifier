package com.wiseowl.notifier.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wiseowl.notifier.data.entity.RuleEntity

@TypeConverters(TypeConverter::class)
@Database(entities = [RuleEntity::class], version = 1)
abstract class NotifierDatabase: RoomDatabase() {
    abstract val dao: Dao

    companion object{
        const val NAME = "notifier.db"

        @Volatile
        var INSTANCE: NotifierDatabase? = null

        fun getInstance(context: Context): NotifierDatabase{
            return INSTANCE ?: synchronized(this){
                INSTANCE = Room.databaseBuilder(context, NotifierDatabase::class.java, NAME).build()
                INSTANCE!!
            }
        }
    }
}