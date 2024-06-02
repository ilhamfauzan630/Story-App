package com.zanacademy.myfirstsubmissionintermediate.view.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.zanacademy.myfirstsubmissionintermediate.R
import com.zanacademy.myfirstsubmissionintermediate.data.LoadingStateAdapter
import com.zanacademy.myfirstsubmissionintermediate.data.ResultState
import com.zanacademy.myfirstsubmissionintermediate.data.pref.UserPreference
import com.zanacademy.myfirstsubmissionintermediate.data.pref.dataStore
import com.zanacademy.myfirstsubmissionintermediate.databinding.ActivityMainBinding
import com.zanacademy.myfirstsubmissionintermediate.view.ViewModelFactory
import com.zanacademy.myfirstsubmissionintermediate.view.addstory.AddStoryActivity
import com.zanacademy.myfirstsubmissionintermediate.view.maps.MapsActivity
import com.zanacademy.myfirstsubmissionintermediate.view.welcome.WelcomeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        setupView()
        setupAction()

        binding.addStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.title = getString(R.string.dicoding_story)
    }

    private fun setupAction() {
        val pref = UserPreference.getInstance(application.dataStore)
        val user = runBlocking { pref.getSession().first() }

        val token = "Bearer ${user.token}"

        Log.d("token", token)

        val adapter = ListStoryAdapter()
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.adapter = adapter

        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        viewModel.story(token).observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
            }
            R.id.keluar -> {
                viewModel.logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}