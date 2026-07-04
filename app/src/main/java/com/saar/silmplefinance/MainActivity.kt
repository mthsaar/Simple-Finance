package com.saar.silmplefinance

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar
import java.util.Locale
import com.saar.silmplefinance.database.DatabaseHandler
import com.saar.silmplefinance.databinding.ActivityMainBinding
import com.saar.silmplefinance.entity.Cadastro


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var banco: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.etData.setOnClickListener {
            showDatePicker()
        }

        banco = DatabaseHandler(this)

        if (intent.getIntExtra("id", 0) > 0) {
            binding.etCod.setText(intent.getIntExtra("id", 0).toString())
            binding.etCod.visibility = View.VISIBLE
            binding.tvCod.visibility = View.VISIBLE
            binding.etNome.setText(intent.getStringExtra("nome"))
            binding.etData.setText(intent.getStringExtra("data"))
            binding.etValor.setText(intent.getStringExtra("valor"))
            if (intent.getStringExtra("tipo").equals("crédito", ignoreCase = true)) {
                binding.rbCredito.isChecked = true
            } else {
                binding.rbDebito.isChecked = true
            }
        } else {
            binding.btExcluir.visibility = View.GONE
            binding.btVerLancamentos.visibility = View.GONE
        }


        binding.btExcluir.setOnClickListener {
            excluir()
        }

        binding.btSalvar.setOnClickListener {
            salvar()
        }

    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                binding.etData.setText(date)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun salvar() {
        val nome = binding.etNome.text.toString()
        val valor = binding.etValor.text.toString()
        val data = binding.etData.text.toString()

        if (nome.isEmpty() || valor.isEmpty() || data.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Lógica condicional para o Tipo (Crédito ou Débito)
        val tipo = if (binding.rbCredito.isChecked) {
            "Crédito"
        } else {
            "Débito"
        }

        val idExtra = intent.getIntExtra("id", 0)
        if (idExtra > 0) {
            val cadastro = Cadastro(idExtra, nome, tipo, valor, data)
            banco.alterar(cadastro)
            Toast.makeText(this@MainActivity, "Lançamento salvo", Toast.LENGTH_SHORT).show()
        } else {
            // Generate a random ID or use a timestamp
            val id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
            val cadastro = Cadastro(id, nome, tipo, valor, data)
            banco.incluir(cadastro)
            Toast.makeText(this@MainActivity, "Lançamento salvo", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    private fun excluir() {
        val id = binding.etCod.text.toString().toIntOrNull()
        if (id == null) {
            binding.etCod.error = "Digite um código válido"
        } else {
            banco.excluir(id)
            Toast.makeText(
                this@MainActivity,
                "Exclusão efetuada com sucesso.",
                Toast.LENGTH_LONG
            ).show()
            finish()
        }
    }
}