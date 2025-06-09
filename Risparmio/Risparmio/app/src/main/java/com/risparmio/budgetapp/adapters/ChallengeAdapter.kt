package com.risparmio.budgetapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.risparmio.budgetapp.R
import com.risparmio.budgetapp.data.model.Challenge

class ChallengeAdapter(
    private var challenges: List<Challenge>
) : RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {

    class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvChallengeTitle)
        val description: TextView = itemView.findViewById(R.id.tvChallengeDescription)
        val rewardPoints: TextView = itemView.findViewById(R.id.tvRewardPoints)
        val timeRemaining: TextView = itemView.findViewById(R.id.tvTimeRemaining)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_challenge, parent, false)
        return ChallengeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val challenge = challenges[position]
        holder.title.text = challenge.title
        holder.description.text = challenge.description
        holder.rewardPoints.text = "${challenge.rewardPoints} pts"
        holder.timeRemaining.text = challenge.timeRemaining
    }

    override fun getItemCount(): Int = challenges.size


    fun updateList(newList: List<Challenge>) {
        this.challenges = newList
        notifyDataSetChanged()
    }
}
