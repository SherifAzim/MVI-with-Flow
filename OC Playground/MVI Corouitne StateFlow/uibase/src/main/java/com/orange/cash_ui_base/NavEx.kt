package com.orange.cash_ui_base


import android.os.Bundle
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions

fun buildDeepLink(destination: CashDeepLinkDest) =
    NavDeepLinkRequest.Builder
        .fromUri(destination.address.toUri())
        .build()


fun NavController.deepLinkNavigateTo(
    deepLinkDestination: CashDeepLinkDest,
    popUpTo: Boolean = false,
    extras: Bundle? = null
) {
    val builder = NavOptions.Builder()

    if (popUpTo) {
        builder.setPopUpTo(graph.startDestinationId, true)
    }

    navigate(
        buildDeepLink(deepLinkDestination),
        builder.build()
    )
}

fun NavController.deepLinkNavigateTo(
    deepLinkDestination: String,
    popUpTo: Boolean = false,
    extras: Bundle? = null
) {
    val builder = NavOptions.Builder()

    if (popUpTo) {
        builder.setPopUpTo(graph.startDestinationId, true)
    }

    navigate(
        NavDeepLinkRequest.Builder
            .fromUri(deepLinkDestination.toUri())
            .build(),
        builder.build()
    )
}

fun NavController.deepLinkNavigateTo(
    deepLinkDestination: CashDeepLinkDest,
    popUpToDest: Int,
    popUpTo: Boolean = false,
    extras: Bundle? = null
) {
    val builder = NavOptions.Builder()

    if (popUpTo && popUpToDest > 0) {
        builder.setPopUpTo(popUpToDest, true)
    }

    navigate(
        buildDeepLink(deepLinkDestination),
        builder.build()
    )
}

sealed class CashDeepLinkDest(val address: String) {
    object FirstFragment :
        CashDeepLinkDest("sherif://com.sherif.ocfeature.ui.FirstFragment")
}