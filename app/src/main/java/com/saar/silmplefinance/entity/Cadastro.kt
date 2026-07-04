package com.saar.silmplefinance.entity

data class Cadastro(
    val id: Int? = null,
    val nome: String,
    val tipo: String, // "crédito" ou "débito"
    val valor: String,
    val data: String
)
