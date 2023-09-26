package com.engineer.fred.easyfood.domain.usecases.remote

import com.engineer.fred.easyfood.data.models.PopularMeal
import com.engineer.fred.easyfood.data.repo.RemoteRepositoryImpl
import javax.inject.Inject

class GetPopularMealsUseCase @Inject constructor(
    private val remoteRepository : RemoteRepositoryImpl
) {
    suspend operator fun invoke( category : String ) : List<PopularMeal> {
        return remoteRepository.getPopularMeal( category ).meals
    }
}