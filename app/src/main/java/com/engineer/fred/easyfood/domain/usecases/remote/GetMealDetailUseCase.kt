package com.engineer.fred.easyfood.domain.usecases.remote

import com.engineer.fred.easyfood.data.models.Meal
import com.engineer.fred.easyfood.data.repo.RemoteRepositoryImpl
import javax.inject.Inject

class GetMealDetailUseCase @Inject constructor(
    private val remoteRepository : RemoteRepositoryImpl
) {
    suspend operator fun invoke( mealId: String ) : Meal {
        return remoteRepository.getMealDetail( mealId ).meals[0]
    }
}