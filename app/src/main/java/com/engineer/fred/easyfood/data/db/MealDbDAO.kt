package com.engineer.fred.easyfood.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.engineer.fred.easyfood.data.models.Meal
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDbDAO {

    @Insert(  onConflict = REPLACE )
    suspend fun saveMeal(  meal : Meal  )

    @Query(" SELECT * FROM meal ")
    fun getSavedMeals() : Flow<List<Meal>>

    @Delete
    suspend fun deleteMeal( meal : Meal )
}