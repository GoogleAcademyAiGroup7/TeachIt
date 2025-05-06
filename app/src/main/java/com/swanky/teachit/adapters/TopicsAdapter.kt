package com.swanky.teachit.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.swanky.teachit.activities.LearningAssistantActivity
import com.swanky.teachit.databinding.RowTopicBinding
import com.swanky.teachit.models.Topic
import com.swanky.teachit.utils.dateFormat
import com.swanky.teachit.helpers.ColorCycler

class TopicsAdapter(private val context: Context, private var topicList: List<Topic>) :
    RecyclerView.Adapter<TopicsAdapter.ViewHolder>(), Filterable {

    private var filteredList: List<Topic> = topicList

    class ViewHolder(val binding: RowTopicBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = filteredList[position]

        // Set dynamic background color
        val color = ColorCycler(context).getColor(position)
        holder.binding.cardTopic.setCardBackgroundColor(color)

        holder.binding.topicTitleRow.text = topic.title
        holder.binding.topicDescRow.text = topic.description
        holder.binding.topicCreatedAtRow.text = "${topic.createdAt.dateFormat()} tarihinde eklendi."
        holder.binding.topicStudyCountRow.text = topic.studyCount.toString()
        holder.binding.topicLastStudiedAtRow.text = topic.lastStudiedAt?.let {
            "Son çalışma tarihi\n${it.dateFormat()}"
        } ?: "Çalışma verisi bulunmuyor"

        // Handle item click
        holder.itemView.setOnClickListener {
            // Start LearningAssistantActivity with the selected topic
            val intent = Intent(context, LearningAssistantActivity::class.java)
            intent.apply {
                putExtra("selectedTopic", topic)
            }
            context.startActivity(intent)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredResults = if (constraint.isNullOrEmpty()) {
                    topicList
                } else {
                    topicList.filter { topic ->
                        topic.title.contains(constraint, ignoreCase = true) ||
                                topic.description?.contains(constraint, ignoreCase = true) ?: false
                    }
                }
                return FilterResults().apply {
                    values = filteredResults
                    count = filteredResults.size
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val newList = results?.values as? List<Topic> ?: emptyList()
                val diffCallback = TopicDiffCallback(filteredList, newList)
                val diffResult = DiffUtil.calculateDiff(diffCallback)
                filteredList = newList
                diffResult.dispatchUpdatesTo(this@TopicsAdapter)
            }
        }
    }

    private class TopicDiffCallback(
        private val oldList: List<Topic>,
        private val newList: List<Topic>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }
}