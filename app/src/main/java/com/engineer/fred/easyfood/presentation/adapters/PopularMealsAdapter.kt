package com.engineer.fred.easyfood.presentation.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.engineer.fred.easyfood.data.models.PopularMeal
import com.engineer.fred.easyfood.databinding.MostPopularCardBinding

class PopularMealsAdapter : Adapter<PopularMealsAdapter.ItemViewHolder>() {

    private var itemClick: ItemClick? = null

    fun setUpItemClick( itemClick : ItemClick ) {
        this.itemClick = itemClick
    }

    inner class ItemViewHolder( private val binding : MostPopularCardBinding ) :  ViewHolder( binding.root ) {
        fun bind( popularMeal : PopularMeal ) {
            Glide.with( itemView ).load( Uri.parse( popularMeal.imageUrl )).into( binding.imgPopularMeal )
            binding.imgPopularMeal.setOnClickListener {
                itemClick?.showMealDetail( popularMeal.id )
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<PopularMeal>() {
        override fun areItemsTheSame(oldItem : PopularMeal , newItem : PopularMeal) : Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem : PopularMeal , newItem : PopularMeal) : Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer( this, diffCallback )

    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : ItemViewHolder {
        val binding = MostPopularCardBinding.inflate( LayoutInflater.from( parent.context), parent, false )
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