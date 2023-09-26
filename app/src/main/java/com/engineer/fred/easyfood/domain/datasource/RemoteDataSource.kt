package com.engineer.fred.easyfood.domain.datasource

import com.engineer.fred.easyfood.data.models.CategoryMealsResponse
import com.engineer.fred.easyfood.data.models.MealCategoryResponse
import com.engineer.fred.easyfood.data.models.MealDetailResponse
import com.engineer.fred.easyfood.data.models.MealSearchResponse
import com.engineer.fred.easyfood.data.models.PopularMealResponse
import com.engineer.fred.easyfood.data.models.RandomMealResponse

interface RemoteDataSource {
    suspend fun getRandomMeal() : RandomMealResponse
    suspend fun getPopularMeal(category : String ) : PopularMealResponse
    suspend fun getMealsCategory() : MealCategoryResponse
    suspend fun getMealDetail( mealId: String ) : MealDetailResponse
    suspend fun searchMeal( query: String ): MealSearchResponse
    suspend fun getCategoryMeals( categoryName: String ) : CategoryMealsResponse
}