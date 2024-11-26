package com.example.coursefinderapp.data.local.repository

import com.example.coursefinderapp.data.local.database.dao.AuthorDao
import com.example.coursefinderapp.data.local.transformer.AuthorTransformer
import com.example.coursefinderapp.domain.entity.Author
import com.example.coursefinderapp.domain.repository.local.AuthorLocalRepository
import javax.inject.Inject

class AuthorLocalRepositoryImpl @Inject constructor(
    private val authorDao: AuthorDao
) : AuthorLocalRepository {

    private val authorTransformer = AuthorTransformer()

    override suspend fun fetchAll(): List<Author> {
        val model = authorDao.fetchAll()
        return model.map {
            authorTransformer.fromModel(it)
        }
    }

    override suspend fun fetchById(authorId: Int): Author? {
        val model = authorDao.fetchById(authorId)
        return if (model != null) {
            authorTransformer.fromModel(model)
        } else {
            null
        }
    }

    override suspend fun insertOne(author: Author) {
        val model = authorTransformer.toModel(author)
        return authorDao.insertOne(model)
    }

    override suspend fun deleteById(authorId: Int) = authorDao.deleteById(authorId)

    override suspend fun updateOne(author: Author) {
        val model = authorTransformer.toModel(author)
        return authorDao.updateOne(model)
    }
}