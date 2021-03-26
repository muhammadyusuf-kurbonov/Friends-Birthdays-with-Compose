package uz.muhammadyusuf.kurbonov.friendsbirthday.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uz.muhammadyusuf.kurbonov.friendsbirthday.model.BirthdayEntity

@Dao
interface BirthdaysDao {
    @Insert
    suspend fun insertEntity(birthdayEntity: BirthdayEntity)

    @Update
    suspend fun updateEntity(birthdayEntity: BirthdayEntity)

    @Delete
    suspend fun deleteEntity(birthdayEntity: BirthdayEntity)

    @Query("SELECT * FROM BirthdayEntity")
    fun queryEntities(): Flow<List<BirthdayEntity>>
}