package com.engineer.fred.easyfood.domain.usecases.remote

import com.engineer.fred.easyfood.data.models.MealCategory
import com.engineer.fred.easyfood.data.repo.RemoteRepositoryImpl
import javax.inject.Inject

class GetMealCategoriesUseCase @Inject constructor(
    private val remoteRepository : RemoteRepositoryImpl
) {
    suspend operator fun invoke() : List<MealCategory> {
        return  remoteRepository.getMealsCategory().categories
    }
}