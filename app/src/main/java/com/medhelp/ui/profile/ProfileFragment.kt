package com.medhelp.ui.profile

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.medhelp.R
import com.medhelp.ui.home.HomeFragment
import com.medhelp.ui.localDb.roomDb.DatabaseClient
import com.medhelp.ui.localDb.roomDb.bean.UserProfile
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.select_camera_photo.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {
    private var imageUri: Uri? = null
    val REQUEST_CODE = 200
    var filePath:String?=null
    private var currentPhotoPath : String= ""
  lateinit  var userPreviousData: List<UserProfile?>
    private val pickImage = 100
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_profile, container, false)
         userPreviousData = DatabaseClient.getInstance(context).appDatabase.userDao()?.getAll()!!
        if (userPreviousData.isNotEmpty()){
            root.etFirstName.setText(userPreviousData[0]?.firstName)
            root.etLastName.setText(userPreviousData[0]?.lastName)
            if (userPreviousData[0]?.profileImage!=null){
                root.image.setImageURI(Uri.parse(File(userPreviousData[0]?.profileImage).toString()))
                filePath=userPreviousData[0]?.profileImage
            }
        }
        root.rlTakePhoto.setOnClickListener {
            showPictureDialog()
        }
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btSave.setOnClickListener {
            when {
                TextUtils.isEmpty(etFirstName.text.toString()) -> {
                    etFirstName.setError(getString(R.string.please_enter_first_name))
                    etFirstName.requestFocus()
                }
                TextUtils.isEmpty(etLastName.text.toString()) -> {
                    etLastName.setError(getString(R.string.please_enter_last_name))
                    etLastName.requestFocus()
                }
                else -> {
                    saveDetails()
                }
            }
        }
    }

    private fun showPictureDialog() {
        val picturePickerDialog = context?.let { Dialog(it) }
        picturePickerDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        activity?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        picturePickerDialog?.setCancelable(true)
        picturePickerDialog?.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(android.R.color.transparent)))
        picturePickerDialog?.setContentView(R.layout.select_camera_photo)
        picturePickerDialog.show()
        picturePickerDialog.choose_camera.setOnClickListener {

            checkPermissionForCamera()
            picturePickerDialog.hide()
        }
        picturePickerDialog.choose_gallery.setOnClickListener {
            checkPermissionForImage()
            picturePickerDialog.hide()
        }
    }

    private fun checkPermissionForImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((activity?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    && (activity?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ) {
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                val permissionCoarse = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                requestPermissions(permission, HomeFragment.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001
                requestPermissions(permissionCoarse, HomeFragment.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_WRITE LIKE 1002
            } else {
                pickImageFromGallery()
            }
        }
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
        const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2
        const val MY_PERMISSIONS_REQUEST_CAMERA = 3
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, pickImage) // GIVE AN INTEGER VALUE FOR IMAGE_PICK_CODE LIKE 1000
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
filePath= context?.let { imageUri?.let { it1 -> getPathFromInputStreamUri(it, it1) } }
            filePath?.let { Log.e("onActivityResult: ", it) }

            Picasso.get().load(imageUri).into(image)
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null) {

            image.setImageBitmap(data.extras?.get("data") as Bitmap)
            var uri=getImageUri(context!!, (data.extras?.get("data") as Bitmap?)!!)
            filePath= context?.let {uri?.let { it1 -> getPathFromInputStreamUri(it, it1) } }
            filePath?.let { Log.e("onActivityResult: ", it) }

            Picasso.get().load(uri).into(image)
        }
    }
    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }
    private fun checkPermissionForCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((activity?.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)) {
                val permission = arrayOf(Manifest.permission.CAMERA)

                requestPermissions(permission, HomeFragment.MY_PERMISSIONS_REQUEST_CAMERA) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001

            } else {
                pickFromCamera()
            }
        }
    }

    private fun saveDetails() {


            if (userPreviousData.isEmpty()) {
                val details = UserProfile(1, etFirstName.text.toString(), etLastName.text.toString(), filePath)
                DatabaseClient.getInstance(context).appDatabase.userDao()?.insert(details)
                Toast.makeText(context, "User Updated", Toast.LENGTH_SHORT).show()
            } else {
                val details = userPreviousData[0]?.uid?.let { UserProfile(it, etFirstName.text.toString(), etLastName.text.toString(), filePath) }
                DatabaseClient.getInstance(context).appDatabase.userDao()?.update(details)
                Toast.makeText(context, "User Updated", Toast.LENGTH_SHORT).show()
            }


    }

    private fun pickFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE)
    }

    fun getPathFromInputStreamUri(context: Context, uri: Uri): String? {
        var inputStream: InputStream? = null
        var filePath: String? = null
        if (uri.authority != null) {
            try {
                inputStream = context.getContentResolver().openInputStream(uri)
                val photoFile: File? = createTemporalFileFrom(inputStream)
                filePath = photoFile?.getPath()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return filePath
    }

    @Throws(IOException::class)
    private fun createTemporalFileFrom(inputStream: InputStream?): File? {
        var targetFile: File? = null
        if (inputStream != null) {
            var read: Int
            val buffer = ByteArray(8 * 1024)
            targetFile = createTemporalFile()
            val outputStream: OutputStream = FileOutputStream(targetFile)
            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }
            outputStream.flush()
            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return targetFile
    }

    fun createTemporalFile(): File? {
        try {
            return createImageFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.path
        return image
    }

}