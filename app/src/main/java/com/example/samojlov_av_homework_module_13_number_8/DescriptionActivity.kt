package com.example.samojlov_av_homework_module_13_number_8

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.samojlov_av_homework_module_13_number_8.databinding.ActivityDescriptionBinding
import com.google.gson.Gson
import java.io.IOException
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

@OptIn(ExperimentalStdlibApi::class)
@Suppress("DEPRECATION", "DEPRECATED_IDENTITY_EQUALS")
class DescriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDescriptionBinding
    private lateinit var toolbarDescription: androidx.appcompat.widget.Toolbar
    private lateinit var imageDescriptionIV: ImageView
    private lateinit var nameDescriptionTV: TextView
    private lateinit var descriptionTV: TextView


    private val db = DBHelper(this, null)
    private val wardrobe = Wardrobe()
    private var clothes: Clothes? = null
    private var listClothes = mutableListOf<Clothes>()
    private var clothesId = 0
    private val GALLERY_REQUEST = 26
    private var photo: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDescriptionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        setContentView(R.layout.activity_description)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

    private fun init() {

        toolbarDescription = binding.toolbarDescription
        setSupportActionBar(toolbarDescription)
        title = getString(R.string.toolbar_title)
        toolbarDescription.subtitle = getString(R.string.toolbar_subtitle)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbarDescription.setNavigationOnClickListener {
            onBackPressed()
        }
        toolbarDescription.navigationIcon!!.setColorFilter(
            resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )

        imageDescriptionIV = binding.imageDescriptionIV
        nameDescriptionTV = binding.nameDescriptionTV
        descriptionTV = binding.descriptionTV


        listClothes = db.readClothes()


        clothesInit()
    }

    override fun onResume() {
        super.onResume()
        initView()
        updateName()
        updateDescription()
        updateImage()
    }

    @SuppressLint("MissingInflatedId")
    private fun updateImage() {
        imageDescriptionIV.setOnLongClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogValues = inflater.inflate(R.layout.update_image, null)
            dialogBuilder.setView(dialogValues)
            val editImage = dialogValues.findViewById<ImageView>(R.id.updateImageIV)

            if (clothes != null) {
                initImage(editImage)
            }

            editImage.setOnClickListener {
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
            }

            dialogBuilder.setTitle(getString(R.string.dialog_update_image_Title))
            dialogBuilder.setPositiveButton(getString(R.string.dialog_update_name_positive_button)) { _, _ ->

                if (photo != null) {
                    val type = typeOf<Bitmap>().javaType
                    val gson = Gson().toJson(photo, type)
                    val id = clothes!!.id
                    val name = clothes!!.name
                    val description = clothes!!.description
                    clothes = Clothes(id, name, description, gson)
                    db.updateProduct(clothes!!)
                }
                initView()
            }
            dialogBuilder.setNeutralButton(getString(R.string.basicValuesMenu_Toast)) { _, _ ->
                basicValue()
            }
            dialogBuilder.setNegativeButton(getString(R.string.dialog_NegativeButton), null)
            dialogBuilder.create().show()
            false
        }
    }

    private fun updateDescription() {
        descriptionTV.setOnLongClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogValues = inflater.inflate(R.layout.update_description, null)
            dialogBuilder.setView(dialogValues)
            val editDescription = dialogValues.findViewById<EditText>(R.id.updateDescriptionET)
            editDescription.setText(clothes!!.description)

            dialogBuilder.setTitle(getString(R.string.dialog_update_description_Title))
            dialogBuilder.setPositiveButton(getString(R.string.dialog_update_name_positive_button)) { _, _ ->
                val updateDescription = editDescription.text.toString().trim()
                val id = clothes!!.id
                val name = clothes!!.name
                val image = clothes!!.image
                val updateClothes =
                    Clothes(id, name, updateDescription, image)
                clothes = updateClothes
                db.updateProduct(clothes!!)
                initView()
            }
            dialogBuilder.setNeutralButton(getString(R.string.basicValuesMenu_Toast)) { _, _ ->
                basicValue()
            }
            dialogBuilder.setNegativeButton(getString(R.string.dialog_NegativeButton), null)
            dialogBuilder.create().show()
            false
        }
    }

    private fun updateName() {
        nameDescriptionTV.setOnLongClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogValues = inflater.inflate(R.layout.update_name, null)
            dialogBuilder.setView(dialogValues)
            val editName = dialogValues.findViewById<EditText>(R.id.updateNameET)
            editName.setText(clothes!!.name)

            dialogBuilder.setTitle(getString(R.string.dialog_update_name_Title))
            dialogBuilder.setPositiveButton(getString(R.string.dialog_update_name_positive_button)) { _, _ ->
                val updateName = editName.text.toString().trim()
                val id = clothes!!.id
                val description = clothes!!.description
                val image = clothes!!.image
                val updateClothes =
                    Clothes(id, updateName, description, image)
                clothes = updateClothes
                db.updateProduct(clothes!!)
                initView()
            }
            dialogBuilder.setNeutralButton(getString(R.string.basicValuesMenu_Toast)) { _, _ ->
                basicValue()
            }
            dialogBuilder.setNegativeButton(getString(R.string.dialog_NegativeButton), null)
            dialogBuilder.create().show()
            false
        }
    }

    private fun initView() {
        if (clothes != null) {

            initImage(imageDescriptionIV)

            nameDescriptionTV.text = clothes!!.name
            descriptionTV.text = clothes!!.description
        }
    }

    private fun initImage(editImage: ImageView) {
        if (clothes!!.image!!.toIntOrNull() != null) {
            editImage.setImageResource(clothes!!.image!!.toInt())
        } else {
            val type = typeOf<Bitmap>().javaType
            val image = Gson().fromJson<Bitmap>(clothes!!.image!!, type)
            editImage.setImageBitmap(image)
        }
    }

    private fun clothesInit() {
        clothesId = intent.extras!!.getInt("clothes")
        for (i in listClothes.indices) {
            if (listClothes[i].id == clothesId) {
                clothes = listClothes[i]
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    @SuppressLint("SetTextI18n", "ShowToast")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenu -> {
                Toast.makeText(
                    applicationContext, getString(R.string.toast_exit), Toast.LENGTH_LONG
                ).show()
                finishAffinity()
            }

            R.id.basicValuesMenu -> {
                Toast.makeText(
                    applicationContext, getString(R.string.basicValuesMenu_Toast), Toast.LENGTH_LONG
                ).show()
                basicValue()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun basicValue() {
        val id = clothesId
        val name = wardrobe.listIdInit()[id]
        val description = wardrobe.mapDescription[name]
        val image = wardrobe.mapImage[name].toString()
        clothes = Clothes(id, name, description, image)
        db.updateProduct(clothes!!)
        initView()
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GALLERY_REQUEST -> if (resultCode === RESULT_OK) {
                val selectedImage: Uri? = data?.data
                try {
                    photo = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

//    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
//    override fun onBackPressed() {
//        super.onBackPressed()
//        val intent = Intent()
//        setResult(RESULT_OK,intent)
//    }

}