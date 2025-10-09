package com.example.assignmentapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.assignmentapp.data.source.local.entity.FavoriteNewsEntity
import com.example.assignmentapp.data.source.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, FavoriteNewsEntity::class],
    version = 1,
    exportSchema = false
)

abstract class AssignmentAppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun favoriteNewsDao(): FavoriteNewsDao
}