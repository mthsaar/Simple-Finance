package com.saar.silmplefinance.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.saar.silmplefinance.MainActivity
import com.saar.silmplefinance.R
import com.saar.silmplefinance.entity.Cadastro

class ElementoListaAdapter (val context: Context, val registros: MutableList<Cadastro>): BaseAdapter() {

    override fun getCount(): Int {
        return registros.size
    }

    override fun getItem(pos: Int): Any? {
        val cadastro = Cadastro(
            registros[pos].id,
            registros[pos].nome,
            registros[pos].tipo,
            registros[pos].valor,
            registros[pos].data
        )
        return cadastro
    }

    override fun getItemId(pos: Int): Long {
        return registros[pos].id?.toLong() ?: 0L
    }

    override fun getView(
        pos: Int,
        componenteOrigem: View?,
        rootComponent: ViewGroup?
    ): View? {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate( R.layout.elemento_lista, null)

        val tvNomeElementoLista: TextView = v.findViewById(R.id.tvNomeElementoLista)
        val tvValorElementoLista: TextView = v.findViewById(R.id.tvValorElementoLista)
        val tvDataElementoLista: TextView = v.findViewById(R.id.tvDataElementoLista)
        val tvTipoDeLancamentoLista: TextView = v.findViewById(R.id.tvTipoDeLancamentoLista)
        
        val imageView: ImageView = v.findViewById(R.id.imageView)
        val btEditarElementoLista: ImageButton = v.findViewById(R.id.btEditarElementoLista)

        tvNomeElementoLista.text = registros[pos].nome
        tvValorElementoLista.text = registros[pos].valor.toString()
        tvDataElementoLista.text = registros[pos].data
        tvTipoDeLancamentoLista.text = registros[pos].tipo

        if (registros[pos].tipo.equals("crédito", ignoreCase = true)) {
            imageView.setImageResource(R.drawable.outline_input_circle_24)
        } else {
            imageView.setImageResource(R.drawable.outline_output_circle_24)
        }

        btEditarElementoLista.setOnClickListener {
            val intent = Intent( context, MainActivity::class.java )
            intent.putExtra( "id", registros[pos].id )
            intent.putExtra( "nome", registros[pos].nome )
            intent.putExtra( "valor", registros[pos].valor )
            intent.putExtra("data", registros[pos].data)
            intent.putExtra("tipo", registros[pos].tipo)
            context.startActivity( intent )
        }

        return v
    }

}