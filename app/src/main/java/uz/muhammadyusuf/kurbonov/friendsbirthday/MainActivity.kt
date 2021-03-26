package uz.muhammadyusuf.kurbonov.friendsbirthday

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.android.material.composethemeadapter.MdcTheme
import timber.log.Timber
import uz.muhammadyusuf.kurbonov.friendsbirthday.navigation.Destinations
import uz.muhammadyusuf.kurbonov.friendsbirthday.navigation.navGraph
import uz.muhammadyusuf.kurbonov.friendsbirthday.ui.core.LocalNavController

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            initTimber()
            val navController = rememberNavController()
            CompositionLocalProvider(
                LocalNavController provides navController
            ) {

                MdcTheme {
                    Timber.d("${LocalNavController.current}")
                    NavHost(
                        navController = navController,
                        startDestination = Destinations.LAUNCHER_SCREEN,
                        builder = navGraph
                    )
                }

            }
        }
    }
}
