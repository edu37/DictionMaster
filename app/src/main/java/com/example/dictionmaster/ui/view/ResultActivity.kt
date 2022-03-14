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
import com.example.dictionmaster.service.models.APIModelResponse
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
    private var mAudioFile = ""

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

    // Carrega os dados da primeira activity e manda para o viewModel
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
                player.setDataSource(mAudioFile)

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
                    handleState(resource)
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

    // Cuida da resposta retornada pela API, garantindo também que nenhum valor null seja colocado nas views
    private fun handleState(resource: ResourceState.Success<APIModelResponse>) {
        resource.data?.let { it ->
            it.results?.let { results ->
                results[0].lexicalEntries?.let { lexicalEntries ->
                    lexicalEntries[0].entries?.let { entries ->
                        entries[0].senses?.let { senses ->
                            senses[0].examples?.let { examples ->
                                mExampleAdapter.examples = examples
                                binding.cardviewExample.visibility = View.VISIBLE
                            }
                            senses[0].definitions?.let {
                                mDefinitionAdapter.sense = senses
                                binding.cardviewDefinition.visibility = View.VISIBLE
                            }
                        }
                        entries[0].pronunciations?.let { pronun ->
                            pronun[0].audioFile?.let {
                                mAudioFile = it
                                binding.cardviewPronunciation.visibility = View.VISIBLE
                            }
                            pronun[0].phoneticSpelling?.let {
                                binding.textPronunciation.text = it
                            }
                        }
                        binding.progressCircular.visibility = View.GONE
                    }
                }
            }

            binding.textWord.visibility = View.VISIBLE
            binding.buttonSearch.visibility = View.VISIBLE
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