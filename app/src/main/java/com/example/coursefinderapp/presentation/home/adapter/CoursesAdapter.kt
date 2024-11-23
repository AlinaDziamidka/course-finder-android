package com.example.coursefinderapp.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.coursefinderapp.R
import com.example.coursefinderapp.databinding.CourseCardBinding
import com.example.coursefinderapp.domain.entity.Course
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CoursesAdapter(
    private val context: Context,
    private val onDescriptionClickListener: (Course) -> Unit,
    private val onFavoriteClickListener: (Course) -> Unit
) : PagingDataAdapter<Course, CoursesAdapter.CoursesViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Course>() {
            override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean =
                oldItem == newItem
        }
    }

    class CoursesViewHolder(
        itemView: View,
        private val onDescriptionClickListener: (Course) -> Unit,
        private val onFavoriteClickListener: (Course) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = CourseCardBinding.bind(itemView)
        private lateinit var addToFavoriteActionView: MaterialButton
        private lateinit var ratingView: TextView
        private lateinit var courseDateView: TextView
        private lateinit var courseNameView: TextView
        private lateinit var courseSummaryView: TextView
        private lateinit var priceView: TextView
        private lateinit var moveToDescriptionView: LinearLayout

        init {
            bindViews()
        }

        private fun bindViews() {
            addToFavoriteActionView = binding.addToFavoriteActionView
            ratingView = binding.ratingView
            courseDateView = binding.courseDateView
            courseNameView = binding.courseNameView
            courseSummaryView = binding.courseDescriptionView
            priceView = binding.priceView
            moveToDescriptionView = binding.moreActionView
        }

        fun onBind(course: Course, context: Context) {


            val rating = if (course.rating == null) {
                context.getString(R.string.home_no_rating)
            } else {
                course.rating.toString()
            }
            ratingView.text = rating


            courseNameView.text = course.title
            courseSummaryView.text = course.summary

            val price = if (course.price.isNullOrEmpty()) {
                context.getString(R.string.home_course_price_free)
            } else {
                val formattedPrice =
                    course.price.toDoubleOrNull()?.toInt()?.toString() ?: course.price
                context.getString(R.string.home_course_price, formattedPrice)
            }
            priceView.text = price


            addToFavoriteActionView.setOnClickListener {
                onFavoriteClickListener(course)
            }

            moveToDescriptionView.setOnClickListener {
                onDescriptionClickListener(course)
            }

            if (course.createDate.isNullOrEmpty()) {
                courseDateView.visibility = View.GONE
            } else {
                courseDateView.visibility = View.VISIBLE
                val formattedDate = formatDate(course.createDate)
                courseDateView.text = formattedDate
            }

        }

        private fun formatDate(dateString: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
                val date = inputFormat.parse(dateString)
                outputFormat.format(date ?: Date())
            } catch (e: Exception) {
                "Некорректная дата"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursesViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.course_card, parent, false)
        return CoursesViewHolder(itemView, onDescriptionClickListener, onFavoriteClickListener)
    }

    override fun onBindViewHolder(holder: CoursesViewHolder, position: Int) {
        val course = getItem(position)
        if (course != null) {
            holder.onBind(course, context)
        }
    }

}