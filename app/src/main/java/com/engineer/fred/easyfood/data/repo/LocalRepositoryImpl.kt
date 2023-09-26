package com.engineer.fred.easyfood.data.repo

import com.engineer.fred.easyfood.data.models.Meal
import com.engineer.fred.easyfood.data.source.LocalDataSourceImpl
import com.engineer.fred.easyfood.domain.repositories.LocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor (
    private val dataSource : LocalDataSourceImpl
) : LocalRepository {
    override suspend fun saveMeal(meal : Meal) = dataSource.saveMeal( meal )
    override fun getSavedMeals() : Flow<List<Meal>> = dataSource.getSavedMeals()
    override suspend fun deleteMeal(meal : Meal) = dataSource.deleteMeal( meal )
}