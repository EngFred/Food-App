package com.engineer.fred.easyfood.data.source

import com.engineer.fred.easyfood.data.db.MealDbDAO
import com.engineer.fred.easyfood.data.models.Meal
import com.engineer.fred.easyfood.domain.datasource.LocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val dao : MealDbDAO
) : LocalDataSource {
    override suspend fun saveMeal(meal : Meal) = dao.saveMeal( meal )
    override fun getSavedMeals() : Flow<List<Meal>> = dao.getSavedMeals()
    override suspend fun deleteMeal(meal : Meal)  = dao.deleteMeal( meal )
}