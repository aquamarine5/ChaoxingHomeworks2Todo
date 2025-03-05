package org.aquamarine5.brainspark.chaoxinghomeworks2todo.chaoxing

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

@Service
class ChaoxingHttpClient {
    suspend fun create(phoneNumber: Int, password: String): OkHttpClient {
        val cookieJar: CookieJar = object : CookieJar {
            private val cookieStore: MutableMap<String, List<Cookie>> = mutableMapOf()

            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                cookieStore[url.host] = cookies
            }

            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                return cookieStore[url.host] ?: listOf()
            }
        }
        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .build().apply {
                chaoxingLoginHelper.login(phoneNumber, password)
            }

    }

    @Autowired
    private lateinit var chaoxingLoginHelper: ChaoxingLoginHelper
}