package com.engineer.fred.easyfood.domain.datasource

import com.engineer.fred.easyfood.data.models.Meal
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun saveMeal( meal: Meal )
    fun getSavedMeals() : Flow<List<Meal>>
    suspend fun deleteMeal( meal : Meal )
}