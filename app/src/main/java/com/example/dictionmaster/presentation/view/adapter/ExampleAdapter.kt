package com.example.dictionmaster.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionmaster.databinding.ItemviewBinding
import com.example.dictionmaster.data.models.Examples

class ExampleAdapter : RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder>() {
    inner class ExampleViewHolder(val binding: ItemviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Examples>() {
        override fun areItemsTheSame(oldItem: Examples, newItem: Examples): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Examples, newItem: Examples): Boolean {
            return oldItem.text == newItem.text
        }
    }

    private val differ = AsyncListDiffer(this, differCallBack)

    var examples: List<Examples>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        return ExampleViewHolder(
            ItemviewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        holder.binding.apply {
            val example = examples[position]
            textData.text = example.text
        }
    }

    override fun getItemCount(): Int = if (examples.size > 3) 3 else examples.size
}