package com.saar.silmplefinance

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.core.view.WindowInsetsCompat
import com.saar.silmplefinance.database.DatabaseHandler
import com.saar.silmplefinance.adapter.ElementoListaAdapter
import com.saar.silmplefinance.databinding.ActivityExtratoViewBinding

class Extrato_View : AppCompatActivity() {

    private lateinit var binding: ActivityExtratoViewBinding
    private lateinit var banco: DatabaseHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExtratoViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btIncluir.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        banco = DatabaseHandler(this)
        
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch {

            val registros = banco.listar()

            val adapter = ElementoListaAdapter(
                this@Extrato_View,
                registros
            )

            binding.lvExtrato.adapter = adapter
        }
    }




}