package com.engineer.fred.easyfood.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineer.fred.easyfood.data.models.CategoryMeal
import com.engineer.fred.easyfood.data.utils.Resource
import com.engineer.fred.easyfood.domain.usecases.remote.GetCategoryMealsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class CategoryMealsActivityViewModel @Inject constructor(
    private val getCategoryMealsUseCase : GetCategoryMealsUseCase
) : ViewModel(){

    private val _categoryMeals = MutableStateFlow<Resource<List<CategoryMeal>>>( Resource.Loading )
    val categoryMeals = _categoryMeals.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    fun getCategoryMeals(  categoryName: String ) {
        viewModelScope.launch( Dispatchers.IO + errorHandler  ) {
            try {
                val result = getCategoryMealsUseCase( categoryName )
                _categoryMeals.value = Resource.Success( result )
            }catch ( ex: Exception ) {
                when ( ex ) {
                    is ConnectException -> _categoryMeals.value = Resource.Failure("No internet connection!")
                    is HttpException -> {
                        _categoryMeals.value=Resource.Failure("$ex")
                        Log.i("OMONGOLE" , "HttpExceptionError: $ex")
                    }
                    else -> Log.i("OMONGOLE", "error: $ex")
                }
            }
        }
    }

}