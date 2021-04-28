package uz.muhammadyusuf.kurbonov.friendsbirthday.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import uz.muhammadyusuf.kurbonov.friendsbirthday.database.AppDatabase
import uz.muhammadyusuf.kurbonov.friendsbirthday.database.BirthdaysDao
import uz.muhammadyusuf.kurbonov.friendsbirthday.model.BirthdayEntity

class HomeViewModel: ViewModel() {
    private lateinit var database: BirthdaysDao

    fun initialize(context: Context) {
        database = AppDatabase.getInstance(context).getDatabaseController()
    }

    fun delete(entity: BirthdayEntity) {
        viewModelScope.launch {
            database.deleteEntity(entity)
        }
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