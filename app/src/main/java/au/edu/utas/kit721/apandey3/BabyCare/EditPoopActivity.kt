package au.edu.utas.kit721.apandey3.BabyCare

import android.Manifest
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import au.edu.utas.kit721.apandey3.BabyCare.databinding.ActivityEditPoopBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.min

class EditPoopActivity : AppCompatActivity() {

    private lateinit var ui:ActivityEditPoopBinding
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
        ui = ActivityEditPoopBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val poop = intent.getParcelableExtra<PoopList>("Poop")

        Log.d(TAG, poop.toString())

        if (poop != null) {
            ui.startTime.setText(poop.poopTime.toString())
            ui.notes.setText(poop.poopNote)

            if (poop.nappyType == "Wet") {
                ui.rdWet.isChecked = true
                ui.rdWetDirty.isChecked = false
            } else {
                ui.rdWetDirty.isChecked = true
                ui.rdWet.isChecked = false
            }
            val base64String = poop.poopImage.toString()
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            ui.myImageView.setImageBitmap(bitmap)

            ui.btnCamera.setOnClickListener {
                requestToTakeAPicture()
            }


            val db = Firebase.firestore
            val poopCollection = db.collection("poopHistory")


            ui.btnDelete.setOnClickListener {
                if (poop != null) {
                    poopCollection.document(poop.id!!)
                        .delete()
                        .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                        .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }

                    finish()
                }
            }

            ui.buttonSave.setOnClickListener {
                val updates = hashMapOf(
                    "poopTime" to ui.startTime.text.toString(),
                    "nappyType" to if (ui.rdWet.isChecked) "Wet" else "Wet + Dirty",
                    "poopNote" to ui.notes.text.toString(),
                    "poopImage" to base64String
                )

                if (poop != null) {
                    poopCollection.document(poop.id!!)
                        .set(updates)
                        .addOnSuccessListener {
                            Log.d(FIREBASE_TAG, "Successfully updated feed details ${poop.id}")
                            Log.d(FIREBASE_TAG, base64String.toString())

                            //return to the list
                            finish()
                        }
                }
            }
        }

    }

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