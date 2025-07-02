package com.project.animalface_app

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import android.webkit.URLUtil

class AnimalFaceResultActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PREDICTED_CLASS_LABEL = "predictedClassLabel"
        const val EXTRA_CONFIDENCE = "confidence"
        const val EXTRA_IMAGE_URL = "imageUrl"
    }

    private lateinit var resultImageView: ImageView
    private lateinit var resultTextView: TextView
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_face_result)

        resultImageView = findViewById(R.id.cameraImg)
        resultTextView = findViewById(R.id.result_text)

        val predictedClassLabel = intent.getStringExtra(EXTRA_PREDICTED_CLASS_LABEL) ?: "알 수 없음"
        val confidence = intent.getDoubleExtra(EXTRA_CONFIDENCE, 0.0)
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)

        if (predictedClassLabel == "알 수 없음") {
            resultTextView.text = "결과를 가져올 수 없습니다."
        } else {
            resultTextView.text = "결과: $predictedClassLabel\n정확도: ${formatToPercentage(confidence)}"
        }

        if (imageUrl != null && URLUtil.isValidUrl(imageUrl)) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.user_profile_)
                .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL) // 캐시 사용
                .into(resultImageView)
        } else {
            resultImageView.setImageResource(getImageResource(predictedClassLabel))
        }
    }

    private fun getImageResource(predictedClassLabel: String?): Int {
        return when (predictedClassLabel) {
            "강아지" -> R.drawable.dog_result
            "고양이" -> R.drawable.cat_result
            "공룡" -> R.drawable.dinos_result
            "꼬북이" -> R.drawable.turtle_result
            "티벳여우" -> R.drawable.fox_result
            else -> R.drawable.user_profile_
        }
    }

    private fun formatToPercentage(value: Double): String {
        return String.format("%.2f%%", value * 100)
    }
}
