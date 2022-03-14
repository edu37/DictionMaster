package com.example.dictionmaster.ui.view

import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

    private fun loadData() {
        val bundle = intent.extras
        val word: String = bundle!!.getString(Constants.WORD).toString()
        val language: String = bundle.getString(Constants.LANGUAGE).toString()
        mViewMoldel.fetch(language, word)
        binding.textWord.text = word.uppercase()
    }

    private fun setupButtons() {
        binding.audioPronunciation.setOnClickListener {
            try {
                player = MediaPlayer()
                player.setAudioStreamType(AudioManager.STREAM_MUSIC)
                player.setDataSource(audioFile)

                player.prepare()
                player.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        binding.buttonSearch.setOnClickListener {
            finish()
        }
    }

    private fun setupCollect() = lifecycleScope.launchWhenStarted {
        mViewMoldel.data.collect { resource ->
            when (resource) {
                is ResourceState.Success -> {
                    resource.data?.let {
                        mExampleAdapter.examples =
                            it.results[0].lexicalEntries[0].entries[0].senses[0].examples
                        mDefinitionAdapter.sense = it.results[0].lexicalEntries[0].entries[0].senses
                        binding.textPronunciation.text =
                            it.results[0].lexicalEntries[0].entries[0].pronunciations[0].phoneticSpelling
                        audioFile =
                            it.results[0].lexicalEntries[0].entries[0].pronunciations[0].audioFile
                        binding.progressCircular.visibility = View.GONE

                        binding.textWord.visibility = View.VISIBLE
                        binding.cardviewDefinition.visibility = View.VISIBLE
                        binding.cardviewExample.visibility = View.VISIBLE
                        binding.cardviewPronunciation.visibility = View.VISIBLE
                        binding.buttonSearch.visibility = View.VISIBLE
                    }
                }
                is ResourceState.Error -> {
                    Toast.makeText(applicationContext, "Um erro ocorreu", Toast.LENGTH_SHORT)
                        .show()
                    binding.progressCircular.visibility = View.GONE
                    finish()
                }
                is ResourceState.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                    binding.textWord.visibility = View.GONE
                    binding.cardviewDefinition.visibility = View.GONE
                    binding.cardviewExample.visibility = View.GONE
                    binding.cardviewPronunciation.visibility = View.GONE
                    binding.buttonSearch.visibility = View.GONE
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