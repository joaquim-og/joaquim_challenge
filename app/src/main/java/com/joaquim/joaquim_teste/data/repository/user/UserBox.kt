package com.joaquim.joaquim_teste.data.repository.user

import android.util.Log
import com.joaquim.joaquim_teste.data.commom.ErrorsTags.USER_NOT_CREATED
import com.joaquim.joaquim_teste.data.commom.ObjectBox
import com.joaquim.joaquim_teste.data.commom.SharedPrefs
import com.joaquim.joaquim_teste.data.commom.USER_UID
import com.joaquim.joaquim_teste.data.model.user.LocalObjectBoxDbUser
import com.joaquim.joaquim_teste.data.model.user.LocalObjectBoxDbUser_

class UserBox : UserRepository {

    private val userBox = ObjectBox.boxStore.boxFor(LocalObjectBoxDbUser::class.java)

    override fun createLocalUser(
        user: LocalObjectBoxDbUser
    ) {

        val userExists = getLocalUser(user.userUid)

        try {

            if (userExists == null) {
                userBox.put(user)

                user.userUid?.let { setLocalUserUid(it) }
            } else {
                userBox.put(userExists)

                userExists.userUid?.let { setLocalUserUid(it) }
            }
        } catch (e: Error) {
            Log.d(USER_NOT_CREATED, "Here why -> ${e.localizedMessage}")
        }

    }

    override fun getLocalUser(userUid: String?): LocalObjectBoxDbUser? {
        val query =
            userBox.query(LocalObjectBoxDbUser_.userUid.equal(userUid))
                .build()

        return query.findUnique()
    }

    private fun setLocalUserUid(userUid: String) = SharedPrefs.setUserData(USER_UID, userUid)

}