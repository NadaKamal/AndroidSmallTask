package com.example.data.extentions

import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Orientation
import androidx.recyclerview.widget.RecyclerView.VERTICAL


fun View.visible() {
    visibility = VISIBLE
}


fun View.invisible() {
    visibility = INVISIBLE
}


fun View.gone() {
    visibility = GONE
}


fun ViewGroup.inflate(@LayoutRes layout: Int): View =
    LayoutInflater.from(context).inflate(layout, this, false)

fun RecyclerView.linearLayoutManager(@Orientation orientation: Int = VERTICAL) {
    layoutManager = LinearLayoutManager(this.context, orientation, false)
}

/**
 * Set GridLayoutManager to RecyclerView
 * @param spanCount items count in one row
 */
//fun RecyclerView.gridLayoutManager(spanCount: Int, spacing: Int, includeEdge: Boolean) {
//  layoutManager = GridLayoutManager(this.context, spanCount)
//  addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))
//}
//endregion

//region Snackbar
/**
 * Show short snackbar
 * @param message message to be shown
 */
//fun View.snackbar(message: String?=null) {
//  if (message != null) {
//    Snackbar.make(this, message, BaseTransientBottomBar.LENGTH_SHORT)
//      .setBackgroundTint(context.getColorCompat(R.color.mainColor))
//      .setTextColor(context.getColorCompat(R.color.white))
//      .show()
//  }
//}

/**
 * Show short snackbar with dismiss listener
 * @param message Message to be shown
 * @param onDismissed On dismiss action
 */
//fun View.snackbar(message: String, onDismissed: () -> Unit) {
//  Snackbar.make(this, message, BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
//    .setBackgroundTint(context.getColorCompat(R.color.mainColor))
//    .setTextColor(context.getColorCompat(R.color.white))
//    .addCallback(object : Snackbar.Callback() {
//      override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
//        super.onDismissed(transientBottomBar, event)
//        onDismissed.invoke()
//      }
//    }).show()
//}

/**
 * Show short snackbar with dismiss listener
 * @param message Message to be shown
 * @param onDismissed On dismiss action
 */
//fun View.snackbar(message: String, action: String, onDismissed: () -> Unit) {
//  Snackbar.make(this, message, BaseTransientBottomBar.LENGTH_INDEFINITE)
//    .setBackgroundTint(context.getColorCompat(R.color.mainColor))
//    .setTextColor(context.getColorCompat(R.color.white))    .setAction(action) {
//      onDismissed.invoke()
//    }.show()
//}

/**
 * Show long snackbar
 * @param message message to be shown
 */
//fun View.longSnackbar(message: String) {
//  Snackbar.make(this, message, Snackbar.LENGTH_LONG)
//    .setBackgroundTint(context.getColorCompat(R.color.mainColor)).show()
//}
//endregion

//region Spinner
fun Spinner.onItemSelected(pos: (Int) -> Unit) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            pos(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
}
//endregion