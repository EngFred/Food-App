package com.engineer.fred.easyfood.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineer.fred.easyfood.data.models.Meal
import com.engineer.fred.easyfood.data.utils.Resource
import com.engineer.fred.easyfood.domain.usecases.local.SaveMealUseCase
import com.engineer.fred.easyfood.domain.usecases.remote.GetMealDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class DetailActivityViewModel @Inject constructor (
    private val getMealDetailUseCase : GetMealDetailUseCase,
    private val saveMealUseCase : SaveMealUseCase
) : ViewModel() {

    private val _mealDetail = MutableStateFlow<Resource<Meal>>(Resource.Loading)
    val mealDetail = _mealDetail.asStateFlow()

    private val _saveMeal = MutableSharedFlow<Resource<String>>()
    val saveMeal = _saveMeal.asSharedFlow()

    private val errorHandler = CoroutineExceptionHandler { _ , throwable ->
        throwable.printStackTrace()
    }

    fun getMealDetail( mealId: String ) {
        viewModelScope.launch( Dispatchers.IO + errorHandler ) {
            try {
                val result = getMealDetailUseCase( mealId )
                _mealDetail.value = Resource.Success(result)
            } catch ( ex: Exception ) {
                when ( ex ) {
                    is ConnectException -> _mealDetail.value = Resource.Failure("No internet connection!")
                    is HttpException -> {
                        _mealDetail.value=Resource.Failure("$ex")
                        Log.i("OMONGOLE" , "HttpExceptionError: $ex")
                    }
                    else -> Log.i("OMONGOLE", "error: $ex")
                }
            }
        }
    }

    fun saveMeal( meal : Meal ) {
        viewModelScope.launch(  Dispatchers.IO + errorHandler ) {
            _saveMeal.emit(Resource.Loading)
            try {
                 saveMealUseCase.invoke( meal )
                _saveMeal.emit( Resource.Success("Meal added to favorites!") )
            }catch ( ex: Exception ) {
                _saveMeal.emit( Resource.Failure("Something went wrong!") )
                Log.i("OMONGOLE" , "SaveMealError: $ex")
            }
        }
    }

}