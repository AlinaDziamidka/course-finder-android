package com.example.coursefinderapp.presentation.coursedetails

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.coursefinderapp.R
import com.example.coursefinderapp.databinding.FragmentCourseDetailsBinding
import com.example.coursefinderapp.databinding.FragmentHomeBinding
import com.example.coursefinderapp.domain.entity.Author
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.presentation.home.HomeViewState
import com.example.coursefinderapp.util.image.CropTopTransformation
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class CourseDetailsView : Fragment(R.layout.fragment_course_details) {

    private val viewModel: CourseDetailsViewModel by viewModels()
    private var _binding: FragmentCourseDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: CourseDetailsViewArgs by navArgs()
    private lateinit var courseImage: ImageView
    private lateinit var addToFavoriteAction: FloatingActionButton
    private lateinit var actionBack: FloatingActionButton
    private lateinit var ratingView: TextView
    private lateinit var courseDateView: TextView
    private lateinit var courseNameView: TextView
    private lateinit var authorImageView: ImageView
    private lateinit var authorNameView: TextView
    private lateinit var moveToPlatformAction: MaterialButton
    private lateinit var courseDescriptionView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentCourseDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setFABSize()
        setCourse()
        observeCourse()
        onPressedBackAction()
    }

    private fun initViews() {
        courseImage = binding.courseImageView
        addToFavoriteAction = binding.addToFavoriteActionView
        actionBack = binding.backActionView
        ratingView = binding.ratingView
        courseDateView = binding.courseDateView
        courseNameView = binding.courseNameView
        authorImageView = binding.authorImageView
        authorNameView = binding.authorNameView
        moveToPlatformAction = binding.moveToPlatformActionView
        courseDescriptionView = binding.aboutCourseContent
    }

    private fun setFABSize() {
        val statusBarHeight = activity?.getStatusBarHeight() ?: 0

        addToFavoriteAction.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin += statusBarHeight
        }

        actionBack.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin += statusBarHeight
        }
    }

    private fun Activity.getStatusBarHeight(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val insets = this.window.decorView.rootWindowInsets
            insets?.getInsets(WindowInsets.Type.systemBars())?.top ?: 0
        } else {
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
        }
    }

    private fun setCourse() {
        val courseId = args.courseId
        val dataSource = args.dataSource
        viewModel.setUpCourse(courseId, dataSource)
    }

    private fun observeCourse() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    when (it) {
                        is CourseDetailsViewState.Success -> {
                            Log.d(OBSERVE, "Success to fetch courses: ${it.data}")
                            handleOnSuccess(it.data)
                        }

                        is CourseDetailsViewState.Loading -> {
                            Log.d(OBSERVE, "Loading fetching courses")
                        }

                        is CourseDetailsViewState.Failure -> {
                            Log.d(OBSERVE, "Failed to fetch courses: ${it.message}")
                        }
                    }
                }
            }
        }
    }

    private fun handleOnSuccess(course: Course) {
        setUpAuthor(course)
        setCourseName(course.title)
        setCourseDescription(course.description)
        setRating(course.rating)
        setCreationDate(course.createDate)
        setCourseImage(course.coverImage)
        setMoveToPlatformAction(course.courseUrl)
        setAddToFavoriteAction(course)
    }

    private fun setUpAuthor(course: Course) {
        viewModel.setUpAuthor(course, args.dataSource)
        observeAuthor()
    }

    private fun observeAuthor() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authorViewState.collect {
                    when (it) {
                        is CourseDetailsViewState.Success -> {
                            Log.d(OBSERVE, "Success to fetch author: ${it.data}")
                            addAuthor(it.data)
                        }

                        is CourseDetailsViewState.Loading -> {
                            Log.d(OBSERVE, "Loading fetching author")
                        }

                        is CourseDetailsViewState.Failure -> {
                            Log.d(OBSERVE, "Failed to fetch author: ${it.message}")
                        }
                    }
                }
            }
        }
    }

    private fun addAuthor(author: Author) {
        setAuthorName(author.authorName)
        if (author.avatar != null) {
            setAuthorAvatar(author.avatar)
        }
    }

    private fun setAuthorName(authorName: String?) {
        authorNameView.text = authorName
    }

    private fun setAuthorAvatar(avatar: String) {

        if (avatar.endsWith(".svg", ignoreCase = true)) {
            GlideToVectorYou
                .init()
                .with(requireContext())
                .setPlaceHolder(
                    R.drawable.author_image_placeholder,
                    R.drawable.author_image_placeholder
                )
                .load(Uri.parse(avatar), authorImageView)
        } else {
            Glide.with(requireContext())
                .load(avatar)
                .placeholder(R.drawable.author_image_placeholder)
                .error(R.drawable.author_image_placeholder)
                .into(authorImageView)
        }
    }

    private fun setCourseName(title: String) {
        courseNameView.text = title
    }

    private fun setCourseDescription(description: String?) {
        courseDescriptionView.text = if (description != null) {
            HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            ""
        }
    }

    private fun setRating(rating: Float?) {
        ratingView.text = if (rating?.compareTo(0.0f) == 0) {
            requireContext().getString(R.string.home_no_rating)
        } else {
            String.format(Locale.US, "%.1f", rating)
        }
    }

    private fun setCreationDate(createDate: String?) {
        if (createDate.isNullOrEmpty()) {
            courseDateView.visibility = View.GONE
        } else {
            courseDateView.visibility = View.VISIBLE
            val formattedDate = formatDate(createDate)
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

    private fun setCourseImage(coverImage: String?) {
        Glide.with(requireContext())
            .load(coverImage)
            .placeholder(R.drawable.course_full_image_placeholder)
            .error(R.drawable.error_full_image_placeholder)
            .transform(CropTopTransformation())
            .into(courseImage)
    }

    private fun setMoveToPlatformAction(courseUrl: String?) {
        moveToPlatformAction.setOnClickListener {
            courseUrl?.takeIf { it.isNotEmpty() }?.let {
                openUrlInBrowser(it)
            }
        }
    }

    private fun openUrlInBrowser(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun setAddToFavoriteAction(course: Course) {
        if (args.dataSource == "DATABASE") {
            setFavoriteButtonColor()
        } else {
            addToFavoriteAction.setOnClickListener {
                addCourseToFavorite(course)
                setFavoriteButtonColor()
            }
        }
    }

    private fun addCourseToFavorite(course: Course) {
        viewModel.saveCourseToFavorite(course)
    }

    private fun setFavoriteButtonColor() {
        val color = ContextCompat.getColor(requireContext(), R.color.green_color_button_default)
        addToFavoriteAction.setImageResource(R.drawable.ic_favorite_big_selected)
        addToFavoriteAction.imageTintList = ColorStateList.valueOf(color)
    }

    private fun onPressedBackAction() {
        actionBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    override fun onPause() {
        super.onPause()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "FavoriteView"
        private const val OBSERVE = "coursesObserver"
    }
}
