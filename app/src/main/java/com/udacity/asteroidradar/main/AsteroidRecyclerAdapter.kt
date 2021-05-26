package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.ViewItemBinding

class AsteroidRecyclerAdapter(private val listener: Listener): ListAdapter<Asteroid, AsteroidRecyclerAdapter.AsteroidViewHolder>(DiffCallBack){
    companion object DiffCallBack: DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder.from(parent)
    }

    class AsteroidViewHolder private constructor(private val binding: ViewItemBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): AsteroidViewHolder {
                val binding = ViewItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
                return AsteroidViewHolder(binding)
            }
        }

        fun bind(item: Asteroid, listener: Listener) {
            binding.asteroid = item
            binding.asteroidItem.setOnClickListener{
                listener.onClick(item)
            }
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

class Listener(private val onClickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = onClickListener(asteroid)
}