package com.crimson.assignment.model

import com.crimson.assignment.model.Search
import com.google.gson.annotations.SerializedName

data class SearchList (
    @SerializedName("Search") val search : List<Search>,
    @SerializedName("totalResults") val totalResults : Int,
    @SerializedName("Response") val response : Boolean
)