package uz.muhammadyusuf.kurbonov.friendsbirthday.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.muhammadyusuf.kurbonov.friendsbirthday.model.BirthdayEntity
import uz.muhammadyusuf.kurbonov.friendsbirthday.model.Group

@Database(entities = [BirthdayEntity::class, Group::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    companion object{
        private var _instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (_instance == null)
                _instance = Room.databaseBuilder(context, AppDatabase::class.java, "main.db")
                    .build()

            return _instance!!

        }
    }

    abstract fun getDatabaseController(): BirthdaysDao
}