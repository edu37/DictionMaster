package com.example.dictionmaster.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionmaster.databinding.ItemviewBinding
import com.example.dictionmaster.service.models.Sense

class DefinitionAdapter : RecyclerView.Adapter<DefinitionAdapter.DefinitionViewHolder>() {
    inner class DefinitionViewHolder(val binding: ItemviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Sense>() {
        override fun areItemsTheSame(oldItem: Sense, newItem: Sense): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Sense, newItem: Sense): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, differCallBack)

    var sense: List<Sense>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefinitionViewHolder {
        return DefinitionViewHolder(
            ItemviewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: DefinitionViewHolder, position: Int) {

        holder.binding.apply {
            try {
                if (sense[position].definitions[0].isNotEmpty())
                    textData.text = sense[position].definitions[0]
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getItemCount(): Int {

        return sense.size
    }
}