package com.engineer.fred.easyfood.domain.usecases.local

import com.engineer.fred.easyfood.data.models.Meal
import com.engineer.fred.easyfood.data.repo.LocalRepositoryImpl
import javax.inject.Inject

class SaveMealUseCase @Inject constructor(
    private val localRepository : LocalRepositoryImpl
) {
    suspend operator fun invoke( meal : Meal ) = localRepository.saveMeal( meal )
}