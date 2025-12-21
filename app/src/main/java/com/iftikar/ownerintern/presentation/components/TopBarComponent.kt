package com.iftikar.ownerintern.presentation.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarComponent(
    content: @Composable () -> Unit,
    rightIcon: @Composable (() -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        title = {content()},
        actions = {
            rightIcon?.invoke()
        }
    )
}