package br.com.alura.meetups.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import br.com.alura.meetups.model.Event
import br.com.alura.meetups.model.User
import br.com.alura.meetups.webclient.EventService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import java.net.SocketTimeoutException

class EventRepository(
    private val service: EventService,
) {

    private val email get() = FirebaseAuth.getInstance().currentUser?.email

    fun buscaTodos(): LiveData<Result<List<Event>>?> =
        liveData(Dispatchers.IO) {
            val resultado =
                executeService(mensagemDeErro = "Falha ao buscar eventos") {
                    service.buscaTodos()
                }
            emit(resultado)
        }

    fun buscaEvento(id: String): LiveData<Result<Event>?> =
        liveData(Dispatchers.IO) {
            val resultado =
                executeService(mensagemDeErro = "Falha ao buscar evento") {
                    val email = FirebaseAuth.getInstance().currentUser?.email
                    service.buscaPorId(id, email)
                }
            emit(resultado)
        }

    fun subscribe(eventId: String): LiveData<Result<Boolean>> =
        liveData(Dispatchers.IO) {
            val result =
                executeServiceWithoutResult(errorMessage = "Falha ao inscrever no evento") {
                    service.subscribe(eventId, User(email))
                }
            result.dado?.let { success ->
                if (success){
                    FirebaseMessaging.getInstance().subscribeToTopic(eventId)
                }
            }
            emit(result)
        }

    fun cancel(eventId: String): LiveData<Result<Boolean>> =
        liveData(Dispatchers.IO) {
            val result =
                executeServiceWithoutResult(errorMessage = "Falha ao cancelar inscrição") {
                    service.cancel(eventId, User(email))
                }
            result.dado?.let { success ->
                if (success) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(eventId)
                }
            }
            emit(result)
        }

    fun findSubscriptions(): LiveData<Result<List<Event>>> =
        liveData(Dispatchers.IO) {
            val result = email?.let { email ->
                executeService(mensagemDeErro = "Falha ao buscar inscrições") {
                    service.findSubscriptions(email)
                }
            } ?: Result(erro = "Usuário não autenticado")
            result.dado?.let { subscriptions ->
                subscriptions.forEach {
                    FirebaseMessaging.getInstance().subscribeToTopic(it.id)
                }
            }
            emit(result)
        }

    private suspend fun executeServiceWithoutResult(
        errorMessage: String,
        executa: suspend () -> Response<Unit>,
    ): Result<Boolean> {
        return try {
            criaResultadoSemResposta(executa(), errorMessage)
        } catch (e: SocketTimeoutException) {
            Result(erro = "Falha ao conectar com o servidor")
        } catch (e: Exception) {
            Result(erro = "Erro desconhecido")
        }
    }

    private fun <T> criaResultado(
        resposta: Response<T>,
        mensagemDeErro: String? = null,
    ): Result<T>? {
        if (resposta.isSuccessful) {
            return resposta.body()?.let { Result(it) }
        }
        return Result(erro = mensagemDeErro)
    }

    private suspend fun <T> executeService(
        mensagemDeErro: String = "Falha na requisição",
        executa: suspend () -> Response<T>,
    ): Result<T>? {
        return try {
            criaResultado(executa(), mensagemDeErro)
        } catch (e: SocketTimeoutException) {
            Result(erro = "Falha ao conectar com o servidor")
        } catch (e: Exception) {
            Result(erro = "Erro desconhecido")
        }
    }

    private fun criaResultadoSemResposta(
        sucesso: Response<Unit>,
        mensagemDeErro: String? = null,
    ): Result<Boolean> {
        if (sucesso.isSuccessful) {
            return Result(true)
        }
        return Result(false, mensagemDeErro)
    }

}
