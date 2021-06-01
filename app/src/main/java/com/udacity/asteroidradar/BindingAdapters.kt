package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidRecyclerAdapter

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription = context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription = context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("dataList")
fun bindRecyclerViewToSubmitList(recyclerView: RecyclerView, asteroidList: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidRecyclerAdapter
    adapter.submitList(asteroidList)
}

@BindingAdapter("picOfTheDay")
fun bindPImageViewToPicOfTheDay(imageView: ImageView, pic: PictureOfDay?) {
    val context = imageView.context
    if (pic != null) {
        Picasso.with(imageView.context).load(pic.url)
            .placeholder(R.drawable.placeholder_picture_of_day)
            .error(R.drawable.placeholder_picture_of_day).into(imageView)
        imageView.contentDescription = context
                .getString(R.string.nasa_picture_of_day_content_description_format)
                .format(pic.title)
    }
    else {
        imageView.setImageResource(R.drawable.placeholder_picture_of_day)
        imageView.contentDescription = context.getString(R.string.nasa_picture_of_day_content_description_format)
    }
}