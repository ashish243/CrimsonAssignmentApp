package com.crimson.assignment.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.crimson.assignment.utils.NetworkState
import com.crimson.assignment.api.ProductService
import com.crimson.assignment.api.getProductsResult
import com.crimson.assignment.db.ProductLocalCache
import com.crimson.assignment.model.Search
import com.crimson.assignment.ui.ProductActivity


class ProductBoundaryCallback(
    private val service: ProductService,
    private val cache: ProductLocalCache
) : PagedList.BoundaryCallback<Search>() {

    // keep the last requested page.
    // When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    private val _networkErrors = MutableLiveData<String>()

    private val value = MutableLiveData<NetworkState>()
    val valueForRefresh get() = value


    // LiveData of network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false


    fun loadToRefresh() {
        requestAndSaveData()
    }

    /*
    Request data from web service[website] and save into database
    if data insert to database [lastRequestedPage] is increment
    the [isRequestInProgress] help to avoid multiple query.
    the error is set to [_networkErrors] field property which is [MutableLiveData<String>]
     */
    private fun requestAndSaveData() {

        if (isRequestInProgress) return

        isRequestInProgress = true
        value.value = NetworkState.LOADING
        getProductsResult(service, ProductActivity.TYPE,
             "5d81e1ce", 1, ProductActivity.SEARCH, { products ->
                lastRequestedPage = 1
                cache.insert(products) {
                    lastRequestedPage++
                    isRequestInProgress = false
                    value.postValue(NetworkState.LOADED)
                }
            }, { error ->
                _networkErrors.postValue(error)
                value.value = NetworkState.error(error)
                isRequestInProgress = false
            })
    }

    private fun requestAndSaveDataLoadMore() {

        if (isRequestInProgress) return

        isRequestInProgress = true
        value.value = NetworkState.LOADING
        getProductsResult(service, ProductActivity.TYPE,
            "5d81e1ce", lastRequestedPage, ProductActivity.SEARCH, { products ->

                cache.insert(products) {
                    lastRequestedPage++
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
        requestAndSaveData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Search) {
        requestAndSaveDataLoadMore()
    }
}