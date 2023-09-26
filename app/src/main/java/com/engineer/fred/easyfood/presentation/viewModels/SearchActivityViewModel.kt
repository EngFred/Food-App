package com.engineer.fred.easyfood.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineer.fred.easyfood.data.models.Meal
import com.engineer.fred.easyfood.data.utils.Resource
import com.engineer.fred.easyfood.domain.usecases.remote.SearchMealUseCase
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
class SearchActivityViewModel @Inject constructor(
    private val searchMealUseCase : SearchMealUseCase
) : ViewModel() {

    private val _search = MutableStateFlow<Resource<List<Meal>>>( Resource.Loading )
    val search = _search.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    fun searchMeal( mealName: String ) {
        viewModelScope.launch( Dispatchers.IO + errorHandler ) {
            try {
                val result = searchMealUseCase( mealName )
                _search.value = Resource.Success( result )
            }catch ( ex: Exception ) {
                when ( ex ) {
                    is ConnectException -> _search.value = Resource.Failure("No internet connection!")
                    is HttpException -> {
                        _search.value=Resource.Failure("$ex")
                        Log.i("OMONGOLE" , "HttpExceptionError: $ex")
                    }
                    else -> Log.i("OMONGOLE", "error: $ex")
                }
            }
        }
    }

}