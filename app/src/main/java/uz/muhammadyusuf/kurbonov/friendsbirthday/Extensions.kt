package uz.muhammadyusuf.kurbonov.friendsbirthday

import android.app.DatePickerDialog
import android.content.Context
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun initTimber(){
    if (BuildConfig.DEBUG && Timber.treeCount() == 0)
        Timber.plant(Timber.DebugTree())
}


fun Long.formatAsDate(
    pattern: String,
    newTimeZone: TimeZone = TimeZone.getDefault()
): String =
    SimpleDateFormat(pattern, Locale.getDefault()).apply {
        timeZone = newTimeZone
    }.format(Date(this))

fun Long.prettyDate(): String = formatAsDate("dd MMM YYYY")

fun String.reformatDate(
    oldFormat: String,
    newFormat: String,
    oldTimeZone: TimeZone = TimeZone.getDefault(),
    newTimeZone: TimeZone = TimeZone.getDefault()
): String {
    val date = SimpleDateFormat(oldFormat, Locale.getDefault())
        .apply {
            timeZone = oldTimeZone
        }
        .parse(this)

    val result = date?.time?.formatAsDate(newFormat, newTimeZone)
        ?: throw IllegalArgumentException("Wrong pattern. Check it")
    Timber.d("$this => $result")
    return result
}

fun String.prettifyDate() = reformatDate("yyyy-MM-dd", "dd MMM yyyy")

suspend fun openDatePickerDialog(context: Context) = suspendCoroutine<Long> {
    var date = System.currentTimeMillis()
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = date
    val listener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = day
        date = calendar.timeInMillis
        it.resume(date)
    }
    DatePickerDialog(
        context,
        listener,
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    ).show()
}