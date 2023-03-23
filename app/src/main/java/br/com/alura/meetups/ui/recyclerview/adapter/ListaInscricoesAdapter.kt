package br.com.alura.meetups.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.alura.meetups.R
import br.com.alura.meetups.model.Event
import coil.load
import kotlinx.android.synthetic.main.inscricao_item.view.*

class ListaInscricoesAdapter(
    private val context: Context,
    inscricoes: List<Event> = listOf(),
    val cliqueNoItem: (id: String) -> Unit,
) : RecyclerView.Adapter<ListaInscricoesAdapter.ViewHolder>() {

    private val inscricoes = inscricoes.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.inscricao_item,
                parent,
                false
            ))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.vincula(inscricoes[position])
    }

    override fun getItemCount(): Int = inscricoes.size

    fun atualiza(inscricoes: List<Event>) {
        this.inscricoes.clear()
        this.inscricoes.addAll(inscricoes)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titulo = itemView.inscricao_item_titulo
        private val imagem = itemView.inscricao_item_imagem
        private lateinit var event: Event

        init {
            itemView.setOnClickListener {
                if (::event.isInitialized) {
                    cliqueNoItem(this.event.id)
                }
            }
        }

        fun vincula(event: Event) {
            this.event = event
            titulo.text = this.event.titulo
            val uri = this.event.imagem
            imagem.load(uri)
            if (uri.isNullOrBlank()) {
                imagem.visibility = GONE
            } else {
                imagem.visibility = VISIBLE
            }
        }

    }

}
