package com.example.coursefinderapp.presentation.account.adapter

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coursefinderapp.R
import com.example.coursefinderapp.databinding.CourseCardBinding
import com.example.coursefinderapp.databinding.UserCourseCardBinding
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.presentation.favorite.adapter.FavoriteAdapter
import com.example.coursefinderapp.util.image.CropTopTransformation
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AccountAdapter(
    private val context: Context,
    private var courses: MutableList<Course>
) :
    RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    class AccountViewHolder(
        itemView: View
    ) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = UserCourseCardBinding.bind(itemView)
        private lateinit var addToFavoriteView: MaterialButton
        private lateinit var ratingView: TextView
        private lateinit var courseDateView: TextView
        private lateinit var courseNameView: TextView
        private lateinit var courseImageView: ImageView
        private lateinit var progressPercentView: TextView
        private lateinit var lessonCompletedCountView: TextView
        private lateinit var lessonCountView: TextView
        private lateinit var lessonProgressBar: ProgressBar
        private lateinit var cardContainer: LinearLayout

        init {
            bindViews()
        }

        private fun bindViews() {
            addToFavoriteView = binding.addToFavoriteActionView
            ratingView = binding.ratingView
            courseDateView = binding.courseDateView
            courseNameView = binding.courseNameView
            courseImageView = binding.courseImageView
            progressPercentView = binding.progressPercentView
            lessonCompletedCountView = binding.lessonCompletedCountView
            lessonCountView = binding.lessonCountView
            lessonProgressBar = binding.lessonProgressBar
            cardContainer = binding.rootContainer
        }

        fun onBind(course: Course, context: Context) {
            courseNameView.text = course.title
            setCardBackground(course)
            setCreationDate(course)
            setRating(course, context)
            setImage(context, course)
            setOnFavoriteView(course)
            setCourseProgress(course, context)
        }

        private fun setCardBackground(course: Course) {
            if (course.isFavorite) {
                cardContainer.setBackgroundResource(R.drawable.bg_course_card_green)
            }
        }

        private fun setCreationDate(course: Course) {
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
                "Invalid date"
            }
        }

        private fun setRating(
            course: Course,
            context: Context
        ) {
            val rating = if (course.rating?.compareTo(0.0f) == 0) {
                context.getString(R.string.home_no_rating)
            } else {
                String.format(Locale.US, "%.1f", course.rating)
            }
            ratingView.text = rating
        }

        private fun setImage(
            context: Context,
            course: Course
        ) {
            Glide.with(context)
                .load(course.coverImage)
                .placeholder(R.drawable.course_image_placeholder)
                .error(R.drawable.error_course_image_placeholder)
                .transform(CropTopTransformation())
                .into(courseImageView)
        }

        private fun setOnFavoriteView(course: Course) {
            if (course.isFavorite) {
                addToFavoriteView.setIconResource(R.drawable.ic_favorite_selected)
                addToFavoriteView.setIconTintResource(R.color.green_color_button_default)
            }
        }

        private fun setCourseProgress(course: Course, context: Context) {
            val totalLessons = course.lessonCount
            val completedLessons = 1
            val progress = calculateProgress(totalLessons, completedLessons)

            setProgressPercent(course, progress, context)
            setCompletedLessons(course, completedLessons, context)
            setTotalLesson(course, totalLessons, context)
            setProgressBar(course, progress, context)
        }

        private fun calculateProgress(totalLessons: Int?, completedLessons: Int?): Int {
            val safeTotalLessons = totalLessons ?: 0
            val safeCompletedLessons = completedLessons ?: 0

            if (safeTotalLessons == 0) return 0
            return (safeCompletedLessons * 100) / safeTotalLessons
        }

        private fun setProgressPercent(course: Course, progress: Int, context: Context) {
            progressPercentView.text =
                context.getString(R.string.account_progress_percent, progress)

            if (course.isFavorite) {
                progressPercentView.setTextColor(context.getColor(R.color.grey_text_color))
            }
        }

        private fun setCompletedLessons(course: Course, completedLessons: Int?, context: Context) {
            lessonCompletedCountView.text = completedLessons.toString()

            if (course.isFavorite) {
                lessonCompletedCountView.setTextColor(context.getColor(R.color.grey_text_color))
            }
        }

        private fun setTotalLesson(course: Course, totalLessons: Int?, context: Context) {
            lessonCountView.text = totalLessons.toString()

            if (course.isFavorite) {
                lessonCountView.setTextColor(context.getColor(R.color.grey_text_color))
            }
        }

        private fun setProgressBar(course: Course, progress: Int, context: Context) {
            lessonProgressBar.progress = progress

            if (course.isFavorite) {
                lessonProgressBar.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.green_color_button_default
                    )
                )

                val drawable = lessonProgressBar.progressDrawable
                drawable.setColorFilter(
                    ContextCompat.getColor(context, R.color.dark_grey),
                    PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_course_card, parent, false)
        return AccountViewHolder(itemView)
    }

    override fun getItemCount(): Int = courses.size

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.onBind(courses[position], context)
    }

    fun setCourses(courses: MutableList<Course>) {
        this.courses = courses
        notifyDataSetChanged()
    }

    fun addCourse(course: Course) {
        courses.add(course)
        notifyDataSetChanged()
    }

    fun removeCourse(course: Course) {
        courses.remove(course)
        notifyDataSetChanged()
    }

    fun clear() {
        courses.clear()
        notifyDataSetChanged()
    }
}
