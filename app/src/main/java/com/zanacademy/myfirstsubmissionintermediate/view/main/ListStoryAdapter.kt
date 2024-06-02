package com.zanacademy.myfirstsubmissionintermediate.view.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zanacademy.myfirstsubmissionintermediate.R
import com.zanacademy.myfirstsubmissionintermediate.data.response.ListStoryItem
import com.zanacademy.myfirstsubmissionintermediate.databinding.ListStoryBinding
import com.zanacademy.myfirstsubmissionintermediate.view.detailstory.DetailStoryActivity

class ListStoryAdapter: PagingDataAdapter<ListStoryItem, ListStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listStory = getItem(position)

        if (listStory != null) {
            holder.bind(listStory)
        }
    }

    class MyViewHolder(private val binding: ListStoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(listStory: ListStoryItem) {
            binding.title.text = listStory.name
            binding.description.text = listStory.description

            Glide.with(itemView)
                .load(listStory.photoUrl)
                .into(binding.thumbnail)

            val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
            val name: TextView = itemView.findViewById(R.id.title)
            val description: TextView = itemView.findViewById(R.id.description)

            itemView.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(thumbnail, "thumbnail"),
                        Pair(name, "name"),
                        Pair(description, "description")
                    )

                val moveWithDataUser = Intent(itemView.context, DetailStoryActivity::class.java)
                moveWithDataUser.putExtra("PHOTO_URL", listStory.photoUrl)
                moveWithDataUser.putExtra("NAME", listStory.name)
                moveWithDataUser.putExtra("DESCRIPTION", listStory.description)
                itemView.context.startActivity(moveWithDataUser, optionsCompat.toBundle())
            }
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }


}