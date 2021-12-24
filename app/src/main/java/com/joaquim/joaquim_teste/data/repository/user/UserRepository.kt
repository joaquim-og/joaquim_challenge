package com.joaquim.joaquim_teste.data.repository.user

import com.joaquim.joaquim_teste.data.model.user.LocalObjectBoxDbUser

interface UserRepository {

    fun createLocalUser(
        user: LocalObjectBoxDbUser,
        userCreated: (boolean: Boolean) -> Unit
    )

    fun getLocalUser(userUid: String?): LocalObjectBoxDbUser?

}