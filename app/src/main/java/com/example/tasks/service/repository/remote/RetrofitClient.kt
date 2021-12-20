package com.example.tasks.service.repository.remote

import com.example.tasks.service.constants.TaskConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    companion object {

        /**
         * Classe responsável pelo Retrfit client que inicia a conexão com a chamada
         * da API
         *
         */

        private lateinit var retrofit: Retrofit
        private val baseUrl = "http://devmasterteam.com/CursoAndroidAPI/"
        private var personKey = ""
        private var tokenKey = ""


        /**
         * Função getRetrofitClient tem a função de inicializar a conexão com CursoAndroidAPI,
         * verifica se a váriavel retrofit foi inicializada, caso não tenha sido incializada
         * a função inicia e retorna um dado do tipo Retofit
         */

        private fun getRetrofitClient(): Retrofit {

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(object  : Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request =
                        chain.request()
                            .newBuilder()
                            .addHeader(TaskConstants.HEADER.PERSON_KEY,personKey)
                            .addHeader(TaskConstants.HEADER.TOKEN_KEY,tokenKey)
                            .build()
                    return chain.proceed(request)
                }

            })

            if (!::retrofit.isInitialized) {
                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit

        }


        fun addHeader(token: String, personKey: String){
           this.personKey = personKey
            this.tokenKey = token


        }

        /**
         * Função createService responsável por criar qualquer tipo de serviço
         */

        fun <T> createService(serviceClass: Class<T>): T {
            return getRetrofitClient().create(serviceClass)
        }

    }

}