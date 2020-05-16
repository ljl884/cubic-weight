package com.wentaol.cubicweight.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.wentaol.cubicweight.R
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), RequestHandler {
    // region Instance variables

    @Inject
    lateinit var viewModel: MainViewModel

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val productListAdapter = ProductListAdapter()

    // endregion

    // region Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.addCategoryFilter("Air Conditioners")
        productListView.adapter = productListAdapter
        productListView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        bindView()
        refresh()
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    // endregion

    // region ProductActivity

    private fun refresh() {
        onRequestStart()
        viewModel.loadProductData()
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                updateCategoryFilters()
                onRequestFinish()
            }, {
                onRequestFinish()
                handleError(it)
            })
            .addTo(compositeDisposable)
    }

    private fun updateCategoryFilters() {
        runOnUiThread {
            filterGroup.removeAllViews()
            // add filter chips for all categories.
            viewModel.categoryList.sorted().forEach {
                val chip = Chip(this)
                chip.setChipDrawable(ChipDrawable.createFromAttributes(this, null, 0, R.style.Widget_MaterialComponents_Chip_Filter))
                chip.text = it
                // If the category is already in filter, set it checked.
                chip.isChecked = viewModel.currentCategoryFilter.contains(it)
                chip.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        viewModel.addCategoryFilter(chip.text.toString())
                    } else {
                        viewModel.removeCategoryFilter(chip.text.toString())
                    }
                }
                filterGroup.addView(chip)
            }
        }
    }

    private fun bindView() {
        viewModel.productList
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                productListAdapter.updateData(it)
            }
            .addTo(compositeDisposable)

        viewModel.averageCubicWeight
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                averageCubicWeightText.text = getString(R.string.average_cubic_weight, it.toString())
            }
            .addTo(compositeDisposable)
    }

    private fun handleError(throwable: Throwable) {
        runOnUiThread {
            Toast.makeText(this, throwable.message, Toast.LENGTH_SHORT).show()
        }
    }

    // endregion

    // region RequestHandler

    override fun onRequestStart() {
        runOnUiThread {
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun onRequestFinish() {
        runOnUiThread {
            progressBar.visibility = View.INVISIBLE
        }
    }

    // endregion
}
