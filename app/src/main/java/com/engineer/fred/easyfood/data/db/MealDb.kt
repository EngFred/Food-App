package com.engineer.fred.easyfood.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.engineer.fred.easyfood.data.models.Meal

@Database( entities = [ Meal::class ], version = 2, exportSchema = false )
abstract class MealDb :  RoomDatabase() {
    abstract fun mealDao() : MealDbDAO
}