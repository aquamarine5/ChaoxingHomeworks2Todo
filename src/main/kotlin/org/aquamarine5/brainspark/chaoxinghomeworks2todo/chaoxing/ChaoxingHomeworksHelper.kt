package org.aquamarine5.brainspark.chaoxinghomeworks2todo.chaoxing

import okhttp3.OkHttpClient
import org.springframework.stereotype.Service

@Service
class ChaoxingHomeworksHelper {
    companion object{
        const val URL_COURSE_QUERY="https://mooc1-api.chaoxing.com/mycourse/backclazzdata?view=json&rss=1"
    }

    fun getCourses(client: ChaoxingHttpClient){

    }
}