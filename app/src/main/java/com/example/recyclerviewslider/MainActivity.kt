package com.example.recyclerviewslider

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private var currentItemPosition = 0
    private var totalItems = 0

    private lateinit var prevBtn: ImageButton
    private lateinit var nextBtn: ImageButton
    private lateinit var sliderDotsLayout: LinearLayout
    private lateinit var imageSliderRv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val slidesArray = arrayListOf(
            R.drawable.image_1,
            R.drawable.image_2,
            R.drawable.image_3,
            R.drawable.image_4
        )

        val imageSliderAdapter = ImageSliderAdapter(slidesArray)

        imageSliderRv = findViewById(R.id.image_slider_rv)
        imageSliderRv.adapter = imageSliderAdapter

        imageSliderRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                // check if is still scrolling
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    return
                }

                val horizontalLayoutManager = imageSliderRv.layoutManager as LinearLayoutManager

                currentItemPosition = horizontalLayoutManager.findFirstVisibleItemPosition()
                setCurrentSliderDot()
                setSliderButtons()
            }
        })

        totalItems = slidesArray.size
        prevBtn = findViewById(R.id.prev_slide_ib)
        nextBtn = findViewById(R.id.next_slide_ib)

        prevBtn.setOnClickListener {
            if (currentItemPosition > 0) {
                goToPreviousSlide()
                setCurrentSliderDot()
                setSliderButtons()
            }
        }

        nextBtn.setOnClickListener {
            if ((currentItemPosition + 1) < totalItems) {
                goToNextSlide()
                setCurrentSliderDot()
                setSliderButtons()
            }
        }

        sliderDotsLayout = findViewById(R.id.slider_dots_ll)
        setupSliderDots(slidesArray)

        disablePrevBtn()
    }

    private fun goToPreviousSlide() {
        imageSliderRv.smoothScrollToPosition(--currentItemPosition)
    }

    private fun goToNextSlide() {
        imageSliderRv.smoothScrollToPosition(++currentItemPosition)
    }

    private fun setupSliderDots(slidesArray: ArrayList<Int>) {
        for (i in slidesArray.indices) {
            val layoutInflater = LayoutInflater.from(this@MainActivity)
            val dotCardView: CardView =
                layoutInflater.inflate(R.layout.dot_item, sliderDotsLayout, false) as CardView

            if (i == 0) {
                dotCardView.alpha = 1.0f
            }

            sliderDotsLayout.addView(dotCardView)
        }
    }

    private fun setCurrentSliderDot() {
        sliderDotsLayout.children.forEachIndexed { index, _ ->
            if (index == currentItemPosition) {
                sliderDotsLayout.getChildAt(currentItemPosition).alpha = 1.0f
            } else {
                sliderDotsLayout.getChildAt(index).alpha = 0.5f
            }
        }
    }

    private fun setSliderButtons() {
        if (currentItemPosition == 0) {
            disablePrevBtn()
            enableNextBtn()
        } else if (currentItemPosition + 1 == totalItems) {
            disableNextBtn()
            enablePrevBtn()
        } else {
            enablePrevBtn()
            enableNextBtn()
        }
    }

    private fun enablePrevBtn() {
        prevBtn.isEnabled = true
        prevBtn.background.alpha = 255
    }

    private fun disablePrevBtn() {
        prevBtn.isEnabled = false
        prevBtn.background.alpha = 128
    }

    private fun enableNextBtn() {
        nextBtn.isEnabled = true
        nextBtn.background.alpha = 255
    }

    private fun disableNextBtn() {
        nextBtn.isEnabled = false
        nextBtn.background.alpha = 128
    }
}