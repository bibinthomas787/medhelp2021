package com.medhelp.ui.home

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.medhelp.R
import com.medhelp.base.Cons
import com.medhelp.ui.home.map.MapsActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.io.IOException
import java.util.regex.Matcher
import java.util.regex.Pattern


class HomeFragment : Fragment() {

    private val pickImage = 100
    private var imageUri: Uri? = null
    val REQUEST_CODE = 200


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        root.cvGallery.setOnClickListener(View.OnClickListener {

            checkPermissionForImage()
        })

        root.cvCamera.setOnClickListener(View.OnClickListener {

            checkPermissionForCamera()
        })

        root.btnSearch.setOnClickListener(View.OnClickListener {
           getLatLangFromZipCode(root.etPostal.text.toString())
         //  check(root.etPostal.text.toString())
        })


        root.etPostal.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {


                if (s.length == 6) {
                     btnSearch.visibility = View.VISIBLE
                    check(root.etPostal.text.toString())
                } else {
                    root.btnSearch.visibility = View.INVISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

            }
        })




        return root
    }

    private fun check(value: String) {
        var regexcheck = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z]●?[0-9][A-Z][0-9]$"
        val pattern: Pattern = Pattern.compile(regexcheck)
        val matcher: Matcher = pattern.matcher(value)
        System.out.println(matcher.matches())

        if(matcher.matches())
        {
           // Toast.makeText(context as Activity, "Valid Postal code...", Toast.LENGTH_SHORT).show()
         //   getLatLangFromZipCode(value)
            btnSearch.visibility = View.VISIBLE
        }
        else
        {
            Toast.makeText(context as Activity, "Invalid Postal code...", Toast.LENGTH_SHORT).show()
            btnSearch.visibility = View.INVISIBLE
        }
    }
    private fun getLatLangFromZipCode(zipCode:String){
        val geocoder = Geocoder(context)
        try {
            val addresses: List<Address>? = geocoder.getFromLocationName(zipCode, 1)
            if (addresses != null && !addresses.isEmpty()) {
                val address: Address = addresses[0]

                var intent=Intent(context,MapsActivity::class.java)
                intent.putExtra("lang",address.getLongitude())
                intent.putExtra("lat",address.getLatitude())
                startActivity(intent)
               /* // Use the address as needed
                val message = java.lang.String.format("Latitude: %f, Longitude: %f",
                        address.getLatitude(), address.getLongitude())
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()*/
            } else {
                // Display appropriate message when Geocoder services are not available
                Toast.makeText(context, "Unable to geocode zipcode", Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            // handle exception
        }
    }


    private fun checkPermissionForImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((activity?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)  == PackageManager.PERMISSION_DENIED)
                && (activity?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ) {
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                val permissionCoarse = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                requestPermissions(permission, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001
                requestPermissions(permissionCoarse, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_WRITE LIKE 1002
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
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            Picasso.get().load(imageUri).into(imgPresciption)
        }

        else  if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null){

            imgPresciption.setImageBitmap(data.extras?.get("data") as Bitmap)
        }
    }

    private fun checkPermissionForCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((activity?.checkSelfPermission(Manifest.permission.CAMERA)  == PackageManager.PERMISSION_DENIED) ) {
                val permission = arrayOf(Manifest.permission.CAMERA)

                requestPermissions(permission, MY_PERMISSIONS_REQUEST_CAMERA) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001

            } else {
                pickFromCamera()
            }
        }
    }

    private fun pickFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (permissions.size == 0) {
            return
        }
        var allPermissionsGranted = true
        if (grantResults.size > 0) {
            if (requestCode == REQUEST_CODE) {
                pickFromCamera()
            } else if (requestCode == pickImage) {
                pickImageFromGallery()
            } else {
                for (grantResult in grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false
                        break
                    }
                }
            }
        }
        if (!allPermissionsGranted) {
            var somePermissionsForeverDenied = false
            for (permission in permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permission)) {
                    //denied
                    Toast.makeText(context, "Permission DENIED", Toast.LENGTH_SHORT).show()
                } else {
                    if (context?.let { ActivityCompat.checkSelfPermission(it, permission) } == PackageManager.PERMISSION_GRANTED) {
                        //allowed
                        if (permission == Manifest.permission.READ_EXTERNAL_STORAGE) {
                            checkPermissionForCamera()
                        } else {
                            checkPermissionForImage()
                        }
                        Toast.makeText(context, "Permission ALLOWED", Toast.LENGTH_SHORT).show()
                    } else {
                        //set to never ask again
                        Toast.makeText(context, "Permission Forever DENIED", Toast.LENGTH_SHORT).show()
                        somePermissionsForeverDenied = true
                    }
                }
            }
            if (somePermissionsForeverDenied) {
                val alertDialogBuilder = AlertDialog.Builder(context)
                alertDialogBuilder.setTitle("Permissions Required")
                        .setMessage("You have forcefully denied some of the required permissions " +
                                "for this action. Please open settings, go to permissions and allow them.")
                        .setPositiveButton("Settings") { dialog, which ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", context?.getPackageName(), null)) ///Return the name of the base context this context is derived from.
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                        .setNegativeButton("Cancel") { dialog, which -> }
                        .setCancelable(false)
                        .create()
                        .show()
            }
        }
    }
}