package com.example.nct_lite.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nct_lite.R
import com.example.nct_lite.databinding.ActivityMainBinding

/**
 * MainActivity - Activity chính của ứng dụng
 * 
 * Chức năng:
 * - Có Header (Toolbar) ở phía trên
 * - Có Bottom Navigation Bar (Menu bar) ở phía dưới
 * - Sử dụng Navigation Component để quản lý và chuyển đổi giữa các Fragment:
 *   + HomeFragment (navigation_home)
 *   + SearchFragment (navigation_search)
 *   + LibraryFragment (navigation_library)
 * 
 * Cách hoạt động:
 * - Khi người dùng click vào item trong bottom navigation,
 *   Navigation Component sẽ tự động gọi và hiển thị Fragment tương ứng
 *   trong FragmentContainerView (nav_host_fragment_activity_main)
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Header (Toolbar)
        setSupportActionBar(binding.toolbar)

        // Lấy NavHostFragment - container chứa các Fragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        
        // Lấy NavController - điều khiển navigation giữa các Fragment
        val navController = navHostFragment.navController

        // Cấu hình AppBar (Toolbar) để hiển thị title của Fragment hiện tại
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,      // Home Fragment
                R.id.navigation_search,   // Search Fragment
                R.id.navigation_library   // Library Fragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        
        // Kết nối Bottom Navigation với NavController
        // Khi click vào item trong bottom nav, sẽ tự động chuyển đến Fragment tương ứng
        binding.navView.setupWithNavController(navController)
    }
}
