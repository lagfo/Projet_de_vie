package org.ticanalyse.projetdevie.domain.usecase.user


data class UserUseCases(
    val getUser: GetUser,
    val upsertUser: UpsertUser
)