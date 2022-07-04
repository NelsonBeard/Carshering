package com.carshering.ui

import android.view.View
import android.widget.ImageView
import coil.load
import com.facebook.shimmer.ShimmerFrameLayout

fun setCarPicture(
    carPicImageView: ImageView,
    shimmerImageView: ImageView,
    shimmerFrameLayout: ShimmerFrameLayout,
    pictureUrl: String
) {
    carPicImageView.load(pictureUrl) {
        target(
            onStart = {
                shimmerFrameLayout.startShimmerAnimation()
                shimmerImageView.visibility = View.VISIBLE
                carPicImageView.visibility = View.GONE
            },
            onSuccess = {
                shimmerFrameLayout.stopShimmerAnimation()
                shimmerImageView.visibility = View.GONE
                carPicImageView.visibility = View.VISIBLE
                carPicImageView.setImageDrawable(it)
            },
            onError = {
                shimmerFrameLayout.stopShimmerAnimation()
                shimmerImageView.visibility = View.GONE
                carPicImageView.visibility = View.VISIBLE
            }
        )
    }
}
