package com.crimson.assignment.model

import com.google.gson.annotations.SerializedName

data class Ratings (
	@SerializedName("Source") val source : String,
	@SerializedName("Value") val value : String
)