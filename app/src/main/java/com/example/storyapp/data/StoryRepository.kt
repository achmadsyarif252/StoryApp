package com.example.storyapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.storyapp.data.local.entitiy.Story
import com.example.storyapp.data.local.room.StoryDao
import com.example.storyapp.data.local.room.StoryRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class StoryRepository(application: Application) {
    private val mStoryDao: StoryDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = StoryRoomDatabase.getDatabase(application)
        mStoryDao = db.storyDao()
    }

    fun getAllstory(): List<Story> = mStoryDao.getAllStories()

    fun insert(story: Story) {
        executorService.execute { mStoryDao.insert(story) }
    }

    fun delete() = executorService.execute { mStoryDao.deleteStories() }


}