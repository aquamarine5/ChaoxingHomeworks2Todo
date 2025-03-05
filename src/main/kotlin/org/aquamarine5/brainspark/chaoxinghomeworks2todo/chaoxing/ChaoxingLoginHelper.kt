package org.aquamarine5.brainspark.chaoxinghomeworks2todo.chaoxing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.FormBody
import okhttp3.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Service
class ChaoxingLoginHelper {
    @Autowired
    private lateinit var chaoxingHttpClient: ChaoxingHttpClient
    fun encryptByAES(message: String, key: String = Companion.TRANSFER_KEY): String {
        val iv = IvParameterSpec(key.toByteArray(Charsets.UTF_8))
        val secretKey = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv)
        val encrypted = cipher.doFinal(message.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(encrypted)
    }

    suspend fun login(phoneNumber: Int, password: String) = withContext(Dispatchers.IO) {
        val uname = encryptByAES(phoneNumber.toString())
        val encryptedPassword = encryptByAES(password)
        val request = Request.Builder()
            .url(URL_LOGIN)
            .post(FormBody.Builder().apply {
                addEncoded("fid", "-1")
                addEncoded("uname", uname)
                addEncoded("password", encryptedPassword)
                addEncoded("refer", "https%3A%2F%2Fi.chaoxing.com")
                addEncoded("t", "true")
                addEncoded("forbidotherlogin", "0")
                addEncoded("validate", "")
                addEncoded("doubleFactorLogin", "0")
                addEncoded("independentId", "0")
                addEncoded("independentNameId", "0")
            }.build())
            .build()
        chaoxingHttpClient.client.newCall(request).execute().use {

        }
    }

    companion object {
        private const val TRANSFER_KEY = "u2oh6Vu^HWe4_AES"
        private const val URL_LOGIN = "https://passport2.chaoxing.com/fanyalogin"
    }
}