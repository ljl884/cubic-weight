package com.wentaol.cubicweight.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wentaol.cubicweight.R
import com.wentaol.cubicweight.model.Product
import com.wentaol.cubicweight.model.cubicWeight

class ProductListAdapter : RecyclerView.Adapter<ProductViewHolder>() {

    private val data: MutableList<Product> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.row_product_item, parent, false)
        return ProductViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = data[position]
        holder.bind(product.title, product.cubicWeight)
    }

    override fun getItemCount(): Int = data.size

    fun updateData(products: List<Product>) {
        data.clear()
        data.addAll(products)
        notifyDataSetChanged()
    }
}