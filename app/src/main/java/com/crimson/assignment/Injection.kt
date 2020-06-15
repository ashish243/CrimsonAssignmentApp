package com.crimson.assignment

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.crimson.assignment.api.ProductService
import com.crimson.assignment.data.DetailRepository
import com.crimson.assignment.db.ProductLocalCache
import com.crimson.assignment.data.ProductRepository
import com.crimson.assignment.db.ProductDatabase
import com.crimson.assignment.ui.detail.DetailViewModelFactory
import com.crimson.assignment.ui.ViewModelFactory
import java.util.concurrent.Executors


/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Creates an instance of [ProductLocalCache] based on the database DAO.
     */
    private fun provideCache(context: Context): ProductLocalCache {
        val database = ProductDatabase.getInstance(context)
        return ProductLocalCache(database.productDao(), Executors.newSingleThreadExecutor())
    }

    /**
     * Creates an instance of [ProductRepository] based on the [ProductService] and a
     * [ProductLocalCache]
     */
    private fun provideProductRepository(context: Context): ProductRepository {
        return ProductRepository(ProductService.create(), provideCache(context))
    }

    /**
     * Creates an instance of [DetailRepository] based on the [ProductService] and a
     * [ProductLocalCache]
     */
    private fun provideDetailProductRepository(context: Context): DetailRepository {
        return DetailRepository(ProductService.create(), provideCache(context))
    }


    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideProductRepository(context))
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideDetailViewModelFactory(context: Context): ViewModelProvider.Factory {
        return DetailViewModelFactory(provideDetailProductRepository(context))
    }
}
