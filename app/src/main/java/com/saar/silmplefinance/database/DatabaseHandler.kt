package com.saar.silmplefinance.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.saar.silmplefinance.entity.Cadastro

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "SimpleFinance.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "cadastro"
        private const val ID = "id"
        private const val NOME = "nome"
        private const val TIPO = "tipo"
        private const val VALOR = "valor"
        private const val DATA = "data"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY,"
                + NOME + " TEXT,"
                + TIPO + " TEXT,"
                + VALOR + " TEXT,"
                + DATA + " TEXT" + ")")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun incluir(cadastro: Cadastro) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, cadastro.id)
        contentValues.put(NOME, cadastro.nome)
        contentValues.put(TIPO, cadastro.tipo)
        contentValues.put(VALOR, cadastro.valor)
        contentValues.put(DATA, cadastro.data)

        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    fun alterar(cadastro: Cadastro) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NOME, cadastro.nome)
        contentValues.put(TIPO, cadastro.tipo)
        contentValues.put(VALOR, cadastro.valor)
        contentValues.put(DATA, cadastro.data)

        db.update(TABLE_NAME, contentValues, "$ID = ?", arrayOf(cadastro.id.toString()))
        db.close()
    }

    fun excluir(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun pesquisar(id: Int): Cadastro? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME, arrayOf(ID, NOME, TIPO, VALOR, DATA),
            "$ID = ?", arrayOf(id.toString()), null, null, null, null
        )

        var cadastro: Cadastro? = null
        if (cursor != null && cursor.moveToFirst()) {
            cadastro = Cadastro(
                cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(NOME)),
                cursor.getString(cursor.getColumnIndexOrThrow(TIPO)),
                cursor.getString(cursor.getColumnIndexOrThrow(VALOR)),
                cursor.getString(cursor.getColumnIndexOrThrow(DATA))
            )
            cursor.close()
        }
        db.close()
        return cadastro
    }

    fun listar(): MutableList<Cadastro> {
        val lista = mutableListOf<Cadastro>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val cadastro = Cadastro(
                    cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(NOME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TIPO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(VALOR)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DATA))
                )
                lista.add(cadastro)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
}
