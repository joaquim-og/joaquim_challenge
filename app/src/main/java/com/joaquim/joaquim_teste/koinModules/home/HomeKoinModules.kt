package com.joaquim.joaquim_teste.koinModules.home

import com.joaquim.joaquim_teste.ui.HomeEventViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

fun injectHomeModule() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
        listOf(
            viewModelModule,
        )
    )
}

val viewModelModule: Module = module {
    viewModel {
        HomeEventViewModel(get(), get())
    }
}




