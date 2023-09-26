package com.engineer.fred.easyfood.presentation.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.engineer.fred.easyfood.R
import com.engineer.fred.easyfood.data.utils.Resource
import com.engineer.fred.easyfood.databinding.ActivityCategoryMealsBinding
import com.engineer.fred.easyfood.presentation.adapters.CategoryMealsAdapter
import com.engineer.fred.easyfood.presentation.viewModels.CategoryMealsActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryMealsActivity : AppCompatActivity(), CategoryMealsAdapter.ItemClick {

    private var  _binding: ActivityCategoryMealsBinding? = null
    private val binding
        get()=checkNotNull( _binding )

    private val categoryMealsActivityViewModel  by viewModels<CategoryMealsActivityViewModel>()
    private val categoryMealsAdapter: CategoryMealsAdapter by lazy { CategoryMealsAdapter() }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme( R.style.Theme_EasyFood )
        _binding = ActivityCategoryMealsBinding.inflate( layoutInflater )
        categoryMealsAdapter.setUpItemClick( this )
        setContentView( binding.root )

        binding.mealRecyclerview.adapter = categoryMealsAdapter
        binding.mealRecyclerview.layoutManager = GridLayoutManager( this, 2)

        val categoryName = intent.getStringExtra("category_name")
        categoryMealsActivityViewModel.getCategoryMeals( categoryName!! )

        lifecycleScope.launch {
            repeatOnLifecycle(  Lifecycle.State.STARTED ) {
                categoryMealsActivityViewModel.categoryMeals.collectLatest {
                    when( it ){
                        Resource.Loading -> binding.loadingGifMeals.visibility = View.VISIBLE
                        is Resource.Success -> {
                            binding.loadingGifMeals.visibility = View.GONE
                            binding.tvCategoryCount.text = "$categoryName : ${it.result.size}"
                            categoryMealsAdapter.differ.submitList( it.result )
                        }
                        is Resource.Failure -> Toast.makeText(this@CategoryMealsActivity , it.errorMessage , Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun showMealDetail( mealId : String ) {
        val intent = Intent( this , MealDetailsActivity::class.java )
        intent.putExtra( "meal_id", mealId )
        startActivity( intent )
    }

}