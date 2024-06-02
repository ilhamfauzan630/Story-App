package com.zanacademy.myfirstsubmissionintermediate.view.detailstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.zanacademy.myfirstsubmissionintermediate.R
import com.zanacademy.myfirstsubmissionintermediate.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    companion object {
        const val PHOTO_URL = "PHOTO_URL"
        const val NAME = "NAME"
        const val DESCRIPTION = "DESCRIPTION"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val photoUrl = intent.getStringExtra(PHOTO_URL)
        val name = intent.getStringExtra(NAME)
        val description = intent.getStringExtra(DESCRIPTION)

        setDetailStory(photoUrl, name, description)
    }

    private fun setDetailStory(photoUrl: String?, name: String?, description: String?) {
        binding.storyName.text = name
        binding.storyDescription.text = description
        Glide.with(this)
            .load(photoUrl)
            .into(binding.photoUrl)

    }
}