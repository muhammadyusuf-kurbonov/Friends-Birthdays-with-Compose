package uz.muhammadyusuf.kurbonov.friendsbirthday.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import uz.muhammadyusuf.kurbonov.friendsbirthday.ui.screens.AddScreen
import uz.muhammadyusuf.kurbonov.friendsbirthday.ui.screens.HomeScreen
import uz.muhammadyusuf.kurbonov.friendsbirthday.ui.screens.LauncherScreen

val navGraph: NavGraphBuilder.() -> Unit = {
    composable(Destinations.HOME_SCREEN){
        HomeScreen()
    }
    composable(Destinations.ADD_SCREEN){
        AddScreen()
    }
    composable(Destinations.LAUNCHER_SCREEN){
        LauncherScreen()
    }
}

