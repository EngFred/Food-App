package com.engineer.fred.easyfood.data.models

import com.google.gson.annotations.SerializedName

data class PopularMealResponse(
    val meals: List<PopularMeal>
)

data class PopularMeal (
    @SerializedName("strMeal")
    val name: String,
    @SerializedName("strMealThumb")
    val imageUrl: String,
    @SerializedName("idMeal")
    val id: String
)