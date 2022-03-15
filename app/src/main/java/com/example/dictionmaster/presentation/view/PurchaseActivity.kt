package com.example.dictionmaster.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.dictionmaster.R
import com.example.dictionmaster.databinding.ActivityPurchaseBinding
import com.example.dictionmaster.util.Constants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PurchaseActivity : AppCompatActivity() {

    private var _binding: ActivityPurchaseBinding? = null
    private val binding get() = _binding!!

    private val db by lazy { Firebase.firestore }
    private val auth by lazy { Firebase.auth }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.buttonSubscribe.setOnClickListener {
            Toast.makeText(this, "Mais 10 buscas desbloqueadas", Toast.LENGTH_LONG).show()
            db.collection(Constants.USERS).document(auth.uid!!).update("request", 0)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}