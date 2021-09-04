package com.example.android.politicalpreparedness.representative.adapter

import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.utils.Constants.NETWORK_TAG
import kotlinx.android.synthetic.main.single_representative_element.view.*

@BindingAdapter("profileImage")
fun fetchImage(view: ImageView, src: String?) {
    src?.let {
        Log.e(NETWORK_TAG, "loading image")
        val uri = src.toUri().buildUpon().scheme("https").build()

        Glide.with(view.context)
            .load(uri)
            .placeholder(R.drawable.ic_profile)
            .centerCrop()
            .circleCrop()
            .into(view);
    }
}

@BindingAdapter("stateValue")
fun Spinner.setNewValue(value: String?) {
    val adapter = toTypedAdapter<String>(this.adapter as ArrayAdapter<*>)
    val position = when (adapter.getItem(0)) {
        is String -> adapter.getPosition(value)
        else -> this.selectedItemPosition
    }
    if (position >= 0) {
        setSelection(position)
    }
}

inline fun <reified T> toTypedAdapter(adapter: ArrayAdapter<*>): ArrayAdapter<T>{
    return adapter as ArrayAdapter<T>
}
