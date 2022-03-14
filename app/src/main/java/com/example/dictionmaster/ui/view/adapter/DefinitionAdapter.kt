package com.example.dictionmaster.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionmaster.databinding.ItemExampleAndDefinitionBinding
import com.example.dictionmaster.service.models.Definitions

class DefinitionAdapter : RecyclerView.Adapter<DefinitionAdapter.DefinitionViewHolder>() {
    inner class DefinitionViewHolder(val binding: ItemExampleAndDefinitionBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Definitions>() {
        override fun areItemsTheSame(oldItem: Definitions, newItem: Definitions): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Definitions, newItem: Definitions): Boolean {
            return oldItem.text == newItem.text
        }

    }

    private val differ = AsyncListDiffer(this, differCallBack)

    var definitions: List<Definitions>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefinitionViewHolder {
        return DefinitionViewHolder(
            ItemExampleAndDefinitionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: DefinitionViewHolder, position: Int) {
        holder.binding.apply {
            val definition = definitions[position]
            textData.text = definition.text

        }
    }

    override fun getItemCount(): Int = definitions.size
}