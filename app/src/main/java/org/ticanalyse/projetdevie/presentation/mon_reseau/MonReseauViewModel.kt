package org.ticanalyse.projetdevie.presentation.mon_reseau

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.domain.model.ActeursEducatifs
import org.ticanalyse.projetdevie.domain.model.ActeursFamiliauxEtSociaux
import org.ticanalyse.projetdevie.domain.model.ActeursInstitutionnelsEtDeSoutien
import org.ticanalyse.projetdevie.domain.model.ActeursProfessionnels
import org.ticanalyse.projetdevie.domain.model.MonReseau
import org.ticanalyse.projetdevie.domain.usecase.mon_reseau.MonReseauUseCases
import javax.inject.Inject

@HiltViewModel
class MonReseauViewModel @Inject constructor(
    private val monReseauUseCases: MonReseauUseCases
): ViewModel() {

    private val _upsertSuccess = MutableStateFlow(false)
    val upsertSuccess: StateFlow<Boolean> = _upsertSuccess.asStateFlow()

    private val _listActeursFamiliaux = MutableStateFlow<ActeursFamiliauxEtSociaux?>(null)
    val listActeursFamiliaux = _listActeursFamiliaux.asStateFlow()
    private val _listActeursEducatifs = MutableStateFlow<ActeursEducatifs?>(null)
    val listActeursEducatifs = _listActeursEducatifs.asStateFlow()
    private val _listActeursProfessionel = MutableStateFlow<ActeursProfessionnels?>(null)
    val listActeursProfessionel = _listActeursProfessionel.asStateFlow()
    private val _listActeursInstitutionel = MutableStateFlow<ActeursInstitutionnelsEtDeSoutien?>(null)
    val listActeursInstitutionel = _listActeursInstitutionel.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun resetUpsertSuccess() {
        _upsertSuccess.value = false
    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }

    fun getMonReseau() {
        viewModelScope.launch {
            _listActeursProfessionel.value = monReseauUseCases.getMonReseau()?.acteursProfessionnels
            _listActeursInstitutionel.value = monReseauUseCases.getMonReseau()?.acteursInstitutionnelsSoutien
            _listActeursEducatifs.value = monReseauUseCases.getMonReseau()?.acteursEducatifs
            _listActeursFamiliaux.value = monReseauUseCases.getMonReseau()?.acteursFamiliauxSociaux
        }
    }

    fun getReseauInfo(index: Int, category: String, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            val monReseau = monReseauUseCases.getMonReseau() ?: MonReseau(
                acteursFamiliauxSociaux = null,
                acteursInstitutionnelsSoutien = null,
                acteursProfessionnels = null,
                acteursEducatifs = null
            )

            val result = when (category) {
                "acteurFamiliauxSociaux" -> {
                    val info = monReseau.acteursFamiliauxSociaux ?: return@launch onResult(null)
                    when (index) {
                        0 -> info.parentsTuteurs
                        1 -> info.freresSoeursCousinsCousines
                        2 -> info.voisins
                        3 -> info.chefsCoutumiersReligieux
                        4 -> info.grandsParents
                        5 -> info.amisProches
                        6 -> info.mentorModeleCommunaute
                        7 -> info.leadersCommunautairesAssociationsLocales
                        else -> null
                    }
                }
                "acteurProfessionnel" -> {
                    val info = monReseau.acteursProfessionnels ?: return@launch onResult(null)
                    when (index) {
                        0 -> info.anciensEmployeursMaitresApprentissage
                        1 -> info.employesONGProjetsDeveloppement
                        2 -> info.artisansEntrepreneursLocaux
                        3 -> info.personnesRessourcesCooperativesGroupementsMutuelles
                        else -> null
                    }
                }
                "acteurEducatif" -> {
                    val info = monReseau.acteursEducatifs ?: return@launch onResult(null)
                    when (index) {
                        0 -> info.EnseignantsProfesseurs
                        1 -> info.EncadreursCentresFormationProfessionnelle
                        2 -> info.anciensCamaradesClasse
                        3 -> info.ConseillersOrientationScolaireProfessionnelle
                        4 -> info.animateursONGEducatives
                        else -> null
                    }
                }
                "acteurInstitutionnel" -> {
                    val info = monReseau.acteursInstitutionnelsSoutien ?: return@launch onResult(null)
                    when (index) {
                        0 -> info.agentsServicesSociauxAdministratifs
                        1 -> info.representantsStructuresCommeAgenceNationaleEmploi
                        2 -> info.formateursProgrammesPublicsPrivesFormationInsertion
                        3 -> info.personnelSante
                        else -> null
                    }
                }
                else -> null
            }

            onResult(result)
        }
    }

   fun upsertData(index:Int, category: String, nom:String, description:String, nom2:String, description2:String){
       viewModelScope.launch {

           try {

               val monReseau = monReseauUseCases.getMonReseau() ?: MonReseau(
                   acteursFamiliauxSociaux = null,
                   acteursInstitutionnelsSoutien = null,
                   acteursProfessionnels = null,
                   acteursEducatifs = null
               )

               val updatedMonReseau = when (category) {
                   "acteurFamiliauxSociaux" -> {
                       val old = monReseau.acteursFamiliauxSociaux ?: ActeursFamiliauxEtSociaux()
                       val newActeurs = old.copy(
                           parentsTuteurs = if (index == 0) "$nom|$description|$nom2|$description2" else old.parentsTuteurs,
                           freresSoeursCousinsCousines = if (index == 1) "$nom|$description|$nom2|$description2" else old.freresSoeursCousinsCousines,
                           voisins = if (index == 2) "$nom|$description|$nom2|$description2" else old.voisins,
                           chefsCoutumiersReligieux = if (index == 3) "$nom|$description|$nom2|$description2" else old.chefsCoutumiersReligieux,
                           grandsParents = if (index == 4) "$nom|$description|$nom2|$description2" else old.grandsParents,
                           amisProches = if (index == 5) "$nom|$description|$nom2|$description2" else old.amisProches,
                           mentorModeleCommunaute = if (index == 6) "$nom|$description|$nom2|$description2" else old.mentorModeleCommunaute,
                           leadersCommunautairesAssociationsLocales = if (index == 7) "$nom|$description|$nom2|$description2" else old.leadersCommunautairesAssociationsLocales,
                       )
                       monReseau.copy(acteursFamiliauxSociaux = newActeurs)
                   }
                   "acteurProfessionnel" -> {
                       val old = monReseau.acteursProfessionnels ?: ActeursProfessionnels()
                       val newActeurs = old.copy(
                           anciensEmployeursMaitresApprentissage = if (index == 0) "$nom|$description|$nom2|$description2" else old.anciensEmployeursMaitresApprentissage,
                           employesONGProjetsDeveloppement = if (index == 1) "$nom|$description|$nom2|$description2" else old.employesONGProjetsDeveloppement,
                           artisansEntrepreneursLocaux = if (index == 2) "$nom|$description|$nom2|$description2" else old.artisansEntrepreneursLocaux,
                           personnesRessourcesCooperativesGroupementsMutuelles = if (index == 3) "$nom|$description|$nom2|$description2" else old.personnesRessourcesCooperativesGroupementsMutuelles,
                       )
                       monReseau.copy(acteursProfessionnels = newActeurs)
                   }
                   "acteurEducatif" -> {
                       val old = monReseau.acteursEducatifs ?: ActeursEducatifs()
                       val newActeurs = old.copy(
                           EnseignantsProfesseurs = if (index == 0) "$nom|$description|$nom2|$description2" else old.EnseignantsProfesseurs,
                           EncadreursCentresFormationProfessionnelle = if (index == 1) "$nom|$description|$nom2|$description2" else old.EncadreursCentresFormationProfessionnelle,
                           anciensCamaradesClasse = if (index == 2) "$nom|$description|$nom2|$description2" else old.anciensCamaradesClasse,
                           ConseillersOrientationScolaireProfessionnelle = if (index == 3) "$nom|$description|$nom2|$description2" else old.ConseillersOrientationScolaireProfessionnelle,
                           animateursONGEducatives = if (index == 4) "$nom|$description|$nom2|$description2" else old.animateursONGEducatives,
                       )
                       monReseau.copy(acteursEducatifs = newActeurs)
                   }
                   "acteurInstitutionnel" -> {
                       val old = monReseau.acteursInstitutionnelsSoutien ?: ActeursInstitutionnelsEtDeSoutien()
                       val newActeurs = old.copy(
                           agentsServicesSociauxAdministratifs = if (index == 0) "$nom|$description|$nom2|$description2" else old.agentsServicesSociauxAdministratifs,
                           representantsStructuresCommeAgenceNationaleEmploi = if (index == 1) "$nom|$description|$nom2|$description2" else old.representantsStructuresCommeAgenceNationaleEmploi,
                           formateursProgrammesPublicsPrivesFormationInsertion = if (index == 2) "$nom|$description|$nom2|$description2" else old.formateursProgrammesPublicsPrivesFormationInsertion,
                           personnelSante = if (index == 3) "$nom|$description|$nom2|$description2" else old.personnelSante,
                       )


                       monReseau.copy(acteursInstitutionnelsSoutien = newActeurs)
                   }
                   else -> monReseau
               }

               monReseauUseCases.upsertMonReseau(updatedMonReseau)
               _upsertSuccess.value = true
               _errorMessage.value = null
           }catch (e: Exception) {
               _errorMessage.value = "Une erreur est survenue lors de l'insertion"
               _upsertSuccess.value = false
           }

       }
   }
}