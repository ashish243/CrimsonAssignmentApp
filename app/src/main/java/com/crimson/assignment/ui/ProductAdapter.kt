package com.crimson.assignment.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.crimson.assignment.R
import com.crimson.assignment.databinding.LayoutProductBinding
import com.crimson.assignment.model.Search
import com.crimson.assignment.ui.detail.DetailActivity

class ProductAdapter(private val context: Context) :
    PagedListAdapter<Search, ProductViewHolder>(DIFF_CALLBACK) {

    //this handle the viewBinding to avoid findByView
    private var _binding: LayoutProductBinding? = null
    private val binding get() = _binding!!


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_product, parent, false)
        _binding = LayoutProductBinding.bind(view)


        return ProductViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product = getItem(position)

        //this listener handle event when the user tap the item,

        holder.itemView.setOnClickListener {


            val intent = DetailActivity.newIntent(context)
            intent.putExtra("TITTLE_KEY",product?.title)
            intent.putExtra("POSTER_KEY",product?.poster)
            intent.putExtra("TYPE_KEY",product?.type)
            intent.putExtra("YEAR_KEY",product?.year)
            intent.putExtra("IMDBID_KEY",product?.imdbID)
            context.startActivity(intent)
        }


        try {
            holder.title.text = product?.title

            var imbdId = product?.imdbID

            holder.imbiRating.text = imbdId
            val posterPath = product?.poster

            /**
             * Using Glide to handle image loading.
             *
             */
            Glide.with(context)
                .load(posterPath)
                .placeholder(R.drawable.image_loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // cache both original & resized image
                .centerCrop()
                .transition(DrawableTransitionOptions().crossFade())
                .into(holder.productImage)

               holder.movieType.text = product?.type

                holder.releaseDate.text = product?.year.toString()


        } catch (exception: NullPointerException) {
            Log.e("ProductAdapter", "list is null")
        }


    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Search>() {
            // Product details may have changed if reloaded from the database,

            override fun areItemsTheSame(
                oldConcert: Search,
                newConcert: Search
            ) = oldConcert.title == newConcert.title

            override fun areContentsTheSame(
                oldConcert: Search,
                newConcert: Search
            ) = oldConcert == newConcert
        }
    }


}

class ProductViewHolder(binding: LayoutProductBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val title = binding.title
    val releaseDate = binding.releaseDate
    val productImage = binding.poster
    val imbiRating: TextView = binding.imbiNumber
    val movieType = binding.movieType
}
