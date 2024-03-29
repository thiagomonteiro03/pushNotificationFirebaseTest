package br.com.alura.meetups.di

import android.app.NotificationManager
import android.content.Context
import br.com.alura.meetups.notification.PrincipalChannel
import br.com.alura.meetups.preferences.FirebaseTokenPreferences
import br.com.alura.meetups.repository.DispositivoRepository
import br.com.alura.meetups.repository.EventRepository
import br.com.alura.meetups.ui.viewmodel.DetalhesEventoViewModel
import br.com.alura.meetups.ui.viewmodel.EstadoAppViewModel
import br.com.alura.meetups.ui.viewmodel.ListaEventoViewModel
import br.com.alura.meetups.ui.viewmodel.ListaInscricoesViewModel
import br.com.alura.meetups.webclient.DispositivoService
import br.com.alura.meetups.webclient.EventService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val URL_BASE = "http://192.168.1.3:8080/api/"

val retrofitModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<EventService> { get<Retrofit>().create(EventService::class.java) }
    single<DispositivoService> { get<Retrofit>().create(DispositivoService::class.java) }
    single<OkHttpClient> {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
}

val viewModelModule = module {
    viewModel<ListaEventoViewModel> { ListaEventoViewModel(get()) }
    viewModel<DetalhesEventoViewModel> { DetalhesEventoViewModel(get()) }
    viewModel<EstadoAppViewModel> { EstadoAppViewModel() }
    viewModel<ListaInscricoesViewModel> { ListaInscricoesViewModel(get()) }
}

val repositoryModule = module {
    single<EventRepository> { EventRepository(get()) }
    single<DispositivoRepository> { DispositivoRepository(get(), get()) }
}

val preferencesModule = module {
    single<FirebaseTokenPreferences> { FirebaseTokenPreferences(get()) }
}

val notificationModule = module {
    single<PrincipalChannel> { PrincipalChannel(get(), get()) }
    single<NotificationManager> { get<Context>().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
}

val appModules = listOf(
    retrofitModule,
    viewModelModule,
    repositoryModule,
    preferencesModule,
    notificationModule,
)

