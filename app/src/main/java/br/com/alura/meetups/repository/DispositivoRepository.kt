package br.com.alura.meetups.repository

import android.util.Log
import br.com.alura.meetups.model.Device
import br.com.alura.meetups.preferences.FirebaseTokenPreferences
import br.com.alura.meetups.webclient.DispositivoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "DispositivoRepository"

class DispositivoRepository(
    private val service: DispositivoService,
    private val preferences: FirebaseTokenPreferences
) {

    fun save(device: Device) {
        CoroutineScope(Dispatchers.IO).launch {
            val resposta = service.salva(device)
            if (resposta.isSuccessful) {
                Log.i(TAG, "salva: token " +
                        "enviado ${device.token}")
                preferences.tokenEnviado()
            } else {
                Log.i(TAG, "salva: falha ao enviar o token")
            }
        }
    }

}