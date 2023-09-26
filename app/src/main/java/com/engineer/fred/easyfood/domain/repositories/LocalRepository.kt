package com.engineer.fred.easyfood.domain.repositories

import com.engineer.fred.easyfood.data.models.Meal
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun saveMeal( meal: Meal)
    fun getSavedMeals() : Flow<List<Meal>>
    suspend fun deleteMeal( meal : Meal)
}