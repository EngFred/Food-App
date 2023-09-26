package com.engineer.fred.easyfood.domain.usecases.remote

import com.engineer.fred.easyfood.data.models.Meal
import com.engineer.fred.easyfood.data.repo.RemoteRepositoryImpl
import javax.inject.Inject

class SearchMealUseCase @Inject constructor(
    private val repository : RemoteRepositoryImpl
) {
    suspend operator fun invoke( query: String ) : List<Meal> {
        return repository.searchMeal( query ).meals
    }
}