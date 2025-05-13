package org.ticanalyse.projetdevie.domain.usecase.app_entry

import org.ticanalyse.projetdevie.domain.manger.LocalUserManger


class SaveAppEntry(
    private val localUserManger: LocalUserManger
){
    suspend operator fun invoke(){
        localUserManger.saveAppEntry()
    }
}
