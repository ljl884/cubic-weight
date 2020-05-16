package com.wentaol.cubicweight.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.wentaol.cubicweight.R
import kotlinx.android.synthetic.main.row_product_item.view.*

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        const val LAYOUT = R.layout.row_product_item
    }

    fun bind(name: String, cubicWeight: Double) {
        itemView.nameText.text = name
        itemView.cubicWeightText.text = cubicWeight.toString()
    }
}