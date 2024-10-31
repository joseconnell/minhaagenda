package com.jconnell.minhaagenda

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jconnell.minhaagenda.databinding.ActivityAgregarNotaBinding

class AgregarNotaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarNotaBinding
    private lateinit var db: NotasDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = NotasDatabaseHelper(this)

        binding.ivGuardarNota.setOnClickListener {
            val titulo = binding.edtTitulo.text.toString()
            val descripcion = binding.edtDescripcion.text.toString()

            if (titulo.isNotEmpty() && descripcion.isNotEmpty()) {
                guardarNota(titulo, descripcion)
            } else {
                Toast.makeText(applicationContext, "Llene los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun guardarNota(titulo: String, descripcion: String) {
        val nota = Nota(id = 0, titulo, descripcion)
        db.insertNota(nota)
        Toast.makeText(applicationContext, "Se ha agregado la nota", Toast.LENGTH_SHORT).show()
        startActivity(Intent(applicationContext, MainActivity::class.java))
    }
}
