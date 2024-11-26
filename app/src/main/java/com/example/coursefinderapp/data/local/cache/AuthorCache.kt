package com.example.coursefinderapp.data.local.cache

import com.example.coursefinderapp.domain.entity.Author

class AuthorCacheImpl : AuthorCache {

    private val memoryCacheCourseAuthors = mutableMapOf<Int, Author>()
    private val memoryCacheAuthors = mutableMapOf<Int, Author>()

    override fun saveCourseAuthor(courseId: Int, author: Author) {
        memoryCacheCourseAuthors[courseId] = author
        memoryCacheAuthors[author.id] = author
    }

    override fun fetchAuthorByCourseId(courseId: Int): Author? =
        memoryCacheCourseAuthors[courseId]

    override fun saveAuthor(authorId: Int, author: Author) {
        memoryCacheAuthors[authorId] = author
    }

    override fun fetchAuthor(authorId: Int): Author? = memoryCacheAuthors[authorId]

    override fun clearAuthorsCache() {
        memoryCacheAuthors.clear()
        memoryCacheCourseAuthors.clear()
    }
}

interface AuthorCache {
    fun saveCourseAuthor(courseId: Int, author: Author)
    fun fetchAuthorByCourseId(courseId: Int): Author?
    fun saveAuthor(authorId: Int, author: Author)
    fun fetchAuthor(authorId: Int): Author?
    fun clearAuthorsCache()
}