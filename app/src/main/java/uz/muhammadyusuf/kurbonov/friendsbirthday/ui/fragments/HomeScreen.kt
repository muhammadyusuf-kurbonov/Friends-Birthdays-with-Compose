package uz.muhammadyusuf.kurbonov.friendsbirthday.ui.fragments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import uz.muhammadyusuf.kurbonov.friendsbirthday.ui.core.LocalNavController
import uz.muhammadyusuf.kurbonov.friendsbirthday.viewmodels.HomeViewModel

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

    Box(modifier = modifier.fillMaxSize()) {

        HomeList(items = items.value)

        AddButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            navController.navigate(Destinations.ADD_SCREEN)
        }

    }
}

// Components of home screen
@Composable
private fun HomeList(
    modifier: Modifier = Modifier,
    items: List<BirthdayEntity>
) {
    LazyColumn(content = {
        item {
            Text(
                text = "Birthdays",
                modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1
            )
        }

        items(items.size) {
            HomeListItem(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(8.dp),
                item = items[it]
            )
        }
    }, modifier = modifier)
}

@Composable
private fun HomeListItem(
    modifier: Modifier = Modifier,
    item: BirthdayEntity
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.padding(4.dp)) {
            Text(
                text = item.name,
                modifier = Modifier.weight(1.0f),
                style = MaterialTheme.typography.body1
            )
            Text(
                text = item.birthday,
                style = MaterialTheme.typography.body2
            )
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
