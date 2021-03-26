package uz.muhammadyusuf.kurbonov.friendsbirthday.ui.core

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController

val LocalNavController = compositionLocalOf<NavController> {
    error("CompositionLocal NavController not present")
}
