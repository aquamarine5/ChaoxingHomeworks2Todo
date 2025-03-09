package org.aquamarine5.brainspark.chaoxinghomeworks2todo

import okhttp3.OkHttpClient
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class ChaoxingHomeworks2TodoApplication{

    @Bean
    fun sharedOkhttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }



}
fun main(args: Array<String>) {
    runApplication<ChaoxingHomeworks2TodoApplication>(*args)
}