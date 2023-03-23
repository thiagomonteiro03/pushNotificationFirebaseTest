package br.com.alura.meetups.ui.viewmodel

import androidx.lifecycle.ViewModel
import br.com.alura.meetups.repository.EventRepository

class ListaInscricoesViewModel(private val repository: EventRepository) : ViewModel() {

    fun buscaInscricoes() = repository.findSubscriptions()

}
