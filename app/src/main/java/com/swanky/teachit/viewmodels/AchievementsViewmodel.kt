package com.swanky.teachit.viewmodels

import android.app.Application
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AchievementsViewmodel @Inject constructor(application: Application) : BaseViewModel(application) {
}