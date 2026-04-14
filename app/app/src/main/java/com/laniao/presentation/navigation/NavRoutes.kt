package com.laniao.presentation.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes for LaNiao app.
 */
sealed interface NavRoutes {
    @Serializable
    data object Home : NavRoutes

    @Serializable
    data object Calendar : NavRoutes

    @Serializable
    data object Statistics : NavRoutes

    @Serializable
    data object Settings : NavRoutes

    @Serializable
    data object Schedule : NavRoutes

    @Serializable
    data class AddEntry(
        val entryId: Long? = null,  // null = new entry
        val quickAdd: Boolean = false
    ) : NavRoutes
}
