package br.com.alura.meetups.ui.viewmodel

import androidx.lifecycle.ViewModel
import br.com.alura.meetups.repository.EventRepository

class DetalhesEventoViewModel(private val repository: EventRepository) : ViewModel() {

    fun buscaEvento(id: String) =
        repository.buscaEvento(id)

    fun inscreve(eventoId: String) =
        repository.subscribe(eventoId)

    fun cancela(eventoId: String) =
        repository.cancel(eventoId)

}
