package uz.muhammadyusuf.kurbonov.friendsbirthday

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
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


        val intent = Intent()

        when (Build.MANUFACTURER) {
            "xiaomi" -> intent.component = ComponentName(
                "com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity"
            )
            "oppo" -> intent.component = ComponentName(
                "com.coloros.safecenter",
                "com.coloros.safecenter.permission.startup.StartupAppListActivity"
            )
            "vivo" -> intent.component = ComponentName(
                "com.vivo.permissionmanager",
                "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
            )
        }

        val arrayList = packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )

        if (arrayList.size > 0) {
            startActivity(intent)
        }

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
