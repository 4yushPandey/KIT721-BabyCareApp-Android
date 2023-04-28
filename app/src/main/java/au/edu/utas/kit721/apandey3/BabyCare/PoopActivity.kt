package au.edu.utas.kit721.apandey3.BabyCare

import android.Manifest
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import au.edu.utas.kit721.apandey3.BabyCare.databinding.ActivityPoopBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import android.util.Base64
import androidx.activity.result.contract.ActivityResultContracts
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.math.max
import kotlin.math.min

class PoopActivity : AppCompatActivity() {
    private lateinit var ui: ActivityPoopBinding
    private val REQUEST_IMAGE_CAPTURE = 1
    private var base64String = ""



    //step 5
    private val getPermissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result : Boolean ->
        if (result) {
            // Permission is granted.
            takeAPicture()
        } else {
            Toast.makeText(this, "Cannot access camera, permission denied", Toast.LENGTH_LONG).show()
        }
    }

    //step 6, part 2
    private val getCameraResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { result: Boolean  ->
        //step 7, part 1
        if (result)
        {
            setPic(ui.myImageView)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityPoopBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val currentDate = LocalDate.now()
        val dayOfMonth = currentDate.dayOfMonth
        val month = currentDate.monthValue
        val year = currentDate.year

        val db = Firebase.firestore

        var poopImage = ui.startTime

        ui.btnCamera.setOnClickListener {
            requestToTakeAPicture()
        }

        ui.buttonSave.setOnClickListener {
            val poopData = hashMapOf(
                "poopDate" to "$dayOfMonth-$month-$year",
                "poopTime" to ui.startTime.text.toString(),
                "nappyType" to if (ui.rdWet.isChecked) "Wet" else "Wet + Dirty",
                "poopNote" to ui.notes.text.toString(),
                "poopImage" to base64String
            )

            db.collection("poopHistory")
                .add(poopData)
                .addOnSuccessListener { documentReference ->
                    Log.d(
                        ContentValues.TAG,
                        "DocumentSnapshot added with ID: ${documentReference.id}"
                    )
                    Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                    Toast.makeText(this, "Error saving data!", Toast.LENGTH_SHORT).show()
                }
            finish()
        }
    }
    //step 4
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestToTakeAPicture()
    {
        getPermissionResult.launch(Manifest.permission.CAMERA)
    }

    //step 6, part 1
    private fun takeAPicture() {

        //try {
        val photoFile: File = createImageFile()
        val photoURI: Uri = FileProvider.getUriForFile(
            this,
            "au.edu.utas.kit721.apandey3.BabyCare.fileprovider",
            photoFile
        )
        getCameraResult.launch(photoURI)
        //} catch (e: Exception) {}

    }

    //step 6, part 3
    private lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    //step 7, part 2
    private fun setPic(imageView: ImageView) {
        // Get the dimensions of the View
        val targetW: Int = imageView.measuredWidth
        val targetH: Int = imageView.measuredHeight

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            BitmapFactory.decodeFile(currentPhotoPath, this)

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = max(1, min(photoW / targetW, photoH / targetH))

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
            imageView.setImageBitmap(bitmap)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val imageBytes = byteArrayOutputStream.toByteArray()
            base64String = Base64.encodeToString(imageBytes, Base64.DEFAULT)  // assign value to the class property
        }
    }

    }
