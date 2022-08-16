package com.example.weatherapp.Models

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**

created by
 * Studnet ID: 991563114
 * on ${2020/3/3} */

class MarginItemDecoration(private val spaceSize: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect){
            if(parent.getChildAdapterPosition(view) == 0){
                top = spaceSize
            }
            left = spaceSize - 10
            right = spaceSize - 10
            bottom = spaceSize - 10
        }
    }
}