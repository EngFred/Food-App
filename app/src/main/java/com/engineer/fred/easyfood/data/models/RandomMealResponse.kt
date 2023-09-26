package com.engineer.fred.easyfood.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class RandomMealResponse(
    val meals: List<Meal>
)

@Entity
data class Meal(
    @PrimaryKey
    @SerializedName("idMeal")
    val id: String,
    @SerializedName("strMealThumb")
    val imageUrl: String,
    @SerializedName("strMeal")
    val name: String,
    @SerializedName("strCategory")
    val category : String,
    @SerializedName("strArea")
    val madeFrom: String,
    @SerializedName("strInstructions")
    val makeInstructions: String,
    @SerializedName("strYoutube")
    val youtubeLink: String
)
