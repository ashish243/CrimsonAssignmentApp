package com.crimson.assignment.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.crimson.assignment.api.ProductService
import com.crimson.assignment.api.getMovieDetail
import com.crimson.assignment.db.ProductLocalCache
import com.crimson.assignment.model.DetailRoot
import com.crimson.assignment.ui.detail.DetailActivity
import com.crimson.assignment.utils.NetworkState

class DetailBoundaryCallback (private val service: ProductService, private val cache: ProductLocalCache)
: PagedList.BoundaryCallback<DetailRoot>()
{
    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false
    private val _networkErrors = MutableLiveData<String>()

    private val value = MutableLiveData<NetworkState>()
    val valueForRefresh get() = value

    // LiveData of network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors

     fun requestDetail() {

        if (isRequestInProgress) return

        isRequestInProgress = true
        value.value = NetworkState.LOADING

        getMovieDetail(service, DetailActivity.PLOT,
            DetailActivity.apikey, DetailActivity.SEARCH, { products ->

                cache.insertDetail(products) {
                    isRequestInProgress = false
                   value.postValue(NetworkState.LOADED)
                }
            }, { error ->
                _networkErrors.postValue(error)
                value.value = NetworkState.error(error)
                isRequestInProgress = false
            })
    }
    override fun onZeroItemsLoaded() {
        requestDetail()
    }

}