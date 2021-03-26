package uz.muhammadyusuf.kurbonov.friendsbirthday.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.YEAR
import java.util.Calendar.getInstance

@Entity
data class BirthdayEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var birthday: String = "", // date format YYYY-mm-dd
    var phone: String = "",
    var groupId: Int = 0
): Comparable<BirthdayEntity>{
    override fun compareTo(other: BirthdayEntity): Int {
        return when {
            getRemainingTime() > other.getRemainingTime() -> 1
            getRemainingTime() == other.getRemainingTime() -> 0
            else -> -1
        }
    }

    private fun String.formatAsDate(): Calendar {
        return getInstance().apply {
            timeInMillis = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())
                    .parse(this@formatAsDate)?.time ?: throw IllegalArgumentException("No format")
        }
    }

    private fun getRemainingTime(): Long{
        val calendar = birthday.formatAsDate()
        calendar[YEAR] = GregorianCalendar.getInstance()[YEAR]

        var remainingTime = calendar.timeInMillis - System.currentTimeMillis()

        if (remainingTime < 0) {
            calendar[YEAR] = GregorianCalendar.getInstance()[YEAR] + 1
            remainingTime = calendar.timeInMillis - System.currentTimeMillis()
        }
        return remainingTime
    }
}