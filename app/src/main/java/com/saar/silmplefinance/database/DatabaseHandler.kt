package com.saar.silmplefinance.database

import android.content.Context
import com.saar.silmplefinance.entity.Cadastro
import kotlinx.coroutines.tasks.await
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class DatabaseHandler(context: Context) {

    private val banco = Firebase.firestore

    companion object {
        private const val TABLE_NAME = "cadastro"
        private const val ID = "id"
        private const val NOME = "nome"
        private const val TIPO = "tipo"
        private const val VALOR = "valor"
        private const val DATA = "data"
    }

    suspend fun incluir(cadastro: Cadastro) {

        val registro = hashMapOf(
            NOME to cadastro.nome,
            TIPO to cadastro.tipo,
            VALOR to cadastro.valor,
            DATA to cadastro.data

        )

        banco
            .collection( TABLE_NAME )
            .document(cadastro.id.toString())
            .set( registro )
            .await()
    }

    suspend fun alterar(cadastro: Cadastro) {

        val registro = hashMapOf(
            NOME to cadastro.nome,
            TIPO to cadastro.tipo,
            VALOR to cadastro.valor,
            DATA to cadastro.data
        )

        banco
            .collection( TABLE_NAME )
            .document(cadastro.id.toString())
            .set( registro )
            .await()

    }


    suspend fun excluir(id: Int) {

        banco
            .collection( TABLE_NAME )
            .document(id.toString())
            .delete()
            .await()

    }

    suspend fun pesquisar(id: Int): Cadastro? {

        val documento = banco
            .collection(TABLE_NAME)
            .document(id.toString())
            .get()
            .await()

        if (documento.exists()) {
            val cadastro = Cadastro(
                id,
                documento.get(NOME).toString(),
                documento.get(TIPO).toString(),
                documento.get(VALOR).toString(),
                documento.get(DATA).toString()

            )
            return cadastro
        } else {
            return null
        }

    }

    suspend fun listar(): MutableList<Cadastro> {

        val documentos = banco
            .collection(TABLE_NAME)
            .get()
            .await()

        val lista = mutableListOf<Cadastro>()

        for (documento in documentos.documents) {
            val cadastro = Cadastro(
                documento.id.toInt(),
                documento.get(NOME).toString(),
                documento.get(TIPO).toString(),
                documento.get(VALOR).toString(),
                documento.get(DATA).toString(),

            )
            lista.add(cadastro)
        }

        return lista

    }

}