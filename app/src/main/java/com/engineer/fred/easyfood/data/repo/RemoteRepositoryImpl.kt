package com.engineer.fred.easyfood.data.repo

import com.engineer.fred.easyfood.data.models.CategoryMealsResponse
import com.engineer.fred.easyfood.data.models.MealCategoryResponse
import com.engineer.fred.easyfood.data.models.MealDetailResponse
import com.engineer.fred.easyfood.data.models.MealSearchResponse
import com.engineer.fred.easyfood.data.models.PopularMealResponse
import com.engineer.fred.easyfood.data.source.RemoteDataSourceImpl
import com.engineer.fred.easyfood.data.models.RandomMealResponse
import com.engineer.fred.easyfood.domain.repositories.RemoteRepository
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val remoteDataSource : RemoteDataSourceImpl
) : RemoteRepository {
    override suspend fun getRandomMeal() : RandomMealResponse {
        return remoteDataSource.getRandomMeal()
    }

    override suspend fun getPopularMeal( category : String ) : PopularMealResponse {
        return remoteDataSource.getPopularMeal( category )
    }

    override suspend fun getMealsCategory() : MealCategoryResponse {
        return remoteDataSource.getMealsCategory()
    }

    override suspend fun getMealDetail( mealId : String ) : MealDetailResponse {
        return remoteDataSource.getMealDetail( mealId )
    }

    override suspend fun searchMeal(query : String) : MealSearchResponse {
        return remoteDataSource.searchMeal( query )
    }

    override suspend fun getCategoryMeals(categoryName : String) : CategoryMealsResponse {
       return remoteDataSource.getCategoryMeals( categoryName )
    }

}