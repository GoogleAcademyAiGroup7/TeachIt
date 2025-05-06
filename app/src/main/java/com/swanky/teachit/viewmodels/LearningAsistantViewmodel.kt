package com.swanky.teachit.viewmodels

import android.app.Application
import com.swanky.teachit.repositories.Repository
import javax.inject.Inject

class LearningAsistantViewmodel @Inject constructor(private val repository: Repository, application: Application) : BaseViewModel(application)  {



}