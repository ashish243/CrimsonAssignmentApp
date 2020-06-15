package com.crimson.assignment.ui

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.crimson.assignment.model.ProductResult
import com.crimson.assignment.data.ProductRepository
import com.crimson.assignment.model.Search


/**
 * ViewModel for the [ProductActivity] screen.
 * The [ProductViewModel] class communicate with the [ProductRepository] to get the data.
 */
class ProductViewModel (private val repository: ProductRepository) : ViewModel() {


    private val filterTextAll: MutableLiveData<String> = MutableLiveData()
    private val chacheResultList: MutableLiveData<String> = MutableLiveData()

    val dataList = Transformations.switchMap(filterTextAll) {
        getAllProductForFilter(it)
    }

    val chacheResult = Transformations.switchMap(chacheResultList) {
        getChacheData(it)
    }


    //this function help to set the query enter to [filterTextAll]
    //which is [MutableLiveData<String>] to observe if there is changes
    fun setFilterName(name: String) {
        filterTextAll.value = name
    }

    fun setValueForChache(name: String) {
        chacheResultList.value = name
    }

    /*
    this field handle the [ProductResult]
     */
    private val productResult = MutableLiveData<ProductResult>()

    private val _networkStates = repository.networkState


    /*
    this field will be used in [ProductActivity] for refresh purpose
    When user swipe to refresh, the networkState will help us to perform some task with the view to be displayed.
     */
    val networkStates get() = _networkStates

    /*
    retrieve [ProductResult] and the result is pass to data field
    note that the result pass to data field declare in ProductResult = [LiveData<PagedList<Products>>]
    */
    val productList: LiveData<PagedList<Search>> = Transformations.switchMap(productResult) {
        it.data

    }

    /*
    This handle the network error,in case if occur while PagedList.BoundaryCallBack trying to quering the webservice
    the value retrieve which is [LiveData<String>] is passed to [networkErrors]
     */
    val networkErrors: LiveData<String> = Transformations.switchMap(productResult) {
        it.networkErrors
    }


    fun getSearchData() {
         productResult.value = repository.getProduct()

    }

    fun getChacheData(value: String): LiveData<PagedList<Search>>{
       return repository.getChacheMovie()
    }

    private fun getAllProductForFilter(name: String): LiveData<PagedList<Search>> {
        return repository.getAllProductForFilter(name)
    }

}
