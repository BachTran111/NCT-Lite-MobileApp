package com.example.nct_lite.data
import com.example.nct_lite.data.auth.model.User
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nct_lite.data.auth.model.UserDao

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}