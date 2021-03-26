package uz.muhammadyusuf.kurbonov.friendsbirthday.ui.fragments

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.navigate
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.launch
import timber.log.Timber
import uz.muhammadyusuf.kurbonov.friendsbirthday.R
import uz.muhammadyusuf.kurbonov.friendsbirthday.database.AppDatabase
import uz.muhammadyusuf.kurbonov.friendsbirthday.formatAsDate
import uz.muhammadyusuf.kurbonov.friendsbirthday.model.BirthdayEntity
import uz.muhammadyusuf.kurbonov.friendsbirthday.navigation.Destinations
import uz.muhammadyusuf.kurbonov.friendsbirthday.openDatePickerDialog
import uz.muhammadyusuf.kurbonov.friendsbirthday.prettyDate
import uz.muhammadyusuf.kurbonov.friendsbirthday.ui.core.LocalNavController
import java.util.*


@Composable
fun AddScreen(
    modifier: Modifier = Modifier
) {
    val navController = LocalNavController.current

    val nameTextState = remember {
        mutableStateOf("")
    }

    val birthday = remember {
        mutableStateOf(System.currentTimeMillis())
    }

    val phoneState = remember {
        mutableStateOf(TextFieldValue("+998", TextRange(4)))
    }

    val scrollState = rememberScrollState()

    // Edn states

    Column(modifier = modifier.verticalScroll(scrollState)) {
        Text(
            text = "\tAdd new",
            modifier = Modifier.padding(4.dp),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.subtitle1
        )

        val listOf = listOf(
            "Potato",
            "Tomato",
            "Margarin",
            "Mandarine",
            "Dioxine",
            "Butter",
            "Cake"
        )
        val items = listOf.filter {
            it.contains(nameTextState.value, true)
        }

        AutocompleteTextInput(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            text = nameTextState.value,
            hint = "Name",
            icon = R.drawable.ic_baseline_person_24,
            onValueChange = { nameTextState.value = it },
            items = items
        )
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        val instance = PhoneNumberUtil.createInstance(context)
        val currentNumber = try {
            instance.format(
                instance.parse(phoneState.value.text, Locale.getDefault().country),
                PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL
            )
        } catch (e: Exception) {
            Timber.e(e)
            phoneState.value.text
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            leadingIcon = {
                Image(
                    painter=painterResource(
                        id = R.drawable.ic_baseline_local_phone_24
                    ), colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
                    contentDescription = "Phone"
                )
            },
            value = TextFieldValue(currentNumber, TextRange(currentNumber.length)),
            onValueChange = {
                val replace = it.text.replace("() ", "")
                phoneState.value = TextFieldValue(replace, TextRange(it.text.length))
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            label = { Text(text = "Phone") }
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .onFocusChanged {
                    if (it == FocusState.Active)
                        scope.launch {
                            birthday.value = openDatePickerDialog(context)
                        }
                },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_calendar_today_24),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
                    contentDescription = "Date"
                )
            },
            value = birthday.value.prettyDate(),
            onValueChange = {},
            label = { Text(text = "Birthday") },
            readOnly = true
        )

        Button(onClick = { scope.launch {
            AppDatabase.getInstance(context).getDatabaseController().insertEntity(
                BirthdayEntity(
                    name = nameTextState.value,
                    phone = phoneState.value.text.replace("()- ", ""),
                    birthday = birthday.value.formatAsDate("YYYY-mm-dd")
                )
            )
            navController.navigate(Destinations.HOME_SCREEN)
        }},
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)) {
            Text(text = "Add", modifier = Modifier.padding(4.dp))
        }
    }
}

@Composable
fun AutocompleteTextInput(
    modifier: Modifier = Modifier,
    items: List<String> = emptyList(),
    text: String = "",
    icon: Int = -1,
    hint: String = "",
    onValueChange: (String) -> Unit = {}
) {
    val dropdownShowState = remember { mutableStateOf(false) }

    val tvf = remember {
        mutableStateOf(
            TextFieldValue(text = text, selection = TextRange(text.length))
        )
    }

    dropdownShowState.value = (items.isNotEmpty() && items[0] != text)
            && text.length > 2


    OutlinedTextField(value = tvf.value,
        onValueChange = {
            onValueChange(it.text)
            tvf.value = it
        },
        leadingIcon = {
            if (icon != -1)
                Image(
                    painter = painterResource(id = icon),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
                    contentDescription = "icon"
                )
        },
        modifier = modifier,
        label = {
            if (hint.isNotEmpty())
                Text(text = hint)
        })
    DropdownMenu(
        expanded = dropdownShowState.value,
        onDismissRequest = {
            dropdownShowState.value = false
        }) {
        items.forEach {
            DropdownMenuItem(onClick = {
                onValueChange(it)
                tvf.value = TextFieldValue(text = it, selection = TextRange(it.length))
                dropdownShowState.value = false
            }) {
                Text(text = it)
            }
        }
    }
}
