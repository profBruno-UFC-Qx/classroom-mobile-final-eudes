package com.example.trabalho_livro_livre

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.trabalho_livro_livre.data.AppRepository
import com.example.trabalho_livro_livre.ui.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

// Definição do módulo de Injeção de Dependências do Koin
val appModule = module {
    // Provera uma única instância (Singleton) do seu repositório para o app todo
    single { AppRepository(androidContext()) }

    // Provera a ViewModel injetando o repositório automaticamente nela
    viewModel { MainViewModel(get()) }
}

class LivroLivreApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Inicializa o Koin assim que o aplicativo abre
        startKoin {
            androidContext(this@LivroLivreApplication)
            modules(appModule)
        }
    }
}