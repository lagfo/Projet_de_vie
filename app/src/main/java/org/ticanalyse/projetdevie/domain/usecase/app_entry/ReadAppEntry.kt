package org.ticanalyse.projetdevie.domain.usecase.app_entry

import kotlinx.coroutines.flow.Flow
import org.ticanalyse.projetdevie.domain.manger.LocalUserManger

class ReadAppEntry(
    private val localUserManger: LocalUserManger
) {
    operator fun invoke(): Flow<Boolean> {
        return localUserManger.readAppEntry()
    }
}