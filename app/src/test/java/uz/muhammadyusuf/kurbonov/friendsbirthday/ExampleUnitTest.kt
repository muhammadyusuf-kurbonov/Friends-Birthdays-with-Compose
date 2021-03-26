package uz.muhammadyusuf.kurbonov.friendsbirthday

import org.junit.Test

import org.junit.Assert.*
import uz.muhammadyusuf.kurbonov.friendsbirthday.model.BirthdayEntity

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testBirthdayComparison(){
        val first = BirthdayEntity(
                name = " Muhammadyusuf",
                birthday = "2002-05-22",
                phone = "+998913975538"
        )
        val second = BirthdayEntity(
                name = "Doni",
                birthday = "2005-01-04",
                phone = "+998913975538"
        )

        assertEquals(true, second > first)

    }
}