package org.ticanalyse.projetdevie.domain.usecase.app_entry

import kotlinx.coroutines.flow.Flow
import org.ticanalyse.projetdevie.data.manager.LocalUserManager

class ReadAppEntry(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<Boolean> {
        return localUserManager.readAppEntry()
    }
}