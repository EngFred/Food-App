package com.engineer.fred.easyfood.domain.usecases.remote

import com.engineer.fred.easyfood.data.models.CategoryMeal
import com.engineer.fred.easyfood.data.repo.RemoteRepositoryImpl
import javax.inject.Inject

class GetCategoryMealsUseCase @Inject constructor(
    private val repository : RemoteRepositoryImpl
) {
    suspend operator fun invoke( categoryName: String ) : List<CategoryMeal> {
        return repository.getCategoryMeals( categoryName ).meals
    }
}