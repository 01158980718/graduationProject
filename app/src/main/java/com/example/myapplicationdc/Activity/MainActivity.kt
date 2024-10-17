//package com.example.myapplicationdc.Activity
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.lifecycle.Observer
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.myapplicationdc.Adapter.CategoryAdapter
//import com.example.myapplicationdc.Adapters.TopDoctorAdapter
//import com.example.myapplicationdc.R
//import com.example.myapplicationdc.ViewModel.MainViewModel
//import com.example.myapplicationdc.databinding.ActivityMainBinding
//
//
//class MainActivity: BaseActivity() {
//    private lateinit var binding: ActivityMainBinding
//    private val viewModel = MainViewModel()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        initCategory()
//        initTopDoctor()
//    }
//    private fun initTopDoctor() {
//        binding.progressBarTopDoctors.visibility=View.VISIBLE
//            viewModel.doctor.observe(this@MainActivity, Observer{
//                binding.recyclerViewTopDoctors.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
//                binding.recyclerViewTopDoctors.adapter = TopDoctorAdapter(it)
//                binding.progressBarTopDoctors.visibility=View.GONE
//            })
//            viewModel.loadDoctors()
//        binding.doctorlistText.setOnClickListener{
//            startActivity(Intent(this@MainActivity,TopDoctorActivity::class.java))
//        }
//        }
//
//
//
//    private fun initCategory() {
//        binding.progressBarCategory.visibility = View.VISIBLE
//        viewModel.category.observe( this, Observer {
//            binding.viewCategory.layoutManager =
//                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL,false)
//            binding.viewCategory.adapter = CategoryAdapter(it)
//            binding.progressBarCategory.visibility =View.GONE
//        })
//        viewModel.loadCategory()
//    }
//}


package com.example.myapplicationdc.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplicationdc.Adapter.CategoryAdapter
import com.example.myapplicationdc.Adapters.TopDoctorAdapter
import com.example.myapplicationdc.ViewModel.MainViewModel
import com.example.myapplicationdc.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initCategory()
        initTopDoctor()

        binding.doctorlistText.setOnClickListener {
            startActivity(Intent(this@MainActivity, TopDoctorActivity::class.java))
        }
        binding.history.setOnClickListener{
            startActivity(Intent(this@MainActivity, HistoryActivity::class.java))
        }
        binding.DoctorDashboard.setOnClickListener {
            startActivity(Intent(this@MainActivity, Doctor_Dashboard::class.java))
        }
    }

    private fun initTopDoctor() {
        binding.progressBarTopDoctors.visibility = View.VISIBLE

        viewModel.doctor.observe(this@MainActivity, Observer { doctors ->
            // Set up RecyclerView layout and adapter when data is available
            binding.recyclerViewTopDoctors.layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.recyclerViewTopDoctors.adapter = TopDoctorAdapter(doctors)

            // Hide progress bar after data is loaded
            binding.progressBarTopDoctors.visibility = View.GONE
        })

        viewModel.loadDoctors()
    }

    private fun initCategory() {
        binding.progressBarCategory.visibility = View.VISIBLE

        viewModel.category.observe(this@MainActivity, Observer { categories ->
            binding.viewCategory.layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.viewCategory.adapter = CategoryAdapter(categories)

            binding.progressBarCategory.visibility = View.GONE
        })

        viewModel.loadCategory()
    }
}
