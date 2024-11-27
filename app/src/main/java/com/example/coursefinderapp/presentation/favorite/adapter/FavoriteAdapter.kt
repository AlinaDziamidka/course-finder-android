package com.example.coursefinderapp.presentation.favorite.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coursefinderapp.R
import com.example.coursefinderapp.databinding.CourseCardBinding
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.util.image.CropTopTransformation
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FavoriteAdapter(
    private val context: Context,
    private var courses: MutableList<Course>,
    private val onFavoriteClickListener: (Course) -> Unit,
    private val onDescriptionClickListener: (Course) -> Unit,
) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(
        itemView: View,
        private val onFavoriteClickListener: (Course) -> Unit,
        private val onDescriptionClickListener: (Course) -> Unit,
    ) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = CourseCardBinding.bind(itemView)
        private lateinit var addToFavoriteView: MaterialButton
        private lateinit var ratingView: TextView
        private lateinit var courseDateView: TextView
        private lateinit var courseNameView: TextView
        private lateinit var courseImageView: ImageView
        private lateinit var courseSummaryView: TextView
        private lateinit var priceView: TextView
        private lateinit var moveToDescriptionView: LinearLayout

        init {
            bindViews()
        }

        private fun bindViews() {
            addToFavoriteView = binding.addToFavoriteActionView
            ratingView = binding.ratingView
            courseDateView = binding.courseDateView
            courseNameView = binding.courseNameView
            courseImageView = binding.courseImageView
            courseSummaryView = binding.courseDescriptionView
            priceView = binding.priceView
            moveToDescriptionView = binding.moreActionView
        }

        fun onBind(course: Course, context: Context) {
            courseNameView.text = course.title
            courseSummaryView.text = course.summary
            setCreationDate(course)
            setPrice(course, context)
            setRating(course, context)
            setImage(context, course)
            setOnFavoriteView()
            setOnFavoriteViewAction(course)
            setOnMoveToDescriptionAction(course)
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

        private fun setPrice(
            course: Course,
            context: Context
        ) {
            val price = if (course.price.isNullOrEmpty()) {
                context.getString(R.string.home_course_price_free)
            } else {
                val formattedPrice =
                    course.price.toDoubleOrNull()?.toInt()?.toString() ?: course.price
                context.getString(R.string.home_course_price, formattedPrice)
            }
            priceView.text = price
        }

        private fun setRating(
            course: Course,
            context: Context
        ) {
            val rating = when {
                course.rating == null -> context.getString(R.string.home_no_rating)
                course.rating.compareTo(0.0f) == 0 -> context.getString(R.string.home_no_rating)
                else -> String.format(Locale.US, "%.1f", course.rating)
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

        private fun setOnFavoriteView() {
            addToFavoriteView.setIconResource(R.drawable.ic_favorite_selected)
            addToFavoriteView.setIconTintResource(R.color.green_color_button_default)
        }

        private fun setOnFavoriteViewAction(course: Course) {
            addToFavoriteView.setOnClickListener {
                onFavoriteClickListener(course)
            }
        }

        private fun setOnMoveToDescriptionAction(course: Course) {
            moveToDescriptionView.setOnClickListener {
                onDescriptionClickListener(course)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.course_card, parent, false)
        return FavoriteViewHolder(itemView, onFavoriteClickListener, onDescriptionClickListener)
    }

    override fun getItemCount(): Int = courses.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
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