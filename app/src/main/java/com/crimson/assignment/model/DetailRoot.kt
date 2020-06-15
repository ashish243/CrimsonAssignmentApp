package com.crimson.assignment.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(tableName = "detail")
data class DetailRoot (

	@PrimaryKey @field: SerializedName("Title") val title : String,
	@field: SerializedName("Year") val year : Int,
	@field:SerializedName("Rated") val rated : String,
	@field:SerializedName("Released") val released : String,
	@field:SerializedName("Runtime") val runtime : String,
	@field:SerializedName("Genre") val genre : String,
	@field:SerializedName("Director") val director : String,
	@field:SerializedName("Writer") val writer : String,
	@field:SerializedName("Actors") val actors : String,
	@field:SerializedName("Plot") val plot : String,
	@field:SerializedName("Language") val language : String,
	@field:SerializedName("Country") val country : String,
	@field:SerializedName("Awards") val awards : String,
	@field:SerializedName("Poster") val poster : String,
	//@SerializedName("Ratings") val ratings : List<Ratings> = listOf(),
	@field:SerializedName("Metascore") val metascore : String,
	@field:SerializedName("imdbRating") val imdbRating : Double,
	@field:SerializedName("imdbVotes") val imdbVotes : String,
	@field:SerializedName("imdbID") val imdbID : String,
	@field:SerializedName("Type") val type : String,
	@field:SerializedName("DVD") val dVD : String,
	@field:SerializedName("BoxOffice") val boxOffice : String,
	@field:SerializedName("Production") val production : String,
	@field:SerializedName("Website") val website : String,
	@field:SerializedName("Response") val response : Boolean
)