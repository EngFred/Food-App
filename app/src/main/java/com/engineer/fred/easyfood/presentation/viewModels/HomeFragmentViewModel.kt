package com.engineer.fred.easyfood.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineer.fred.easyfood.data.models.MealCategory
import com.engineer.fred.easyfood.data.models.PopularMeal
import com.engineer.fred.easyfood.data.models.Meal
import com.engineer.fred.easyfood.data.utils.Resource
import com.engineer.fred.easyfood.domain.usecases.remote.GetMealCategoriesUseCase
import com.engineer.fred.easyfood.domain.usecases.remote.GetMealDetailUseCase
import com.engineer.fred.easyfood.domain.usecases.remote.GetPopularMealsUseCase
import com.engineer.fred.easyfood.domain.usecases.remote.GetRandomMealUseCase
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
class HomeFragmentViewModel @Inject constructor(
    private val getRandomMealUseCase : GetRandomMealUseCase,
    private val getPopularMealsUseCase : GetPopularMealsUseCase,
    private val getMealCategoriesUseCase : GetMealCategoriesUseCase,
) : ViewModel() {
    private val meal = MutableStateFlow<Resource<Meal>>( Resource.Loading )
    val randomMeal = meal.asStateFlow()

    private val _popularMeals = MutableStateFlow<Resource<List<PopularMeal>>>( Resource.Loading )
    val popularMeals = _popularMeals.asStateFlow()

    private val _mealCategories = MutableStateFlow<Resource<List<MealCategory>>>( Resource.Loading )
    val mealCategories = _mealCategories.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _ , throwable ->
        throwable.printStackTrace()
    }

    init {
        getRandomMeal()
        getPopularMeals()
        getMealCategories()
    }

    private fun getMealCategories() {
       viewModelScope.launch( Dispatchers.IO + errorHandler ) {
           try {
               val result = getMealCategoriesUseCase.invoke()
               _mealCategories.value = Resource.Success( result )
           } catch ( ex: Exception ) {
               when ( ex ) {
                   is ConnectException -> _mealCategories.value = Resource.Failure("No internet connection!")
                   is HttpException -> {
                       _mealCategories.value=Resource.Failure("$ex")
                       Log.i("OMONGOLE" , "HttpExceptionError: $ex")
                   }
                   else -> Log.i("OMONGOLE", "error: $ex")
               }
           }
       }
    }

    fun getRandomMeal() {
            viewModelScope.launch(  Dispatchers.IO + errorHandler ) {
                try {
                    val result = getRandomMealUseCase.invoke()
                    meal.value =  Resource.Success(result)

                } catch ( ex: Exception ) {
                    when ( ex ) {
                        is ConnectException -> meal.value = Resource.Failure("No internet connection!")
                        is HttpException -> {
                            meal.value=Resource.Failure("$ex")
                            Log.i("OMONGOLE" , "HttpExceptionError: $ex")
                        }
                            else -> Log.i("OMONGOLE", "error: $ex")
                    }
                }
            }
        }

    private fun getPopularMeals( ) {
        viewModelScope.launch( Dispatchers.IO + errorHandler ) {
            try {
                val result = getPopularMealsUseCase("seafood")
                _popularMeals.value = Resource.Success(result)
            } catch ( ex: Exception ) {
                when ( ex ) {
                    is ConnectException -> _popularMeals.value = Resource.Failure("No internet connection!")
                    is HttpException -> {
                        _popularMeals.value=Resource.Failure("$ex")
                        Log.i("OMONGOLE" , "HttpExceptionError: $ex")
                    }
                    else -> Log.i("OMONGOLE", "error: $ex")
                }
            }
        }
    }

}