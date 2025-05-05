package com.swanky.teachit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.swanky.teachit.databinding.ItemSpeechCardBinding
import com.swanky.teachit.db.entity.SpeechRecord

class SpeechListAdapter(
    private val onItemClicked: (SpeechRecord) -> Unit // Tıklama dinleyicisi callback'i
) : ListAdapter<SpeechRecord, SpeechListAdapter.SpeechViewHolder>(
    object : DiffUtil.ItemCallback<SpeechRecord>() {
        override fun areItemsTheSame(oldItem: SpeechRecord, newItem: SpeechRecord): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SpeechRecord, newItem: SpeechRecord): Boolean {
            return oldItem == newItem
        }
    }
) {

    inner class SpeechViewHolder(private val binding: ItemSpeechCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: SpeechRecord) {
            binding.tvCardText.text = record.text
            // Tüm CardView'a tıklama dinleyicisi ekle
            binding.root.setOnClickListener {
                // Adapter'a iletilen callback fonksiyonunu çağır
                onItemClicked(record)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeechViewHolder {
        val binding = ItemSpeechCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpeechViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpeechViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}