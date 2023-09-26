package com.engineer.fred.easyfood.presentation.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.engineer.fred.easyfood.data.models.Meal
import com.engineer.fred.easyfood.data.utils.Resource
import com.engineer.fred.easyfood.databinding.FragmentFavoritesBinding
import com.engineer.fred.easyfood.presentation.adapters.FavoriteMealsAdapter
import com.engineer.fred.easyfood.presentation.ui.activities.MealDetailsActivity
import com.engineer.fred.easyfood.presentation.viewModels.DetailActivityViewModel
import com.engineer.fred.easyfood.presentation.viewModels.FavoritesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment(), FavoriteMealsAdapter.ItemClick {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = checkNotNull( _binding )

    private val favoritesViewModel by viewModels<FavoritesViewModel>()
    private val detailActivityViewModel by viewModels<DetailActivityViewModel>()

    private val favoritesAdapter: FavoriteMealsAdapter by lazy { FavoriteMealsAdapter() }

    companion object {
        var favoriteMeals: List<Meal> = emptyList()
    }

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View {
        _binding = FragmentFavoritesBinding.inflate( inflater, container, false )
        favoritesAdapter.setUpItemClick( this )
        return binding.root
    }

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle( Lifecycle.State.STARTED ) {
                favoritesViewModel.meals.collectLatest {
                    when( it ){
                        Resource.Loading -> binding.loadingGifMeals.visibility = View.VISIBLE
                        is Resource.Success -> {
                            binding.loadingGifMeals.visibility = View.GONE
                            favoritesAdapter.differ.submitList( it.result )
                            favoriteMeals = it.result
                        }
                        is Resource.Failure -> Toast.makeText(requireActivity() , it.errorMessage , Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.favRecView.adapter = favoritesAdapter
        binding.favRecView.layoutManager = GridLayoutManager( requireActivity(), 2 )

        ItemTouchHelper( object : ItemTouchHelper.SimpleCallback(0 , ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT ) {
            override fun onMove( recyclerView : RecyclerView , viewHolder : RecyclerView.ViewHolder , target : RecyclerView.ViewHolder ) : Boolean = false
            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped( viewHolder : RecyclerView.ViewHolder , direction : Int ) {
                val meal = favoritesAdapter.differ.currentList[ viewHolder.adapterPosition ]
                favoritesViewModel.deleteMeal( meal ) //delete the meal from ROOM
                Snackbar.make( binding.favRecView , "Meal deleted" , Snackbar.LENGTH_LONG )
                    .setAction("Undo") {
                        detailActivityViewModel.saveMeal( meal )
                        favoritesAdapter.notifyDataSetChanged()
                    }.show()
            }
        }).attachToRecyclerView( binding.favRecView )

    }

    override fun onResume() {
        super.onResume()
        favoritesViewModel.getAllFavoritesMeals()
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

}