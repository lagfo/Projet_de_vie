package org.ticanalyse.projetdevie.presentation.mon_reseau

import androidx.lifecycle.ViewModel
import org.ticanalyse.projetdevie.domain.model.MonReseau
import org.ticanalyse.projetdevie.domain.usecase.mon_reseau.MonReseauUseCases
import javax.inject.Inject

class MonReseauViewModel @Inject constructor(
    private val monReseauUseCases: MonReseauUseCases
): ViewModel() {

    suspend fun getReseau():MonReseau{
        return monReseauUseCases.getMonReseau.invoke()
    }



}