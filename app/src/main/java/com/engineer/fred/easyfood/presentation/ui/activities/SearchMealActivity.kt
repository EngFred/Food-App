package com.engineer.fred.easyfood.presentation.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.engineer.fred.easyfood.R
import com.engineer.fred.easyfood.data.utils.Resource
import com.engineer.fred.easyfood.databinding.ActivitySearchBinding
import com.engineer.fred.easyfood.presentation.adapters.MealSearchAdapter
import com.engineer.fred.easyfood.presentation.viewModels.SearchActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchMealActivity : AppCompatActivity(), MealSearchAdapter.ItemClick {

    private var  _binding: ActivitySearchBinding? = null
    private val binding
        get()=checkNotNull( _binding )

    private val  searchViewModel: SearchActivityViewModel by viewModels()
    private val mealSearchAdapter: MealSearchAdapter by lazy { MealSearchAdapter() }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme( R.style.Theme_EasyFood )
        _binding = ActivitySearchBinding.inflate( layoutInflater )
        mealSearchAdapter.setUpItemClick( this )
        setContentView( binding.root )

        binding.searchView.setOnQueryTextListener( object : OnQueryTextListener {
            override fun onQueryTextSubmit(query : String?) : Boolean {
                query?.let {
                    searchViewModel.searchMeal( it.lowercase() )
                    hideKeyBoard()
                }
                return true
            }
            override fun onQueryTextChange(newText : String?) : Boolean {
                return false
            }
        })

        binding.searchRv.adapter = mealSearchAdapter
        binding.searchRv.layoutManager = LinearLayoutManager( this )

        lifecycleScope.launch {
            repeatOnLifecycle( Lifecycle.State.STARTED ) {
                searchViewModel.search.collectLatest {
                    when( it ) {
                        Resource.Loading -> binding.loadingGifMeals.visibility = View.VISIBLE
                        is Resource.Success -> {
                            binding.loadingGifMeals.visibility = View.GONE
                            mealSearchAdapter.differ.submitList( it.result )
                        }
                        is Resource.Failure -> {
                            binding.loadingGifMeals.visibility = View.GONE
                            Toast.makeText(this@SearchMealActivity , it.errorMessage , Toast.LENGTH_SHORT).show()
                        }
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
        val intent = Intent( this, MealDetailsActivity::class.java )
        intent.putExtra( "meal_id", mealId )
        startActivity( intent )
    }

    private fun hideKeyBoard() {
        val imm = getSystemService( INPUT_METHOD_SERVICE ) as InputMethodManager
        imm.hideSoftInputFromWindow( currentFocus?.windowToken , 0 )
    }

}