package com.swanky.teachit.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.swanky.teachit.models.Topic
import com.swanky.teachit.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicsViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : BaseViewModel(application) {

    private val _topics = MutableLiveData<List<Topic>>()
    private val _errorGetLiveData = MutableLiveData<Boolean>()
    private val _errorAddLiveData = MutableLiveData<Boolean>()

    // Read only
    val topics: LiveData<List<Topic>> = _topics
    val errorGetLiveData: LiveData<Boolean> = _errorGetLiveData
    val errorAddLiveData: LiveData<Boolean> = _errorAddLiveData

    fun getTopics() {
        viewModelScope.launch {
            val result = repository.getAllTopics()

            result.fold(
                onSuccess = {
                    _topics.value = it
                    _errorGetLiveData.value = false
                },
                onFailure = {
                    _errorGetLiveData.value = true
                }
            )
        }
    }

    fun addTopic(topic: Topic) {
        viewModelScope.launch {
            val result = repository.insertTopic(topic)
            result.fold(
                onSuccess = {
                    _errorAddLiveData.value = false
                },
                onFailure = { e ->
                    e.printStackTrace()
                    _errorAddLiveData.value = true
                }
            )
        }
    }

}