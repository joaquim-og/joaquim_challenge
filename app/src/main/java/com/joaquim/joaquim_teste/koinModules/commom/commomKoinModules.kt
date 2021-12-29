package com.joaquim.joaquim_teste.koinModules.commom

import android.net.ConnectivityManager
import androidx.core.content.ContextCompat
import com.joaquim.joaquim_teste.MyApplication.Companion.globalContext
import com.joaquim.joaquim_teste.data.commom.CheckPermissions
import com.joaquim.joaquim_teste.data.commom.OSIntentsApi
import com.joaquim.joaquim_teste.data.commom.SetToastMessage
import com.joaquim.joaquim_teste.data.commom.VerifyNetwork
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

fun injectCommonClassesModule() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
        listOf(
            verifyNetworkStatus,
            setToastMessageModule,
            checkPermissions,
            osIntentsAPi
        )
    )
}

val verifyNetworkStatus: Module = module {
    single {
        VerifyNetwork(
            ContextCompat.getSystemService(
                globalContext,
                ConnectivityManager::class.java
            )
        )
    }
}

val setToastMessageModule: Module = module {
    single { SetToastMessage() }
}

val checkPermissions: Module = module {
    single { CheckPermissions() }
}

val osIntentsAPi: Module = module {
    single { OSIntentsApi() }
}





