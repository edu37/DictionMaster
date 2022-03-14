package com.example.dictionmaster.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import com.example.dictionmaster.R
import com.example.dictionmaster.databinding.ActivityMainBinding
import com.example.dictionmaster.ui.viewmodel.MainViewModel
import com.example.dictionmaster.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var visibility = false

    private lateinit var fadeIn: Animation
    private lateinit var fadeOut: Animation
    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation

    private var mLanguage = ""
    private var mWord: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupAnims()
        setupFabs()

        setupSearch()
        setupCollect()

    }

    private fun setupCollect() = lifecycleScope.launchWhenStarted {
        viewModel.language.collect {
            mLanguage = it
        }
    }

    private fun setupSearch() {
        binding.buttonSearch.setOnClickListener {
            mWord = binding.textSearch.text.toString().lowercase()
            if (mWord != "") {
                val intent = Intent(this, ResultActivity::class.java)
                val bundle = Bundle()
                bundle.putString(Constants.LANGUAGE, mLanguage)
                bundle.putString(Constants.WORD, mWord)
                intent.putExtras(bundle)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Erro", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupAnims() {
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.visibility_fade_in)
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.visibility_fade_out)
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close)
    }

    private fun setupFabs() {
        binding.apply {
            backgroundFab.setOnClickListener {
                if (visibility) {
                    allGone()
                    visibility = false
                }
            }
            fabDefault.setOnClickListener {
                if (visibility) {
                    allGone()
                    visibility = false
                } else {
                    allVisible()
                    visibility = true
                }
            }
            fabEnglish.setOnClickListener {
                viewModel.updateLanguage(Constants.ENGLISH)
                changeFabDefault(Constants.ENGLISH)
            }
            fabSpanish.setOnClickListener {
                viewModel.updateLanguage(Constants.SPANISH)
                changeFabDefault(Constants.SPANISH)
            }
            fabFrench.setOnClickListener {
                viewModel.updateLanguage(Constants.FRENCH)
                changeFabDefault(Constants.FRENCH)
            }
        }
    }

    private fun allGone() {
        binding.apply {
            fabEnglish.visibility = View.GONE
            fabSpanish.visibility = View.GONE
            fabFrench.visibility = View.GONE
            fabEnglish.startAnimation(fabClose)
            fabSpanish.startAnimation(fabClose)
            fabFrench.startAnimation(fabClose)
            fabEnglish.isClickable = false
            fabSpanish.isClickable = false
            fabFrench.isClickable = false
            backgroundFab.visibility = View.GONE
            backgroundFab.startAnimation(fadeOut)
            backgroundFab.isClickable = false
        }
    }

    private fun allVisible() {
        binding.apply {
            fabEnglish.visibility = View.VISIBLE
            fabSpanish.visibility = View.VISIBLE
            fabFrench.visibility = View.VISIBLE
            fabEnglish.startAnimation(fabOpen)
            fabSpanish.startAnimation(fabOpen)
            fabFrench.startAnimation(fabOpen)
            fabEnglish.isClickable = true
            fabSpanish.isClickable = true
            fabFrench.isClickable = true
            backgroundFab.visibility = View.VISIBLE
            backgroundFab.startAnimation(fadeIn)
            backgroundFab.isClickable = true
        }
    }

    private fun changeFabDefault(language: String) {
        if (language == Constants.ENGLISH) {
            binding.fabDefault.setCompoundDrawablesRelativeWithIntrinsicBounds(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.ic_english_flag
                ), null, null, null
            )
            binding.fabDefault.text = "english"
        } else if (language == Constants.SPANISH) {
            binding.fabDefault.setCompoundDrawablesRelativeWithIntrinsicBounds(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.ic_spanish_flag
                ), null, null, null
            )
            binding.fabDefault.text = "spanish"
        } else {
            binding.fabDefault.setCompoundDrawablesRelativeWithIntrinsicBounds(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.ic_french_flag
                ), null, null, null
            )
            binding.fabDefault.text = "french"
        }
        binding.apply {
            allGone()
        }
        visibility = false
    }

    override fun onResume() {
        super.onResume()
        binding.textSearch.setText("")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}