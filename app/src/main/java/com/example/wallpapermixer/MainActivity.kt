package com.example.wallpapermixer

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.content.res.ResourcesCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var clearButton: Button
    private lateinit var mixButton: Button
    private lateinit var imageButtons: List<ImageButton>
    private lateinit var infoButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.isReady.value
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }
        }
        setContentView(R.layout.activity_main)

        imageView1 = findViewById(R.id.imageView)
        imageView2 = findViewById(R.id.imageView2)
        clearButton = findViewById(R.id.clearButton)
        mixButton = findViewById(R.id.mixButton)
        infoButton = findViewById(R.id.info)

        imageButtons = listOf(
            findViewById(R.id.blue_blossom),
            findViewById(R.id.blue_planets),
            findViewById(R.id.caffe),
            findViewById(R.id.coffee),
            findViewById(R.id.cozy),
            findViewById(R.id.planet),
            findViewById(R.id.red_planet),
            findViewById(R.id.rose),
            findViewById(R.id.sky)
        )

        setOnClickListeners()
        setClearButtonListener()
        setMixButtonListener()
        setInfoButtonListener()
    }

    private fun setOnClickListeners() {
        imageButtons.forEach { imageButton ->
            imageButton.setOnClickListener {
                val clickedImageResource = resources.getIdentifier(
                    imageButton.tag as String,
                    "drawable",
                    packageName
                )
                if (imageView1.tag == clickedImageResource || imageView2.tag == clickedImageResource) {
                    Snackbar.make(findViewById(android.R.id.content), "Image already selected", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                setImageToEmptyImageView(clickedImageResource)
            }
        }
    }

    private fun setImageToEmptyImageView(imageResource: Int) {
        val emptyImageView = if (imageView1.drawable == null) imageView1 else imageView2
        emptyImageView.setImageResource(imageResource)
        emptyImageView.tag = imageResource
    }

    private fun setClearButtonListener() {
        clearButton.setOnClickListener {
            clearImageView(imageView1)
            clearImageView(imageView2)
        }
    }

    private fun setInfoButtonListener() {
        infoButton.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.custom_popup_layout)

            val closeButton: Button = dialog.findViewById(R.id.closeButton)
            closeButton.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun clearImageView(imageView: ImageView) {
        imageView.setImageDrawable(null)
        imageView.tag = null
    }

    private fun setMixButtonListener() {
        mixButton.setOnClickListener {
            val resultingImageResource = getResultingImageResource(
                imageView1.tag as? Int,
                imageView2.tag as? Int
            )
            displayResultingImage(resultingImageResource)
        }
    }

    private fun getResultingImageResource(tag1: Int?, tag2: Int?): Int {
        val combinationsToImages = mapOf(
            Pair(R.drawable.blue_blossom, R.drawable.blue_planets) to R.drawable.blossom_bluplanet,
            Pair(R.drawable.blue_blossom, R.drawable.caffe) to R.drawable.blossom_caffe,
            Pair(R.drawable.blue_blossom, R.drawable.coffee) to R.drawable.blossom_coffee,
            Pair(R.drawable.blue_blossom, R.drawable.cozy) to R.drawable.blossom_cozy,
            Pair(R.drawable.blue_blossom, R.drawable.planet) to R.drawable.planet_blossom,
            Pair(R.drawable.blue_blossom, R.drawable.red_planet) to R.drawable.blossom_redplanet,
            Pair(R.drawable.blue_blossom, R.drawable.rose) to R.drawable.rose_blueblossoms,
            Pair(R.drawable.blue_blossom, R.drawable.sky) to R.drawable.blossom_sky,

            Pair(R.drawable.blue_planets, R.drawable.blue_blossom) to R.drawable.blossom_bluplanet,
            Pair(R.drawable.blue_planets, R.drawable.caffe) to R.drawable.blueplanet_caffe,
            Pair(R.drawable.blue_planets, R.drawable.coffee) to R.drawable.blueplanet_coffee,
            Pair(R.drawable.blue_planets, R.drawable.cozy) to R.drawable.blueplanet_cozy,
            Pair(R.drawable.blue_planets, R.drawable.planet) to R.drawable.planet_blueplanet,
            Pair(R.drawable.blue_planets, R.drawable.red_planet) to R.drawable.blueplanet_redplanet,
            Pair(R.drawable.blue_planets, R.drawable.rose) to R.drawable.blueplanet_rose,
            Pair(R.drawable.blue_planets, R.drawable.sky) to R.drawable.blueplanet_sky,

            Pair(R.drawable.caffe, R.drawable.blue_blossom) to R.drawable.blossom_caffe,
            Pair(R.drawable.caffe, R.drawable.blue_planets) to R.drawable.blueplanet_caffe,
            Pair(R.drawable.caffe, R.drawable.coffee) to R.drawable.caffe_coffee,
            Pair(R.drawable.caffe, R.drawable.cozy) to R.drawable.caffe_cozy,
            Pair(R.drawable.caffe, R.drawable.planet) to R.drawable.planet_caffe,
            Pair(R.drawable.caffe, R.drawable.red_planet) to R.drawable.caffe_redplanet,
            Pair(R.drawable.caffe, R.drawable.rose) to R.drawable.rose_caffe,
            Pair(R.drawable.caffe, R.drawable.sky) to R.drawable.caffe_sky,

            Pair(R.drawable.coffee, R.drawable.blue_blossom) to R.drawable.blossom_coffee,
            Pair(R.drawable.coffee, R.drawable.blue_planets) to R.drawable.blueplanet_coffee,
            Pair(R.drawable.coffee, R.drawable.caffe) to R.drawable.caffe_coffee,
            Pair(R.drawable.coffee, R.drawable.cozy) to R.drawable.coffee_cozy,
            Pair(R.drawable.coffee, R.drawable.planet) to R.drawable.planet_coffee,
            Pair(R.drawable.coffee, R.drawable.red_planet) to R.drawable.coffee_redplanet,
            Pair(R.drawable.coffee, R.drawable.rose) to R.drawable.rose_coffee,
            Pair(R.drawable.coffee, R.drawable.sky) to R.drawable.coffee_sky,

            Pair(R.drawable.cozy, R.drawable.blue_blossom) to R.drawable.blossom_cozy,
            Pair(R.drawable.cozy, R.drawable.blue_planets) to R.drawable.blueplanet_cozy,
            Pair(R.drawable.cozy, R.drawable.caffe) to R.drawable.caffe_cozy,
            Pair(R.drawable.cozy, R.drawable.coffee) to R.drawable.coffee_cozy,
            Pair(R.drawable.cozy, R.drawable.planet) to R.drawable.planet_cozy,
            Pair(R.drawable.cozy, R.drawable.red_planet) to R.drawable.cozy_redplanet,
            Pair(R.drawable.cozy, R.drawable.rose) to R.drawable.rose_cozy,
            Pair(R.drawable.cozy, R.drawable.sky) to R.drawable.cozy_sky,

            Pair(R.drawable.planet, R.drawable.blue_blossom) to R.drawable.planet_blossom,
            Pair(R.drawable.planet, R.drawable.blue_planets) to R.drawable.planet_blueplanet,
            Pair(R.drawable.planet, R.drawable.caffe) to R.drawable.planet_caffe,
            Pair(R.drawable.planet, R.drawable.coffee) to R.drawable.planet_coffee,
            Pair(R.drawable.planet, R.drawable.cozy) to R.drawable.planet_cozy,
            Pair(R.drawable.planet, R.drawable.red_planet) to R.drawable.planet_redplanet,
            Pair(R.drawable.planet, R.drawable.rose) to R.drawable.planet_rose,
            Pair(R.drawable.planet, R.drawable.sky) to R.drawable.planet_sky,

            Pair(R.drawable.red_planet, R.drawable.blue_blossom) to R.drawable.blossom_redplanet,
            Pair(R.drawable.red_planet, R.drawable.blue_planets) to R.drawable.blueplanet_redplanet,
            Pair(R.drawable.red_planet, R.drawable.caffe) to R.drawable.caffe_redplanet,
            Pair(R.drawable.red_planet, R.drawable.coffee) to R.drawable.coffee_redplanet,
            Pair(R.drawable.red_planet, R.drawable.cozy) to R.drawable.cozy_redplanet,
            Pair(R.drawable.red_planet, R.drawable.planet) to R.drawable.planet_redplanet,
            Pair(R.drawable.red_planet, R.drawable.rose) to R.drawable.rose_redplanet,
            Pair(R.drawable.red_planet, R.drawable.sky) to R.drawable.sky_redplanet,

            Pair(R.drawable.rose, R.drawable.blue_blossom) to R.drawable.rose_blueblossoms,
            Pair(R.drawable.rose, R.drawable.blue_planets) to R.drawable.blueplanet_rose,
            Pair(R.drawable.rose, R.drawable.caffe) to R.drawable.rose_caffe,
            Pair(R.drawable.rose, R.drawable.coffee) to R.drawable.rose_coffee,
            Pair(R.drawable.rose, R.drawable.cozy) to R.drawable.rose_cozy,
            Pair(R.drawable.rose, R.drawable.planet) to R.drawable.rose_planet,
            Pair(R.drawable.rose, R.drawable.red_planet) to R.drawable.rose_redplanet,
            Pair(R.drawable.rose, R.drawable.sky) to R.drawable.rose_sky,

            Pair(R.drawable.sky, R.drawable.blue_blossom) to R.drawable.blossom_sky,
            Pair(R.drawable.sky, R.drawable.blue_planets) to R.drawable.blueplanet_sky,
            Pair(R.drawable.sky, R.drawable.caffe) to R.drawable.caffe_sky,
            Pair(R.drawable.sky, R.drawable.coffee) to R.drawable.coffee_sky,
            Pair(R.drawable.sky, R.drawable.cozy) to R.drawable.cozy_sky,
            Pair(R.drawable.sky, R.drawable.planet) to R.drawable.planet_sky,
            Pair(R.drawable.sky, R.drawable.red_planet) to R.drawable.sky_redplanet,
            Pair(R.drawable.sky, R.drawable.rose) to R.drawable.rose_sky
        )
        return combinationsToImages.getOrElse(Pair(tag1, tag2) as Pair<Int, Int>) {
            Snackbar.make(findViewById(android.R.id.content), "Invalid image combination", Snackbar.LENGTH_SHORT).show()
            R.drawable.cozy
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayResultingImage(resultingImageResource: Int) {
        if (imageView1.tag == null && imageView2.tag == null) {
            Snackbar.make(findViewById(android.R.id.content), "Please select two images to mix.", Snackbar.LENGTH_SHORT).show()
            return
        }
        if (imageView1.tag == null || imageView2.tag == null) {
            Snackbar.make(findViewById(android.R.id.content), "Please select two images to mix.", Snackbar.LENGTH_SHORT).show()
            return
        }
        val resultingImageDrawable =
            ResourcesCompat.getDrawable(resources, resultingImageResource, null)
        val popupView = LayoutInflater.from(this).inflate(R.layout.result_popup_layout, null)
        val popupImageView = popupView.findViewById<ImageView>(R.id.resultImageView)
        popupImageView.setImageDrawable(resultingImageDrawable)
        val popupTextView = popupView.findViewById<TextView>(R.id.resultTextView)
        popupTextView.text = "Mixed Image"
        val popup = Dialog(this)
        popup.setContentView(popupView)
        popup.window?.setBackgroundDrawableResource(android.R.color.white)
        popup.show()
        popupImageView.setOnClickListener { popup.dismiss() }
    }
}
