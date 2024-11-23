package com.example.coursefinderapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.coursefinderapp.R

class CourseLoadStateAdapter (
    private val retry: () -> Unit
    ) : LoadStateAdapter<CourseLoadStateAdapter.LoadStateViewHolder>() {

        override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
            holder.bind(loadState)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            loadState: LoadState
        ): LoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.load_state_footer, parent, false)
            return LoadStateViewHolder(view, retry)
        }

        class LoadStateViewHolder(
            itemView: View,
            private val retry: () -> Unit
        ) : RecyclerView.ViewHolder(itemView) {
            private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
            private val retryButton: Button = itemView.findViewById(R.id.retry_button)
            private val errorMessage: TextView = itemView.findViewById(R.id.error_message)

            fun bind(loadState: LoadState) {
                if (loadState is LoadState.Error) {
                    errorMessage.text = loadState.error.localizedMessage
                }
                progressBar.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState is LoadState.Error
                errorMessage.isVisible = loadState is LoadState.Error

                retryButton.setOnClickListener {
                    retry.invoke()
                }
            }
        }
    }