package com.swanky.teachit.helpers

import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.swanky.teachit.R

class RecyclerItemAnimation(private val recyclerView: RecyclerView) {

    // Add Animation
    fun animateRecyclerAdd() {
        val context = recyclerView.context
        val controller: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_anim_fall_down)
        recyclerView.layoutAnimation = controller
        recyclerView.scheduleLayoutAnimation()
    }

    // Delete Animation
    fun animateRecyclerRemove() {
        val context = recyclerView.context
        val removeAnimation = AnimationUtils.loadAnimation(context, R.anim.remove_item_animation)

        // Set recyclerview animator
        recyclerView.itemAnimator = object : DefaultItemAnimator() {
            override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
                holder.itemView.startAnimation(removeAnimation)
                return super.animateRemove(holder)
            }
        }
    }
}
