package br.com.alura.meetups.ui.viewmodel

import androidx.lifecycle.ViewModel
import br.com.alura.meetups.repository.EventRepository

class ListaEventoViewModel(private val repository: EventRepository) : ViewModel() {

    fun buscaTodos() = repository.buscaTodos()

}