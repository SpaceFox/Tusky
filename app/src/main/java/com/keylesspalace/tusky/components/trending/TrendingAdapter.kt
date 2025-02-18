/* Copyright 2021 Tusky Contributors
 *
 * This file is a part of Tusky.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * Tusky is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Tusky; if not,
 * see <http://www.gnu.org/licenses>. */

package com.keylesspalace.tusky.components.trending

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keylesspalace.tusky.databinding.ItemTrendingCellBinding
import com.keylesspalace.tusky.databinding.ItemTrendingDateBinding
import com.keylesspalace.tusky.viewdata.TrendingViewData

class TrendingAdapter(
    private val onViewTag: (String) -> Unit
) : ListAdapter<TrendingViewData, RecyclerView.ViewHolder>(TrendingDifferCallback) {

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_TAG -> {
                val binding =
                    ItemTrendingCellBinding.inflate(LayoutInflater.from(viewGroup.context))
                TrendingTagViewHolder(binding)
            }
            else -> {
                val binding =
                    ItemTrendingDateBinding.inflate(LayoutInflater.from(viewGroup.context))
                TrendingDateViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (val viewData = getItem(position)) {
            is TrendingViewData.Tag -> {
                val holder = viewHolder as TrendingTagViewHolder
                holder.setup(viewData, onViewTag)
            }

            is TrendingViewData.Header -> {
                val holder = viewHolder as TrendingDateViewHolder
                holder.setup(viewData.start, viewData.end)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is TrendingViewData.Tag) {
            VIEW_TYPE_TAG
        } else {
            VIEW_TYPE_HEADER
        }
    }

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_TAG = 1

        val TrendingDifferCallback = object : DiffUtil.ItemCallback<TrendingViewData>() {
            override fun areItemsTheSame(
                oldItem: TrendingViewData,
                newItem: TrendingViewData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: TrendingViewData,
                newItem: TrendingViewData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
