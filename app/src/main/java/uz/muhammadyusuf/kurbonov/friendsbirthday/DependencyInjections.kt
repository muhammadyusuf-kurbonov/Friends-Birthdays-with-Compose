package uz.muhammadyusuf.kurbonov.friendsbirthday

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.muhammadyusuf.kurbonov.friendsbirthday.viewmodels.ActivityViewModel

val coreModule = module {
    scope<MainActivity> {
        viewModel {
            ActivityViewModel()
        }
    }
}

