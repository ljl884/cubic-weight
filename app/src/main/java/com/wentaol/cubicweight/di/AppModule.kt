package com.wentaol.cubicweight.di

import android.content.Context
import com.wentaol.cubicweight.BaseApplication
import com.wentaol.cubicweight.api.IProductService
import com.wentaol.cubicweight.api.ProductService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(application: BaseApplication): Context = application.applicationContext

    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

    @Provides
    @Singleton
    fun provideProductService(
        retrofitBuilder: Retrofit.Builder
    ): IProductService = ProductService(retrofitBuilder)

}