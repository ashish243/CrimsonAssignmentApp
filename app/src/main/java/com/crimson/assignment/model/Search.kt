package com.crimson.assignment.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

//@field:SerializedName("id") var id: String,
@Entity(tableName = "search")
data class Search (
	@PrimaryKey @field:SerializedName("Title") val title : String,
	@field:SerializedName("Year") val year : Int,
	@field:SerializedName("imdbID") val imdbID : String,
	@field:SerializedName("Type") val type : String,
	@field:SerializedName("Poster") val poster : String
)