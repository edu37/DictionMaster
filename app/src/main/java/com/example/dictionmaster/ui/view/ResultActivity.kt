package com.example.dictionmaster.ui.view

import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dictionmaster.databinding.ActivityResultBinding
import com.example.dictionmaster.ui.state.ResourceState
import com.example.dictionmaster.ui.view.adapter.DefinitionAdapter
import com.example.dictionmaster.ui.view.adapter.ExampleAdapter
import com.example.dictionmaster.ui.viewmodel.ResultViewModel
import com.example.dictionmaster.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.lang.Exception

@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {

    private val mViewMoldel: ResultViewModel by viewModels()

    private val mExampleAdapter by lazy { ExampleAdapter() }
    private val mDefinitionAdapter by lazy { DefinitionAdapter() }

    private var _binding: ActivityResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var player: MediaPlayer
    private var audioFile = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        loadData()
        setupRecycler()
        setupCollect()

        setupButtons()

    }

    private fun setupButtons() {
        binding.audioPronunciation.setOnClickListener {
            try {
                player = MediaPlayer().apply {
                    setAudioStreamType(AudioManager.STREAM_MUSIC)
                    setDataSource(audioFile)
                    prepare()
                    start()
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun loadData() {
        val bundle = intent.extras
        val word: String = bundle!!.getString(Constants.WORD).toString()
        val language: String = bundle.getString(Constants.LANGUAGE).toString()
        mViewMoldel.fetch(language, word)
        binding.textWord.text = word.uppercase()
    }

    private fun setupCollect() = lifecycleScope.launchWhenStarted {
        mViewMoldel.data.collect { resource ->
            when (resource) {
                is ResourceState.Success -> {
                    resource.data?.let {
                        mExampleAdapter.examples = it.results[0].lexicalEntries[0].entries[0].senses[0].examples
                    }
                }
                is ResourceState.Error -> {
                    resource.data?.let {
                        Toast.makeText(applicationContext, "Um erro ocorreu", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                else -> {}
            }
        }
        mViewMoldel.data.collect { resource ->
            when (resource) {
                is ResourceState.Success -> {
                    resource.data?.let {
                        mDefinitionAdapter.definitions = it.results[0].lexicalEntries[0].entries[0].senses[0].definitions
                    }
                }
                is ResourceState.Error -> {
                    resource.data?.let {
                        Toast.makeText(applicationContext, "Um erro ocorreu", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                else -> {}
            }
        }
        mViewMoldel.pronunciation.collect { resource ->
            when (resource) {
                is ResourceState.Success -> {
                    resource.data?.let {
                        binding.textPronunciation.text = it[0].phoneticSpelling
                        audioFile = it[0].audioFile
                    }
                }
                is ResourceState.Error -> {
                    resource.data?.let {
                        Toast.makeText(applicationContext, "Um erro ocorreu", Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {}
            }
        }
    }


    private fun setupRecycler() = with(binding) {
        recyclerDefinition.apply {
            this.adapter = mDefinitionAdapter
            this.layoutManager = LinearLayoutManager(this@ResultActivity)
        }
        recyclerExample.apply {
            this.adapter = mExampleAdapter
            this.layoutManager = LinearLayoutManager(this@ResultActivity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}