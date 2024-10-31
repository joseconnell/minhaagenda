package com.jconnell.minhaagenda

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jconnell.minhaagenda.databinding.ActivityActualizarNotaBinding
import android.util.Log


class ActualizarNotaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActualizarNotaBinding
    private lateinit var db: NotasDatabaseHelper
    private var idNota: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActualizarNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotasDatabaseHelper(this)
        idNota = intent.getIntExtra("id_nota", -1)

        if (idNota == -1) {
            finish()
            return
        }

        val nota = db.getidNota(idNota)
        if (nota != null) {
            binding.edtTitulo.setText(nota.titulo)
            binding.edtDescripcion.setText(nota.descripcion)
        } else {
            Toast.makeText(this, "Nota no encontrada", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.ivActualizar.setOnClickListener {
            val nuevoTitulo = binding.edtTitulo.text.toString()
            val nuevaDescripcion = binding.edtDescripcion.text.toString()
            if (nuevoTitulo.isNotEmpty() && nuevaDescripcion.isNotEmpty()) {
                val notaActualizada = Nota(idNota, nuevoTitulo, nuevaDescripcion)
                val rowsUpdated = db.updateNota(notaActualizada)
                if (rowsUpdated > 0) {
                    Toast.makeText(this, "Nota actualizada", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Log.d("ActualizarNotaActivity", "Error al actualizar la nota: $idNota, $nuevoTitulo, $nuevaDescripcion")
                    Toast.makeText(this, "Error al actualizar la nota", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
