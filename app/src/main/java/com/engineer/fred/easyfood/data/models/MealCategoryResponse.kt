package com.engineer.fred.easyfood.data.models

import com.google.gson.annotations.SerializedName

data class MealCategoryResponse(
    val categories: List<MealCategory>
)

data class MealCategory(
    @SerializedName("idCategory")
    val id: String,
    @SerializedName("strCategory")
    val name: String,
    @SerializedName("strCategoryThumb")
    val imageUrl: String,
    @SerializedName("strCategoryDescription")
    val description: String
)