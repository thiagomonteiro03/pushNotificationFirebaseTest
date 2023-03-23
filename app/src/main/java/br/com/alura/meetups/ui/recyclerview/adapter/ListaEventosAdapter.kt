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
import kotlinx.android.synthetic.main.evento_item.view.*

class ListaEventosAdapter(
    private val context: Context,
    events: List<Event> = listOf(),
    val cliqueNoItem: (id: String) -> Unit,
) : RecyclerView.Adapter<ListaEventosAdapter.ViewHolder>() {

    private val eventos = events.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(context)
            .inflate(
                R.layout.evento_item,
                parent,
                false
            ))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.vincula(eventos[position])
    }

    override fun getItemCount(): Int = eventos.size

    fun atualiza(events: List<Event>) {
        this.eventos.clear()
        this.eventos.addAll(events)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titulo = itemView.evento_item_titulo
        private val descricao = itemView.evento_item_descricao
        private val imagem = itemView.evento_item_imagem
        private val containerInscritos = itemView.evento_item_container_inscritos
        private val inscritos = itemView.evento_item_inscritos
        private lateinit var event: Event

        init {
            itemView.setOnClickListener {
                if (::event.isInitialized) {
                    cliqueNoItem(event.id)
                }
            }
        }

        fun vincula(event: Event) {
            this.event = event
            configuraImagem()
            configuraInscritos()
            preencheCampos()
        }

        private fun configuraInscritos() {
            if (this.event.inscritos > 0) {
                containerInscritos.visibility = VISIBLE
            } else {
                containerInscritos.visibility = GONE
            }
        }

        private fun configuraImagem() {
            if (this.event.imagem.isNullOrBlank()) {
                imagem.visibility = GONE
            } else {
                imagem.visibility = VISIBLE
            }
        }

        private fun preencheCampos() {
            titulo.text = this.event.titulo
            descricao.text = this.event.descricao
            imagem.load(this.event.imagem)
            inscritos.text = "${event.inscritos}"
        }

    }

}
