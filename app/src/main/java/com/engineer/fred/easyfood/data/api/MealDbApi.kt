package com.engineer.fred.easyfood.data.api

import com.engineer.fred.easyfood.data.models.CategoryMealsResponse
import com.engineer.fred.easyfood.data.models.MealCategoryResponse
import com.engineer.fred.easyfood.data.models.MealDetailResponse
import com.engineer.fred.easyfood.data.models.MealSearchResponse
import com.engineer.fred.easyfood.data.models.PopularMealResponse
import com.engineer.fred.easyfood.data.models.RandomMealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealDbApi {
    @GET("api/json/v1/1/random.php")
    suspend fun getRandomMeal() : RandomMealResponse

    @GET("api/json/v1/1/filter.php")
    suspend fun getPopularMeal(
        @Query("c") category : String
    ) : PopularMealResponse

    @GET("api/json/v1/1/categories.php")
    suspend fun getMealsCategory() : MealCategoryResponse

    @GET("api/json/v1/1/lookup.php")
    suspend fun getMealDetail(
        @Query("i") mealId: String
    ) : MealDetailResponse

    @GET("api/json/v1/1/search.php")
    suspend fun searchMeal(
        @Query("s") query: String
    ) : MealSearchResponse

    @GET("api/json/v1/1/filter.php")
    suspend fun getCategoryMeals(
        @Query("c") categoryName: String
    ) : CategoryMealsResponse

}