package com.engineer.fred.easyfood.data.models

import com.google.gson.annotations.SerializedName

data class CategoryMealsResponse(
    val meals: List<CategoryMeal>
)

data class CategoryMeal(
    @SerializedName("strMeal")
    val name: String,
    @SerializedName("strMealThumb")
    val imageUrl: String,
    @SerializedName("idMeal")
    val id: String
)