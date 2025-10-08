package com.example.assignmentapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.assignmentapp.data.source.local.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)

abstract class WMSDatabase : RoomDatabase() {
    abstract fun wMSIncomingDao(): UserDao
}