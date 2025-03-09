package org.aquamarine5.brainspark.chaoxinghomeworks2todo.msgraph

import com.alibaba.fastjson2.JSONObject
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.RequestEntity
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.RestTemplate
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*

@Controller
@RequestMapping("/graph")
class IndexController(@Autowired private val okHttpClient: OkHttpClient) {

    @Value("\${graph.client-id}")
    private lateinit var clientId: String

    @Value("\${graph.redirect-url}")
    private lateinit var redirectUrl: String

    @Value("\${graph.scope}")
    private lateinit var scope: String

    @Value("\${graph.client-secret}")
    private lateinit var clientSecret: String

    @GetMapping("/graph")
    fun graphPage(
        model: Model,
        authentication: OAuth2AuthenticationToken,
        @RegisteredOAuth2AuthorizedClient("graph") msalGraphClient: OAuth2AuthorizedClient
    ): String {
        model.addAttribute("userName", authentication.name)
        val restTemplate = RestTemplateBuilder().build()
        val request = RequestEntity.get("https://graph.microsoft.com/v1.0/me")
            .header("Authorization", "Bearer ${msalGraphClient.accessToken.tokenValue}").build()
        val response = restTemplate.exchange(request, String::class.java)
        model.addAttribute("jsonBody", response.body)
        model.addAttribute("graphAccessTokenExpiresAt", msalGraphClient.accessToken.expiresAt)
        return "graph"
    }

    @GetMapping("/login")
    fun requestLogin(): JSONObject {
        val url =
            "https://login.microsoftonline.com/common/oauth2/v2.0/authorize?client_id=${clientId}&response_type=code&redirect_uri=${
                URLEncoder.encode(
                    redirectUrl,
                    StandardCharsets.UTF_8
                )
            }&response_mode=query&scope=${
                URLEncoder.encode(
                    scope,
                    StandardCharsets.UTF_8)
            }&state=${
                UUID.randomUUID()
            }"
        return JSONObject().fluentPut("url",url)
    }

    @RequestMapping("/redirect")
    fun loginRedirect(@RequestParam code:String,@RequestParam state:String,@RequestParam("session_state") sessionState:String){
        val request=Request.Builder()
            .url("https://login.microsoftonline.com/common/oauth2/v2.0/token")
            .post(
                FormBody.Builder()
                    .addEncoded("client_id",clientId)
                    .addEncoded("scope",scope)
                    .addEncoded("code",code)
                    .addEncoded("client_secret",clientSecret)
                    .addEncoded("grant_type","authorization_code")
                    .addEncoded("redirect_uri",redirectUrl)
                    .build()
            )
            .build()
        val response=okHttpClient.newCall(request).execute()
        val json=JSONObject.parseObject(response.body?.string())
    }


}