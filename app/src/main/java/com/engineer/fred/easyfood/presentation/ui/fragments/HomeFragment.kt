package com.engineer.fred.easyfood.presentation.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.bumptech.glide.Glide
import com.engineer.fred.easyfood.databinding.FragmentHomeBinding
import com.engineer.fred.easyfood.presentation.ui.activities.MealDetailsActivity
import com.engineer.fred.easyfood.presentation.viewModels.HomeFragmentViewModel
import com.engineer.fred.easyfood.data.utils.Resource
import com.engineer.fred.easyfood.presentation.adapters.MealCategoriesAdapter
import com.engineer.fred.easyfood.presentation.adapters.PopularMealsAdapter
import com.engineer.fred.easyfood.presentation.ui.activities.CategoryMealsActivity
import com.engineer.fred.easyfood.presentation.ui.activities.SearchMealActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), PopularMealsAdapter.ItemClick, MealCategoriesAdapter.ItemClick {

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = checkNotNull( _binding )

    private val homeVM by viewModels<HomeFragmentViewModel>()
    private val popularMealsAdapter: PopularMealsAdapter by lazy { PopularMealsAdapter() }
    private val mealCategoriesAdapter: MealCategoriesAdapter by lazy { MealCategoriesAdapter() }

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View {
        _binding = FragmentHomeBinding.inflate( inflater, container, false )
        popularMealsAdapter.setUpItemClick( this )
        mealCategoriesAdapter.setUpItemClick( this )
        return binding.root
    }

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        homeVM.getRandomMeal()
        binding.recViewMealsPopular.adapter = popularMealsAdapter
        binding.recViewMealsPopular.layoutManager = LinearLayoutManager( requireActivity(), HORIZONTAL, false )

        binding.categoriesRV.adapter = mealCategoriesAdapter
        binding.categoriesRV.layoutManager = GridLayoutManager( requireActivity(), 3 )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle( Lifecycle.State.STARTED ) {
                homeVM.randomMeal.collectLatest { response ->
                    when ( response ) {
                        Resource.Loading -> {
                            binding.loadingGifMeals.visibility=View.VISIBLE
                            binding.contentMain.visibility = View.INVISIBLE
                        }
                        is Resource.Success ->{
                            binding.loadingGifMeals.visibility = View.INVISIBLE
                            binding.contentMain.visibility = View.VISIBLE
                            try {
                                Glide.with( this@HomeFragment ).load( Uri.parse( response.result.imageUrl )).into( binding.randomMeal )
                            } catch ( ex: Exception ) {
                                Toast.makeText(requireActivity() , "$ex" , Toast.LENGTH_SHORT).show() }
                            binding.randomMeal.setOnClickListener { showRandomMealDetail( response.result.id ) }
                        }
                        is Resource.Failure -> Toast.makeText(requireContext() , response.errorMessage , Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(  Lifecycle.State.STARTED ) {
                homeVM.popularMeals.collectLatest {
                    when( it ){
                        Resource.Loading -> binding.loadingGifMeals.visibility = View.VISIBLE
                        is Resource.Success -> {
                            binding.loadingGifMeals.visibility = View.INVISIBLE
                            binding.contentMain.visibility = View.VISIBLE
                            popularMealsAdapter.differ.submitList(it.result)
                        }
                        is Resource.Failure -> Toast.makeText(requireContext() , it.errorMessage , Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(  Lifecycle.State.STARTED ) {
                homeVM.mealCategories.collectLatest {
                    when( it ){
                        Resource.Loading -> binding.loadingGifMeals.visibility = View.VISIBLE
                        is Resource.Success -> {
                            binding.loadingGifMeals.visibility = View.INVISIBLE
                            binding.contentMain.visibility = View.VISIBLE
                            mealCategoriesAdapter.differ.submitList(it.result)
                        }
                        is Resource.Failure -> Toast.makeText(requireContext() , it.errorMessage , Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.imgSearch.setOnClickListener {
            val intent = Intent( requireActivity(), SearchMealActivity::class.java )
            startActivity( intent )
        }
    }

    private fun showRandomMealDetail( mealId: String ) {
        val intent = Intent( requireActivity(), MealDetailsActivity::class.java )
        intent.putExtra( "meal_id", mealId )
        startActivity( intent )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showMealDetail( mealId : String ) {
        val intent = Intent( requireActivity(), MealDetailsActivity::class.java )
        intent.putExtra( "meal_id", mealId )
        startActivity( intent )
    }

    override fun showCategoryMeals( categoryName : String) {
        val intent = Intent( requireActivity(), CategoryMealsActivity::class.java )
        intent.putExtra( "category_name", categoryName )
        startActivity( intent )
    }

}