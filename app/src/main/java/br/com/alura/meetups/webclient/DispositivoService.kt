package br.com.alura.meetups.webclient

import br.com.alura.meetups.model.Device
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DispositivoService {

    @POST("devices")
    suspend fun salva(
        @Body device: Device,
    ): Response<Unit>

}