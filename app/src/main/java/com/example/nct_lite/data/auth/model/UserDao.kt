package com.example.nct_lite.data.auth.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface UserDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)
    @Query("SELECT * FROM User LIMIT 1")
    suspend fun getUser(): User?
    @Query("DELETE FROM User")
    suspend fun clear()
}
