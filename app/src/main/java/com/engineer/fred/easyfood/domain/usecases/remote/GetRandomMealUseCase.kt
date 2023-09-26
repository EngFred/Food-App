package com.engineer.fred.easyfood.domain.usecases.remote

import com.engineer.fred.easyfood.data.models.Meal
import com.engineer.fred.easyfood.data.repo.RemoteRepositoryImpl
import javax.inject.Inject

class GetRandomMealUseCase @Inject constructor(
    private val remoteRepository : RemoteRepositoryImpl
) {
    suspend operator fun invoke(): Meal {
        return remoteRepository.getRandomMeal().meals[0]
    }
}