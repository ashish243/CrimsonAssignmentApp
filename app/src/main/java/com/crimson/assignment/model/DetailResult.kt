package com.crimson.assignment.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

class DetailResult (
    val data: LiveData<PagedList<DetailRoot>>,
    val networkErrors: LiveData<String>
)
