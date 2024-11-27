package com.example.coursefinderapp.data.local.repository

import com.example.coursefinderapp.data.local.database.dao.CourseDao
import com.example.coursefinderapp.data.local.transformer.CourseTransformer
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.repository.local.CourseLocalRepository
import javax.inject.Inject

class CourseLocalRepositoryImpl @Inject constructor(
    private val courseDao: CourseDao
) : CourseLocalRepository {

    private val courseTransformer = CourseTransformer()

    override suspend fun fetchAll(): List<Course> {
        val model = courseDao.fetchAll()
        return model.map {
            courseTransformer.fromModel(it)
        }
    }

    override suspend fun getAllIds(): List<Int> = courseDao.getAllIds()

    override suspend fun fetchById(courseId: Int): Course? {
        val model = courseDao.fetchById(courseId)
        return if (model != null) {
            courseTransformer.fromModel(model)
        } else {
            null
        }
    }

    override suspend fun insertOne(course: Course) {
        val model = courseTransformer.toModel(course)
        return courseDao.insertOne(model)
    }

    override suspend fun deleteById(courseId: Int) = courseDao.deleteById(courseId)

    override suspend fun updateOne(course: Course) {
        val model = courseTransformer.toModel(course)
        return courseDao.updateOne(model)
    }
}