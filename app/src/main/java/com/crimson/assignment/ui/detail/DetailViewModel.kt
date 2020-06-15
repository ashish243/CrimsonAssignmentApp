package com.crimson.assignment.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.crimson.assignment.data.DetailRepository
import com.crimson.assignment.model.DetailResult
import com.crimson.assignment.model.DetailRoot

class DetailViewModel (private val repository: DetailRepository) : ViewModel() {

    private val productResult = MutableLiveData<DetailResult>()
    private val _networkStates = repository.networkState
    val networkStates get() = _networkStates

    init {

        getData()
    }

    val networkErrors: LiveData<String> = Transformations.switchMap(productResult) {
        it.networkErrors
    }

    val movieDetail: LiveData<PagedList<DetailRoot>> = Transformations.switchMap(productResult) {
        it.data

    }

    private fun getData() {
        productResult.value = repository.getDetail()
    }
}