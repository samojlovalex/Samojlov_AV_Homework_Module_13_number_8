package com.example.samojlov_av_homework_module_13_number_8

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.samojlov_av_homework_module_13_number_8.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private lateinit var toolbarSecond: androidx.appcompat.widget.Toolbar
    private lateinit var recyclerViewRV: RecyclerView

    private var listClothes = mutableListOf<Clothes>()
    private var wardrobe = Wardrobe()
    private val db = DBHelper(this, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySecondBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        setContentView(R.layout.activity_second)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

    private fun init() {

        toolbarSecond = binding.toolbarSecond
        setSupportActionBar(toolbarSecond)
        title = getString(R.string.toolbar_title)
        toolbarSecond.subtitle = getString(R.string.toolbar_subtitle)

        recyclerViewRV = binding.recyclerViewRV
        recyclerViewRV.layoutManager = LinearLayoutManager(this)

        listClothesInit()

        initAdapter()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initAdapter() {
        val adapter = CustomAdapter(listClothes)
        recyclerViewRV.adapter = adapter
        recyclerViewRV.setHasFixedSize(true)
        adapter.setOnClothesClickListener(object : CustomAdapter.OnClothesClickListener {
            override fun onClothesClick(clothes: Clothes, position: Int) {
                val intent = Intent(this@SecondActivity, DescriptionActivity::class.java)
                intent.putExtra("clothes", clothes.id)
                startActivity(intent)
//                launchSecondActivityActivity.launch(intent)
            }
        })
        adapter.notifyDataSetChanged()
    }

    private fun listClothesInit() {

        if (db.readClothes().isEmpty()) {
            var id = 0
            for (i in wardrobe.mapDescription) {
                val clothes = Clothes(id, i.key, i.value, wardrobe.mapImage[i.key].toString())
                db.addProduct(clothes)
                ++id
            }
            listClothes = db.readClothes()
        } else listClothes = db.readClothes()
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
                db.removeAll()
                var id = 0
                for (i in wardrobe.mapDescription) {
                    val clothes = Clothes(id, i.key, i.value, wardrobe.mapImage[i.key].toString())
                    db.addProduct(clothes)
                    ++id
                }
                listClothes = db.readClothes()
                initAdapter()
            }
        }
        return super.onOptionsItemSelected(item)
    }
//    private val launchSecondActivityActivity =
//        registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            if (result.resultCode == RESULT_OK) {
//                result.data
//                initAdapter()
//            } else {
//                Toast.makeText(this, "Canceled", Toast.LENGTH_LONG)
//            }
//        }
}