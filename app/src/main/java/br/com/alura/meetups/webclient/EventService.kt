package br.com.alura.meetups.webclient

import br.com.alura.meetups.model.Event
import br.com.alura.meetups.model.User
import retrofit2.Response
import retrofit2.http.*

interface EventService {

    @GET("events")
    suspend fun buscaTodos(): Response<List<Event>>

    @GET("events/{id}/subscribed")
    suspend fun buscaPorId(
        @Path("id") id: String,
        @Query("email") email: String?,
    ): Response<Event>

    @POST("events/{id}/subscribe")
    suspend fun subscribe(
        @Path("id") eventoId: String,
        @Body user: User,
    ): Response<Unit>

    @PUT("events/{id}/unsubscribe")
    suspend fun cancel(
        @Path("id") eventId: String,
        @Body user: User,
    ): Response<Unit>

    @GET("events/subscriptions")
    suspend fun findSubscriptions(@Query("email") email: String): Response<List<Event>>

}
