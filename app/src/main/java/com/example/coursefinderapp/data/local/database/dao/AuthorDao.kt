package com.example.coursefinderapp.data.local.database.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.coursefinderapp.data.local.database.model.AuthorModel
import com.example.coursefinderapp.data.local.database.model.CourseModel

@Dao
interface AuthorDao {

    @Query("SELECT * FROM author")
    fun fetchAll(): List<AuthorModel>

    @Query("SELECT * FROM author WHERE id = :authorId LIMIT 1")
    fun fetchById(authorId: Int): AuthorModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(authorModel: AuthorModel)

    @Query("DELETE FROM author WHERE id = :authorId")
    fun deleteById(authorId: Int)

    @Update
    fun updateOne(authorModel: AuthorModel)
}