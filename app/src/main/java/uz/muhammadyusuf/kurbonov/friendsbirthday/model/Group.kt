package uz.muhammadyusuf.kurbonov.friendsbirthday.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Group(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var visibilty: Int = 0
)