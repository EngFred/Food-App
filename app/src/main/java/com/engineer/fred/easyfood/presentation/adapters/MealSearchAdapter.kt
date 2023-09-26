package com.engineer.fred.easyfood.presentation.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.engineer.fred.easyfood.data.models.Meal
import com.engineer.fred.easyfood.data.models.MealCategory
import com.engineer.fred.easyfood.data.models.MealCategoryResponse
import com.engineer.fred.easyfood.databinding.CategoryCardBinding
import com.engineer.fred.easyfood.databinding.SearchMealViewBinding

class MealSearchAdapter : Adapter<MealSearchAdapter.ItemViewHolder>() {

    private var itemClick: ItemClick? = null

    fun setUpItemClick( itemClick : ItemClick) {
        this.itemClick = itemClick
    }

    inner class ItemViewHolder( private val binding : SearchMealViewBinding ) :  ViewHolder( binding.root ) {
        fun bind( meal : Meal) {
            Glide.with( itemView ).load( Uri.parse( meal.imageUrl )).into( binding.imgSearchedMeal  )
            binding.tvSearchedMeal.text = meal.name
            binding.root.setOnClickListener {
                itemClick?.showMealDetail( meal.id )
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem : Meal , newItem : Meal) : Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem : Meal , newItem : Meal) : Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer( this, diffCallback )

    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : ItemViewHolder {
        val binding = SearchMealViewBinding.inflate( LayoutInflater.from( parent.context), parent, false )
        return ItemViewHolder( binding )
    }

    override fun getItemCount() : Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder : ItemViewHolder , position : Int) {
        val meal = differ.currentList[position]
        holder.bind( meal )
    }


    interface ItemClick {
        fun showMealDetail( mealId: String )
    }

}