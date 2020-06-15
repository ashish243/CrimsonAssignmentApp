package com.crimson.assignment.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

/**
 * ProductResult from query web service, which contains LiveData<List<Product>> holding query data,
 * and a LiveData<String> of network error state.
 */
data class ProductResult(
    val data: LiveData<PagedList<Search>>,
    val networkErrors: LiveData<String>
) {

}
