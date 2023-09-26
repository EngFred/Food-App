package com.engineer.fred.easyfood.presentation.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.engineer.fred.easyfood.data.models.MealCategory
import com.engineer.fred.easyfood.data.models.MealCategoryResponse
import com.engineer.fred.easyfood.databinding.CategoryCardBinding

class MealCategoriesAdapter : Adapter<MealCategoriesAdapter.ItemViewHolder>() {

    private var itemClick: ItemClick? = null

    fun setUpItemClick( itemClick : ItemClick) {
        this.itemClick = itemClick
    }

    inner class ItemViewHolder( private val binding : CategoryCardBinding ) :  ViewHolder( binding.root ) {
        fun bind( category : MealCategory ) {
            Glide.with( itemView ).load( Uri.parse( category.imageUrl )).into( binding.imgCategory  )
            binding.tvCategoryName.text = category.name
            binding.root.setOnClickListener {
                itemClick?.showCategoryMeals( category.name )
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<MealCategory>() {
        override fun areItemsTheSame(oldItem : MealCategory , newItem : MealCategory) : Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem : MealCategory , newItem : MealCategory) : Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer( this, diffCallback )

    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : ItemViewHolder {
        val binding = CategoryCardBinding.inflate( LayoutInflater.from( parent.context), parent, false )
        return ItemViewHolder( binding )
    }

    override fun getItemCount() : Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder : ItemViewHolder , position : Int) {
        val category = differ.currentList[position]
        holder.bind( category )
    }


    interface ItemClick {
        fun showCategoryMeals( categoryName: String )
    }

}