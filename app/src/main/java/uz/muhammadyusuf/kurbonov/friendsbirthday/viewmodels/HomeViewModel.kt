package uz.muhammadyusuf.kurbonov.friendsbirthday.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.transform
import uz.muhammadyusuf.kurbonov.friendsbirthday.database.AppDatabase
import uz.muhammadyusuf.kurbonov.friendsbirthday.database.BirthdaysDao

class HomeViewModel: ViewModel() {
    private lateinit var database: BirthdaysDao

    fun initialize(context: Context){
        database = AppDatabase.getInstance(context).getDatabaseController()
    }

    val allBirthdays by lazy {
        database.queryEntities()
                .distinctUntilChanged()
                .transform { value ->
                    emit(value.sortedBy {
                        it
                    })
                }
    }
}