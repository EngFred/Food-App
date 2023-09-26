package com.engineer.fred.easyfood.data.utils.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.engineer.fred.easyfood.data.api.MealDbApi
import com.engineer.fred.easyfood.data.db.MealDb
import com.engineer.fred.easyfood.data.db.MealDbDAO
import com.engineer.fred.easyfood.data.source.LocalDataSourceImpl
import com.engineer.fred.easyfood.data.source.RemoteDataSourceImpl
import com.engineer.fred.easyfood.data.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn( SingletonComponent::class )
class AppModule {

    @Provides
    fun provideRetrofitInstance() : Retrofit = Retrofit.Builder()
        .baseUrl( BASE_URL )
        .addConverterFactory( GsonConverterFactory.create() )
        .build()

    @Provides
    fun provideApiInstance( retrofit : Retrofit) : MealDbApi = retrofit.create( MealDbApi::class.java )

    @Provides
    fun provideRemoteDataSource( api : MealDbApi) = RemoteDataSourceImpl( api )

    @Provides
    fun provideRoomDatabase( @ApplicationContext context : Context ) : MealDb = Room.databaseBuilder( context, MealDb::class.java, "meal_database" ).build()

    @Provides
    fun provideDaoInstance( mealDb : MealDb ) : MealDbDAO = mealDb.mealDao()

    @Provides
    fun provideLocalDataSource(  dao: MealDbDAO ) = LocalDataSourceImpl( dao )

}