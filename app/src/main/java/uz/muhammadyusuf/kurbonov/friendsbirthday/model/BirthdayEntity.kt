package uz.muhammadyusuf.kurbonov.friendsbirthday.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.YEAR

@Entity
data class BirthdayEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var birthday: String = "", // date format YYYY-MM-dd
    var phone: String = "",
    var groupId: Int = 0
) : Comparable<BirthdayEntity> {

    override fun compareTo(other: BirthdayEntity): Int {
        return when {
            getRemainingTime() > other.getRemainingTime() -> 1
            getRemainingTime() == other.getRemainingTime() -> 0
            else -> -1
        }
    }

    private fun String.parseDateToCalendar(): Calendar {
        return Calendar.getInstance().apply {
            timeInMillis = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse(this@parseDateToCalendar)?.time
                ?: throw IllegalArgumentException("No format")
        }
    }

    private fun getRemainingTime(): Long {
        val calendar = birthday.parseDateToCalendar()
        calendar[YEAR] = GregorianCalendar.getInstance()[YEAR]

        var remainingTime = calendar.timeInMillis - System.currentTimeMillis()

        if (remainingTime < 0) {
            calendar[YEAR] = GregorianCalendar.getInstance()[YEAR] + 1
            remainingTime = calendar.timeInMillis - System.currentTimeMillis()
        }
        if (isBirthdayToday()) {
            return 1
        }
        return remainingTime
    }

    fun isBirthdayToday(): Boolean {
        val birthdayCalendar = birthday.parseDateToCalendar()
        val today = Calendar.getInstance()
        return birthdayCalendar[Calendar.DAY_OF_YEAR] == today[Calendar.DAY_OF_YEAR]
    }
}