package com.example.coursefinderapp.data.remote.transformer

import com.example.coursefinderapp.data.remote.api.response.AuthorResponse
import com.example.coursefinderapp.data.remote.api.response.CourseAndMetaResponse
import com.example.coursefinderapp.domain.entity.Author
import com.example.coursefinderapp.domain.entity.StepikMeta

class AuthorTransformer {

    fun fromResponse(response: AuthorResponse): Author {
        return Author(
            id = response.id,
            authorName = response.fullName,
            avatar = response.avatar
        )
    }
}