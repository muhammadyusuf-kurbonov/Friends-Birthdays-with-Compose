package uz.muhammadyusuf.kurbonov.friendsbirthday.ui.screens

import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.window.PopupProperties
import androidx.loader.content.CursorLoader
import androidx.navigation.compose.navigate
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.launch
import timber.log.Timber
import uz.muhammadyusuf.kurbonov.friendsbirthday.R
import uz.muhammadyusuf.kurbonov.friendsbirthday.database.AppDatabase
import uz.muhammadyusuf.kurbonov.friendsbirthday.formatAsDate
import uz.muhammadyusuf.kurbonov.friendsbirthday.model.BirthdayEntity
import uz.muhammadyusuf.kurbonov.friendsbirthday.model.Contact
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
    val context = LocalContext.current

    // states

    var nameTextState by remember {
        mutableStateOf("")
    }

    var phoneState by remember {
        mutableStateOf(TextFieldValue("+998", TextRange(4)))
    }

    var selectedContact by remember {
        mutableStateOf<Contact?>(null)
    }

    fun setSelectedContact(contact: Contact?) {
        if (contact == selectedContact)
            return
        selectedContact = contact
        if (contact === null)
            return
        nameTextState = contact.name
        phoneState = TextFieldValue(contact.phone)
    }

    var birthday by remember {
        mutableStateOf(System.currentTimeMillis())
    }

    val scrollState = rememberScrollState()

    val scope = rememberCoroutineScope()
    val instance = PhoneNumberUtil.createInstance(context)
    // End states

    val contactsList = remember {
        mutableListOf<Contact>()
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) return@rememberLauncherForActivityResult
            scope.launch {
//
//                val cr: ContentResolver = context.contentResolver
//                val cur = cr.query(
//                    ContactsContract.Contacts.CONTENT_URI,
//                    null, null, null, null
//                )
//                if (cur?.count ?: 0 > 0) {
//                    while (cur != null && cur.moveToNext()) {
//                        val id = cur.getString(
//                            cur.getColumnIndex(ContactsContract.Contacts._ID)
//                        )
//                        val name = cur.getString(
//                            cur.getColumnIndex(
//                                ContactsContract.Contacts.DISPLAY_NAME
//                            )
//                        )
//                        if (cur.getInt(
//                                cur.getColumnIndex(
//                                    ContactsContract.Contacts.HAS_PHONE_NUMBER
//                                )
//                            ) > 0
//                        ) {
//                            val pCur = cr.query(
//                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                                null,
//                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf(id), null
//                            )
//                            while (pCur!!.moveToNext()) {
//                                val phoneNo = pCur.getString(
//                                    pCur.getColumnIndex(
//                                        ContactsContract.CommonDataKinds.Phone.NUMBER
//                                    )
//                                )
//                                val element = Contact(name, phoneNo)
//                                contactsList.add(element)
//                                Timber.tag("Contacts").d("Added contact $element")
//                            }
//                            pCur.close()
//                        }
//                    }
//                }
//                cur?.close()
//
                val mCursorLoader = CursorLoader(
                    context,
                    ContactsContract.Data.CONTENT_URI, arrayOf(
                        ContactsContract.Data.DISPLAY_NAME_PRIMARY,
                        ContactsContract.Data.LOOKUP_KEY,
                        ContactsContract.Data._ID,
                        ContactsContract.Data.DATA1
                    ),
                    "NOT (" + ContactsContract.Data.DATA1 + "=" + ContactsContract.Data.DISPLAY_NAME_PRIMARY + ")",
                    null,
                    null
                )

                val contactsCursor = mCursorLoader.loadInBackground()
                Timber.tag("Contacts").d("contactsCursor is $contactsCursor")
                contactsCursor?.let { cursor ->
                    if (cursor.moveToFirst()) {
                        Timber.tag("Contacts").d("cursor is $cursor")
                        contactsList.clear()
                        do {
                            val contact = Contact("", "")
                            contact.name =
                                cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME))
                            contact.phone =
                                cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DATA1))
                            contactsList.add(contact)
                            Timber.tag("Contacts").d("List is $contactsList")
                        } while (cursor.moveToNext())
                    }
                }

            }
        }

    LaunchedEffect(key1 = scope.toString(), block = {
        launcher.launch("android.permission.READ_CONTACTS")
    })

    Column(modifier = modifier.verticalScroll(scrollState)) {
        Text(
            text = "\tAdd new",
            modifier = Modifier.padding(4.dp),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.subtitle1
        )

        AutocompleteTextInput(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            text = nameTextState,
            hint = "Name",
            icon = R.drawable.ic_baseline_person_24,
            onValueChange = { nameTextState = it },
            items = contactsList.filter {
                it.name.contains(nameTextState, true)
            },
            onItemSelected = {
                setSelectedContact(it)
            }
        )

        val currentNumber = try {
            instance.format(
                instance.parse(phoneState.text, Locale.getDefault().country),
                PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL
            )
        } catch (e: Exception) {
            Timber.e(e)
            phoneState.text
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            leadingIcon = {
                Image(
                    painter = painterResource(
                        id = R.drawable.ic_baseline_local_phone_24
                    ), colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
                    contentDescription = "Phone"
                )
            },
            value = TextFieldValue(currentNumber, TextRange(currentNumber.length)),
            onValueChange = {
                val replace = it.text.replace("() ", "")
                phoneState = TextFieldValue(replace, TextRange(it.text.length))
                setSelectedContact(null)
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
                            birthday = openDatePickerDialog(context)
                        }
                },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_calendar_today_24),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
                    contentDescription = "Date"
                )
            },
            value = birthday.prettyDate(),
            onValueChange = {},
            label = { Text(text = "Birthday") },
            readOnly = true
        )

        Button(
            onClick = {
                scope.launch {
                    AppDatabase.getInstance(context).getDatabaseController().insertEntity(
                        BirthdayEntity(
                            name = nameTextState,
                            phone = phoneState.text.replace("()- ", ""),
                            birthday = birthday.formatAsDate("YYYY-MM-dd")
                        )
                    )
                    navController.navigate(Destinations.HOME_SCREEN)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Column()
            {
                Text(text = "Add", modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun AutocompleteTextInput(
    modifier: Modifier = Modifier,
    items: List<Contact> = emptyList(),
    text: String = "",
    icon: Int = -1,
    hint: String = "",
    onValueChange: (String) -> Unit = {},
    onItemSelected: (Contact?) -> Unit = {}
) {
    val dropdownShowState = remember { mutableStateOf(false) }

    var tvf by remember {
        mutableStateOf(
            TextFieldValue(text = text, selection = TextRange(text.length))
        )
    }

    dropdownShowState.value = (items.isNotEmpty() && items[0].name != text)
            && text.length > 2


    Column {

        OutlinedTextField(value = tvf,
            onValueChange = {
                Timber.d("Value changed to $it")
                tvf = it
                onValueChange(tvf.text)
                onItemSelected(null)
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
            modifier = Modifier.fillMaxWidth(),
            expanded = dropdownShowState.value,
            onDismissRequest = {
                dropdownShowState.value = false
            },
            properties = PopupProperties()
        ) {
            items.forEach {
                DropdownMenuItem(onClick = {
                    onValueChange(it.name)
                    onItemSelected(it)
                    tvf =
                        TextFieldValue(text = it.name, selection = TextRange(it.name.length))
                    dropdownShowState.value = false
                }) {
                    Text(text = it.name)
                }
            }
        }
    }
}
