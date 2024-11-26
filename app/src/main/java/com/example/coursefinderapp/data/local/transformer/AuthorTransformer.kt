package com.example.coursefinderapp.data.local.transformer

import com.example.coursefinderapp.data.local.database.model.AuthorModel
import com.example.coursefinderapp.domain.entity.Author

class AuthorTransformer {

    fun fromModel(model: AuthorModel): Author {
        return Author(
            id = model.id,
            authorName = model.fullName,
            avatar = model.avatar
        )
    }

    fun toModel(author: Author): AuthorModel {
        return AuthorModel(
            id = author.id,
            fullName = author.authorName,
            avatar = author.avatar
        )
    }
}