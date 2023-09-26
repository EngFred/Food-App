package com.engineer.fred.easyfood.data.source

import com.engineer.fred.easyfood.data.api.MealDbApi
import com.engineer.fred.easyfood.data.models.CategoryMealsResponse
import com.engineer.fred.easyfood.data.models.MealCategoryResponse
import com.engineer.fred.easyfood.data.models.MealDetailResponse
import com.engineer.fred.easyfood.data.models.MealSearchResponse
import com.engineer.fred.easyfood.data.models.PopularMeal
import com.engineer.fred.easyfood.data.models.PopularMealResponse
import com.engineer.fred.easyfood.data.models.RandomMealResponse
import com.engineer.fred.easyfood.domain.datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val api : MealDbApi
) : RemoteDataSource {
    override suspend fun getRandomMeal() : RandomMealResponse {
        return api.getRandomMeal()
    }

    override suspend fun getPopularMeal(category : String ) : PopularMealResponse {
        return api.getPopularMeal( category )
    }

    override suspend fun getMealsCategory() : MealCategoryResponse {
        return api.getMealsCategory()
    }

    override suspend fun getMealDetail( mealId : String ) : MealDetailResponse {
        return api.getMealDetail( mealId )
    }

    override suspend fun searchMeal(query : String) : MealSearchResponse {
        return api.searchMeal( query )
    }

    override suspend fun getCategoryMeals(categoryName : String) : CategoryMealsResponse {
        return api.getCategoryMeals( categoryName )
    }

}