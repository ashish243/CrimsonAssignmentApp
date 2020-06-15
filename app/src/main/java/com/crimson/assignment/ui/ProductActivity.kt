package com.crimson.assignment.ui

import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.crimson.assignment.Injection
import com.crimson.assignment.model.Search
import com.crimson.assignment.R
import com.crimson.assignment.databinding.ActivityMainBinding
import com.crimson.assignment.utils.*
import kotlinx.android.synthetic.main.activity_main.*


class ProductActivity : AppCompatActivity() {

    //this handle the viewBinding to avoid findByView
    private lateinit var _binding: ActivityMainBinding
    private val binding get() = _binding


    //keep track of products list
    private var productLists: PagedList<Search>? = null

    //[ProductAdapter] field, am using backing scope because [ProductAdapter] accept context
    private var _productAdapter: ProductAdapter? = null
    private val productAdapter get() = _productAdapter!!

    /*
    viewModel for [ProductViewModel] class
     */
    private lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get the view model
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(this)).get(
            ProductViewModel::class.java
        )


        //add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)

        //function that handle retrieval of products list

        if (checkInternetAccess()) {
            initSearchView()

        }else{
            viewModel.setValueForChache("getLocalData")
            viewModel.chacheResult.observe(this, Observer {
                initAdapter (it)
                initSearchView()

            })

        }

        retry.setOnClickListener {
            if (checkInternetAccess()) {
                retry.gone()
                refresh()

            } else {
                loading.gone()
                retry.visible()
            }

        }


        /*
        network error from [ProductResult]
         */
        viewModel.networkErrors.observe(this, Observer {
            Toast.makeText(this, "Wooops $it", Toast.LENGTH_SHORT).show()
            retry.visible()
            emptyList.text = getString(R.string.check_internet)
            loading.gone()

            //the retry visibility should be set to gone in as much the product list is not empty
            if (productLists!!.isNotEmpty())
                retry.gone()

        })


    }

    companion object {
        var TYPE =  "movie"
        var SEARCH = "guardians"
    }


    private fun getProductList() {

        viewModel.productList.observe(this, Observer {
            initAdapter(it)
        })
    }

    private fun initAdapter (it: PagedList<Search>){
        _productAdapter = ProductAdapter(this)
        showEmptyList(it.size == 0)

        productLists = it
        productAdapter.submitList(it)
        initRecyclerview()
    }
    private fun initRecyclerview() {
        recyclerView.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    /*
    if the list of the products is empty, this function will handle the view to display
    the [visible] make view visibility to visible & [gone] make view visibility to gone
     */
    private fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList.visible()
            recyclerView.gone()
            loading.visible()

        } else {
            emptyList.gone()
            recyclerView.visible()
            retry.gone()
            loading.gone()
            relLayout.gone()
            binding.searchProduct.visible()

        }
    }

    private fun refresh() {
        viewModel.networkStates.loadToRefresh()
        viewModel.networkStates.valueForRefresh.observe(this, Observer {
            if (it.status == NetworkState.LOADED.status) {

                productAdapter.notifyDataSetChanged()
                message("product updated successfully!!")
            }
            if (it.status == NetworkState.error("Internet error!, Check Internet Connection").status) {
                message("Internet error!, Check Internet Connection")

            }

        })
    }

fun getType(){
    val intSelectButton: Int = radioGroupSearch!!.checkedRadioButtonId
    val radioButton: RadioButton = findViewById(intSelectButton)
    TYPE = radioButton.text.toString()
}
    private fun initSearchView() {

        binding.searchProduct.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            /*
                trigger as user search for product name
                if data retrieve base on search keyword is empty the [submitList] should be set to null
                and th.e recyclerview should be update to result base on search

             */
            override fun onQueryTextChange(query: String): Boolean {
                if (query.trim().isNotEmpty()) {
                    relLayout.gone()
                    getType()
                    SEARCH = query

                    if (checkInternetAccess()) {
                        if (SEARCH.length > 3) {
                            viewModel.getSearchData()
                            getProductList()
                        }
                    } else {
                        viewModel.setFilterName("%${query}%")
                        viewModel.dataList.observe(this@ProductActivity, Observer {
                        productAdapter.submitList(null)
                        productAdapter.submitList(it)

                        //if product is not find base on the search keyword the [binding.relLayout] is visible
                        //hide keyboard

                        if (productAdapter.itemCount == 0) {

                            //if the loading is visible the search error view should be gone
                            if (loading.isVisible) {
                                relLayout.gone()
                            }

                            relLayout.visible()


                            please_try_again.text =
                                getString(R.string.error_message, query)
                            try {
                                hideKeyboard()
                            } catch (exc: Exception) {
                                exc.printStackTrace()
                            }

                        } else {
                            relLayout.gone()

                        }
                    })
                 }
                } else {
                    getType()
                    getProductList()
                    relLayout.gone()
                }

                return false
            }
        })
    }


    override fun onBackPressed() {
        finish()
    }

}
