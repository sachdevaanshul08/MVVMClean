package com.clean.di

import android.app.Application
import androidx.room.Room
import com.clean.BuildConfig
import com.clean.BuildConfig.CONNECTION_TIMEOUT
import com.clean.BuildConfig.READ_TIMEOUT
import com.clean.data.api.VehicleApi
import com.clean.data.db.dao.vehicles.VehiclesDao
import com.clean.data.db.database.AppDatabase
import com.clean.data.utils.calladapter.LiveDataCallAdapterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class ApiModule {

    /*
     * The method returns the Gson object
     * */
    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }


    /*
     * The method returns the Okhttp object
     * */
    @Provides
    @Singleton
    internal fun provideOkhttpClient(): OkHttpClient {

        val httpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }
        httpClient.connectTimeout(
            CONNECTION_TIMEOUT,
            TimeUnit.SECONDS
        )
        httpClient.readTimeout(
            READ_TIMEOUT,
            TimeUnit.SECONDS
        )
        return httpClient.build()
    }


    /*
     * The method returns the Retrofit object
     * */
    @Provides
    @Singleton
    internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .baseUrl(BuildConfig.BASEURL)
            .client(okHttpClient)
            .build()
    }

    /**
     * get the vehicle api
     *
     * @param retrofit
     * @return
     */
    @Provides
    @Singleton
    internal fun provideApi(retrofit: Retrofit): VehicleApi {
        return retrofit.create(VehicleApi::class.java)
    }


    @Singleton
    @Provides
    fun provideDb(app: Application): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, com.clean.data.BuildConfig.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesDeliveryDao(db: AppDatabase): VehiclesDao {
        return db.getVehiclesDao()
    }

}