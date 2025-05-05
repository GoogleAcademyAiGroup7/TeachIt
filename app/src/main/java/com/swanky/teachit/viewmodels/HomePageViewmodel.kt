package com.swanky.teachit.viewmodels

import android.app.Application
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomePageViewmodel @Inject constructor(application: Application) : BaseViewModel(application) {


}