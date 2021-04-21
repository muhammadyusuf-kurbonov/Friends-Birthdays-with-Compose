package uz.muhammadyusuf.kurbonov.friendsbirthday.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.navigate
import uz.muhammadyusuf.kurbonov.friendsbirthday.R
import uz.muhammadyusuf.kurbonov.friendsbirthday.initTimber
import uz.muhammadyusuf.kurbonov.friendsbirthday.model.BirthdayEntity
import uz.muhammadyusuf.kurbonov.friendsbirthday.navigation.Destinations
import uz.muhammadyusuf.kurbonov.friendsbirthday.prettifyDate
import uz.muhammadyusuf.kurbonov.friendsbirthday.ui.core.LocalNavController
import uz.muhammadyusuf.kurbonov.friendsbirthday.viewmodels.HomeViewModel
import kotlin.random.Random

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    initTimber()
    val context = LocalContext.current
    val model = viewModel<HomeViewModel>()

    model.initialize(context)

    val items = model.allBirthdays.collectAsState(initial = emptyList())

    val navController = LocalNavController.current

    Scaffold(modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            AddButton {
                navController.navigate(Destinations.ADD_SCREEN)
            }
        }, topBar = {
            TopAppBar(title = {
                Text(
                    text = "Birthdays",
                    modifier = Modifier.padding(4.dp),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle1
                )
            })
        }
    ) {
        HomeList(items = items.value)
    }
}

// Components of home screen

@Composable
private fun HomeList(
    modifier: Modifier = Modifier,
    items: List<BirthdayEntity>
) {
    Column(modifier = modifier) {
        LazyColumn(content = {
            items(items.size) {
                HomeListItem(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(8.dp),
                    item = items[it]
                )
            }
        })
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun HomeListItem(
    modifier: Modifier = Modifier,
    item: BirthdayEntity
) {
    val color = remember {
        arrayOf(
            Color.Blue,
            Color.Red,
            Color.Green,
            Color.DarkGray
        )[Random.nextInt(0, 4)]
    }
    var expanded by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = modifier.apply {
            if (expanded)
                fillMaxHeight()
        },
        backgroundColor = color,
        contentColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .apply {
                    if (expanded)
                        fillMaxSize()
                }
                .padding(4.dp)
                .clickable { expanded = !expanded },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                var name = item.name

                if (item.isBirthdayToday())
                    name = "🎉\uD83C\uDF89\uD83C\uDF89 $name \uD83C\uDF89\uD83C\uDF89\uD83C\uDF89"

                Text(
                    text = name,
                    modifier = Modifier
                        .padding(4.dp),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h5
                )
                AnimatedVisibility(visible = expanded) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = item.birthday.prettifyDate(),
                            style = MaterialTheme.typography.body2
                        )
                        Text(
                            text = item.phone
                        )
                    }
                }
            }
        }
    }
    Divider()
}


@Composable
private fun AddButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    FloatingActionButton(onClick = {
        onClick()
    }, modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_add_24),
            tint = Color.White,
            contentDescription = "icon"
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeListItem() {
    HomeListItem(
        item = BirthdayEntity(
            name = "Androider",
            birthday = "2002-05-22"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeList() {
    val list = listOf(
        BirthdayEntity(
            name = "Androider",
            birthday = "2002-05-22"
        ),
        BirthdayEntity(
            name = "Androider Test2",
            birthday = "2005-04-19"
        ), BirthdayEntity(
            name = "Androider",
            birthday = "2002-05-22"
        ),
        BirthdayEntity(
            name = "Androider Test2",
            birthday = "2005-04-19"
        ), BirthdayEntity(
            name = "Androider",
            birthday = "2002-05-22"
        ),
        BirthdayEntity(
            name = "Androider Test2",
            birthday = "2005-04-19"
        ), BirthdayEntity(
            name = "Androider",
            birthday = "2002-05-22"
        ),
        BirthdayEntity(
            name = "Androider Test2",
            birthday = "2005-04-19"
        ), BirthdayEntity(
            name = "Androider",
            birthday = "2002-05-22"
        ),
        BirthdayEntity(
            name = "Androider Test2",
            birthday = "2005-04-19"
        ), BirthdayEntity(
            name = "Androider",
            birthday = "2002-05-22"
        ),
        BirthdayEntity(
            name = "Androider Test2",
            birthday = "2005-04-19"
        ),
        BirthdayEntity(
            name = "Androider Test3",
            birthday = "2007-02-10"
        )
    )

    HomeList(items = list, modifier = Modifier.padding(4.dp))
}
