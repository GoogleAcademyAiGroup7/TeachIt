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
    // DiffUtil.ItemCallback'i anonim nesne olarak tanımla
    object : DiffUtil.ItemCallback<SpeechRecord>() {
        override fun areItemsTheSame(oldItem: SpeechRecord, newItem: SpeechRecord): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SpeechRecord, newItem: SpeechRecord): Boolean {
            return oldItem == newItem
        }
    }
) {

    /**
     * Her bir liste öğesinin view'larını tutan ViewHolder.
     * Tıklama olayını da dinler.
     */
    inner class SpeechViewHolder(private val binding: ItemSpeechCardBinding) : RecyclerView.ViewHolder(binding.root) {
        /**
         * Verilen SpeechRecord verisini ViewHolder'daki view'lara bağlar
         * ve tıklama dinleyicisini ayarlar.
         * @param record Gösterilecek SpeechRecord.
         */
        fun bind(record: SpeechRecord) {
            binding.tvCardText.text = record.text
            // Tüm CardView'a tıklama dinleyicisi ekle
            binding.root.setOnClickListener {
                // Adapter'a iletilen callback fonksiyonunu çağır
                onItemClicked(record)
            }
        }
    }

    /**
     * Yeni bir ViewHolder örneği oluşturur.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeechViewHolder {
        val binding = ItemSpeechCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpeechViewHolder(binding)
    }

    /**
     * Belirtilen pozisyondaki veriyi ViewHolder'a bağlar.
     */
    override fun onBindViewHolder(holder: SpeechViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}