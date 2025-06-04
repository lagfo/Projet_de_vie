package org.ticanalyse.projetdevie.domain.usecase.app_entry

import org.ticanalyse.projetdevie.data.manager.LocalUserManager


class SaveAppEntry(
    private val localUserManager: LocalUserManager
){
    suspend operator fun invoke(){
        localUserManager.saveAppEntry()
    }
}
