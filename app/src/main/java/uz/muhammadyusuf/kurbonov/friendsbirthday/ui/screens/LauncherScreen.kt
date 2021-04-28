package uz.muhammadyusuf.kurbonov.friendsbirthday.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.navigate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.muhammadyusuf.kurbonov.friendsbirthday.R
import uz.muhammadyusuf.kurbonov.friendsbirthday.navigation.Destinations
import uz.muhammadyusuf.kurbonov.friendsbirthday.ui.core.LocalNavController


@Composable
fun LauncherScreen() {
    val navController = LocalNavController.current
    Surface(color = MaterialTheme.colors.primary) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.align(Alignment.Center)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    modifier = Modifier
                        .padding(4.dp)
                        .scale(2f)
                        .align(Alignment.CenterHorizontally),
                    contentDescription = "icon"
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.h4.copy(color = Color.White)
                )
            }
        }
        val scope = rememberCoroutineScope()
        scope.launch {
            delay(3000)
            navController.navigate(Destinations.HOME_SCREEN)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewLauncherScreen() {
    LauncherScreen()
}
