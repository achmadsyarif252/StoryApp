package com.example.storyapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyapp.data.local.entitiy.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(story: Story)

    @Query("SELECT * FROM stories ORDER BY id ASC")
    fun getAllStories(): List<Story>

    @Query("DELETE FROM stories")
    fun deleteStories()

}