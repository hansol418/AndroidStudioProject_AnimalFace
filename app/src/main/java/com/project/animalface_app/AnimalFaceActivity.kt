package com.project.animalface_app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.project.animalface_app.databinding.ActivityAnimalFaceBinding
import com.project.animalface_app.network.INetworkService
import com.project.animalface_app.network.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class AnimalFaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnimalFaceBinding
    private lateinit var filePath: String
    private lateinit var imageUri: Uri
    private lateinit var imageView: ImageView
    private lateinit var apiService: INetworkService

    private val CAMERA_REQUEST_CODE = 100
    private val STORAGE_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimalFaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageView = binding.resultUserImage
        apiService = MyApplication.getApiService()



        checkPermissions()
        val requestGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val dataUri = result.data?.data
            if (dataUri != null) {
                imageUri = dataUri
                imageView.setImageURI(dataUri)
                Log.d("AnimalFaceActivity", "갤러리에서 선택된 이미지 URI: $imageUri")
            } else {
                Log.e("AnimalFaceActivity", "갤러리에서 이미지를 선택하지 않았습니다.")
            }
        }

        val requestCameraFileLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                if (filePath.isNotEmpty()) {
                    val bitmap = BitmapFactory.decodeFile(filePath)
                    imageView.setImageBitmap(bitmap)

                    Log.d("AnimalFaceActivity", "카메라로 촬영된 이미지 URI: $imageUri")
                } else {
                    Toast.makeText(this, "이미지 저장 경로가 비어있습니다.", Toast.LENGTH_SHORT).show()
                    Log.e("AnimalFaceActivity", "이미지 저장 경로 없음")
                }
            } else {
                Toast.makeText(this, "사진을 촬영하지 않았습니다.", Toast.LENGTH_SHORT).show()
                Log.e("AnimalFaceActivity", "카메라로 사진 촬영 실패")
            }
        }

        binding.galleryBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            requestGalleryLauncher.launch(intent)
            Log.d("AnimalFaceActivity", "갤러리 버튼 클릭됨")
        }

        binding.cameraBtn.setOnClickListener {
            if (checkPermissions()) {
                Log.d("AnimalFaceActivity", "카메라 권한 확인 완료")

                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

                if (storageDir != null) {
                    Log.d("AnimalFaceActivity", "저장 디렉터리 경로: ${storageDir.absolutePath}")
                } else {
                    Log.e("AnimalFaceActivity", "저장 디렉터리를 찾을 수 없습니다.")
                }

                try {
                    val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
                    filePath = file.absolutePath
                    Log.d("AnimalFaceActivity", "파일 생성 성공: $filePath")

                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "${applicationContext.packageName}.fileprovider",
                        file
                    )

                    imageUri = photoURI
                    Log.d("AnimalFaceActivity", "생성된 이미지 URI: $imageUri")

                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    Log.d("AnimalFaceActivity", "카메라 Intent 생성 및 실행 준비 완료")

                    try {
                        requestCameraFileLauncher.launch(intent)
                        Log.d("AnimalFaceActivity", "카메라 호출 성공: $imageUri")
                    } catch (e: Exception) {
                        Log.e("AnimalFaceActivity", "카메라 호출 실패: ${e.message}")
                    }
                } catch (e: Exception) {
                    Log.e("AnimalFaceActivity", "파일 생성 중 오류 발생: ${e.message}")
                }
            } else {
                Log.e("AnimalFaceActivity", "카메라 권한이 없습니다.")
            }
        }



        binding.predictSendBtn.setOnClickListener {
            if (::imageUri.isInitialized) {
                Log.d("AnimalFaceActivity", "테스트 실행 버튼 클릭됨, 이미지 URI: $imageUri")
                processImage(imageUri)
            } else {
                Toast.makeText(this, "이미지를 선택하세요.", Toast.LENGTH_SHORT).show()
                Log.e("AnimalFaceActivity", "이미지 URI가 초기화되지 않았습니다.")
            }
        }
    }

    private fun processImage(uri: Uri) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val bitmap = getResizedBitmap(uri, 200, 200)
                val imageBytes = bitmapToByteArray(bitmap)
                val profileImagePart = createMultipartBodyFromBytes(imageBytes)

                uploadData(profileImagePart)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("AnimalFaceActivity", "이미지 처리 중 오류 발생: ${e.message}")
            }
        }
    }

    private suspend fun getResizedBitmap(uri: Uri, width: Int, height: Int): Bitmap {
        return withContext(Dispatchers.IO) {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            Bitmap.createScaledBitmap(bitmap, width, height, false)
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    private fun createMultipartBodyFromBytes(imageBytes: ByteArray): MultipartBody.Part {
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageBytes)
        return MultipartBody.Part.createFormData("image", "image.jpg", requestFile)
    }

    private fun uploadData(profileImage: MultipartBody.Part) {
        val call = apiService.predictImage(profileImage)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()

                    try {
                        val jsonObject = JSONObject(responseBody)
                        val predictedClassLabel = jsonObject.getString("predictedClassLabel")
                        val confidence = jsonObject.getDouble("confidence")

                        Log.d("AnimalFaceActivity", "Response Body: $responseBody")
                        Log.d("AnimalFaceActivity", "서버 응답: $predictedClassLabel, 정확도: $confidence")

                        val intent = Intent(this@AnimalFaceActivity, AnimalFaceResultActivity::class.java)
                        intent.putExtra("predictedClassLabel", predictedClassLabel)
                        intent.putExtra("confidence", confidence)
                        startActivity(intent)

                    } catch (e: Exception) {
                        Log.e("AnimalFaceActivity", "JSON 파싱 오류: ${e.message}")
                    }
                } else {
                    Log.e("AnimalFaceActivity", "서버 응답 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@AnimalFaceActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("AnimalFaceActivity", "네트워크 오류: ${t.message}")
            }
        })
    }

    private fun checkPermissions(): Boolean {
        var allPermissionsGranted = true

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
            allPermissionsGranted = false
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE)
                allPermissionsGranted = false
            }
        }

        return allPermissionsGranted
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("AnimalFaceActivity", "카메라 권한이 부여되었습니다.")
                } else {
                    Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                    Log.e("AnimalFaceActivity", "카메라 권한이 거부되었습니다.")
                }
            }
            STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("AnimalFaceActivity", "저장소 권한이 부여되었습니다.")
                } else {
                    Toast.makeText(this, "저장소 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                    Log.e("AnimalFaceActivity", "저장소 권한이 거부되었습니다.")
                }
            }
        }
    }

}
