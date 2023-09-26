package com.engineer.fred.easyfood.presentation.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.engineer.fred.easyfood.R
import com.engineer.fred.easyfood.data.models.Meal
import com.engineer.fred.easyfood.data.utils.Resource
import com.engineer.fred.easyfood.databinding.ActivityMealDetailsBinding
import com.engineer.fred.easyfood.presentation.ui.fragments.FavoritesFragment.Companion.favoriteMeals
import com.engineer.fred.easyfood.presentation.viewModels.DetailActivityViewModel
import com.engineer.fred.easyfood.presentation.viewModels.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MealDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityMealDetailsBinding? = null
    private val binding
        get()= checkNotNull( _binding )

    private var mealSaved = false

    private val detailActivityViewModel by viewModels<DetailActivityViewModel>()
    private val favoritesViewModel by viewModels<FavoritesViewModel>()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme( R.style.Theme_EasyFood )
        _binding = ActivityMealDetailsBinding.inflate( layoutInflater )
        setContentView( binding.root )

        val mealId = intent.getStringExtra("meal_id")
        if (mealId != null) {
            detailActivityViewModel.getMealDetail( mealId )
        }

        lifecycleScope.launch {
            repeatOnLifecycle( Lifecycle.State.STARTED ) {
                detailActivityViewModel.mealDetail.collectLatest {
                    when( it ){
                        Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                        is Resource.Success -> {
                            updateUi( it.result )
                            binding.btnSave.setOnClickListener { _ ->
                                if ( !mealSaved ) {
                                    mealSaved =true
                                    detailActivityViewModel.saveMeal( it.result )
                                    binding.btnSave.setImageResource( R.drawable.ic_favorite )
                                } else {
                                    mealSaved = false
                                    favoritesViewModel.deleteMeal( it.result )
                                    binding.btnSave.setImageResource( R.drawable.ic_fav )
                                    Toast.makeText(this@MealDetailsActivity , "Meal removed from favorites" , Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        is Resource.Failure -> Toast.makeText(this@MealDetailsActivity , it.errorMessage , Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle( Lifecycle.State.STARTED ) {
                detailActivityViewModel.saveMeal.collectLatest {
                    when( it ) {
                        Resource.Loading -> Toast.makeText(this@MealDetailsActivity , "Saving meal..." , Toast.LENGTH_SHORT).show()
                        is Resource.Success -> Toast.makeText(this@MealDetailsActivity , it.result , Toast.LENGTH_LONG).show()
                        is Resource.Failure -> Toast.makeText(this@MealDetailsActivity , it.errorMessage , Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateUi( meal : Meal ) {
        if ( meal in favoriteMeals ) {
            binding.btnSave.setImageResource(R.drawable.ic_favorite)
            mealSaved = true
        } else {
            binding.btnSave.setImageResource(R.drawable.ic_fav)
            mealSaved = false
        }
        binding.progressBar.visibility = View.INVISIBLE
        Glide.with( this ).load( Uri.parse( meal.imageUrl ) ).into( binding.imgMealDetail )
        binding.tvContent.text = meal.makeInstructions
        binding.tvCategoryInfo.text = "Category: ${meal.category}"
        binding.tvAreaInfo.text = " Area:  ${meal.madeFrom}"

        binding.imgYoutube.setOnClickListener {
            val intent = Intent( ACTION_VIEW, Uri.parse( meal.youtubeLink ) )
            startActivity( intent )
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}