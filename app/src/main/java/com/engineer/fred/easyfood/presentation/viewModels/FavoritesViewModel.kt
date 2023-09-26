package com.engineer.fred.easyfood.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineer.fred.easyfood.data.models.Meal
import com.engineer.fred.easyfood.data.utils.Resource
import com.engineer.fred.easyfood.domain.usecases.local.DeleteMealUseCase
import com.engineer.fred.easyfood.domain.usecases.local.GetAllSavedMealsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getAllSavedMealsUseCase : GetAllSavedMealsUseCase,
    private val deleteMealUseCase : DeleteMealUseCase
) : ViewModel() {

    private val _meals = MutableStateFlow<Resource<List<Meal>>>(Resource.Loading)
    val meals =_meals.asStateFlow()

    private val _delete = MutableSharedFlow<Resource<String>>()
    val delete = _delete.asSharedFlow()

    init {
        getAllFavoritesMeals()
    }

    fun getAllFavoritesMeals() {
        viewModelScope.launch( Dispatchers.IO ) {
            try {
                getAllSavedMealsUseCase.invoke().collect{
                    try {
                        _meals.value = Resource.Success( it )
                    } catch ( ex: Exception ) {
                        Log.i("OMONGOLE FLOW", "Flow exception: $ex")
                        _meals.value =Resource.Failure("$ex")
                    }
                }
            } catch ( ex: Exception ) {
                Log.i("OMONGOLE ", "Other exception: $ex")
                _meals.value =Resource.Failure("$ex")
            }
        }
    }

    fun deleteMeal(  meal : Meal ) {
        viewModelScope.launch( Dispatchers.IO ) {
            _delete.emit( Resource.Loading )
            try {
               deleteMealUseCase( meal )
                _delete.emit( Resource.Success("Meal Deleted!"))
            } catch ( ex: Exception ) {
                Log.i("OMONGOLE ", "Other exception: $ex")
                _delete.emit( Resource.Failure("$ex") )
            }
        }
    }

}