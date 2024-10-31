package com.jconnell.minhaagenda

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NotasDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notas.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Notas"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "titulo"
        private const val COLUMN_DESCRIPCION = "descripcion"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TITLE TEXT, $COLUMN_DESCRIPCION TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertNota(nota: Nota) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, nota.titulo)
            put(COLUMN_DESCRIPCION, nota.descripcion)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllNotas(): List<Nota> {
        val listaNotas = mutableListOf<Nota>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val titulo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val descripcion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION))
            val nota = Nota(id, titulo, descripcion)
            listaNotas.add(nota)
        }
        cursor.close()
        db.close()
        return listaNotas
    }

    fun getidNota(idNota: Int): Nota? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(idNota.toString()))
        return if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val titulo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val descripcion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION))
            val nota = Nota(id, titulo, descripcion)
            cursor.close()
            db.close()
            nota
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    fun updateNota(nota: Nota): Int {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put(COLUMN_TITLE, nota.titulo)
                put(COLUMN_DESCRIPCION, nota.descripcion)
            }
            val whereClause = "$COLUMN_ID = ?"
            val whereArgs = arrayOf(nota.id.toString())
            val rowsUpdated = db.update(TABLE_NAME, values, whereClause, whereArgs)
            rowsUpdated
        } catch (e: Exception) {
            e.printStackTrace()
            0
        } finally {
            db.close()
        }
    }

    fun deleteNota(idNota: Int): Int {
        val db = writableDatabase
        return try {
            val whereClause = "$COLUMN_ID = ?"
            val whereArgs = arrayOf(idNota.toString())
            val rowsDeleted = db.delete(TABLE_NAME, whereClause, whereArgs)
            rowsDeleted
        } catch (e: Exception) {
            e.printStackTrace()
            0
        } finally {
            db.close()
        }
    }
}



