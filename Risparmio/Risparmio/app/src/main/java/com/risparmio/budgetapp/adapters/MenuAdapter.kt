package com.risparmio.budgetapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.risparmio.budgetapp.R
import com.risparmio.budgetapp.activities.MenuItem

class MenuAdapter(
    private val items: List<MenuItem>,
    private val onClick: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val emoji: TextView = view.findViewById(R.id.txtEmoji)
        val title: TextView = view.findViewById(R.id.txtTitle)
        val subtitle: TextView = view.findViewById(R.id.txtSubtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu_card, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = items[position]
        holder.emoji.text = item.emoji
        holder.title.text = item.title
        holder.subtitle.text = item.subtitle
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size
}
