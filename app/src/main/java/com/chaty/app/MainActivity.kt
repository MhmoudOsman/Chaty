package com.chaty.app

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.chaty.app.databinding.ActivityMainBinding
import com.chaty.app.messages_screen.ui.MessagesFragmentDirections
import com.chaty.app.tools.getDefaultLanguage
import com.chaty.app.tools.setLocale
import com.chaty.data.tools.CacheHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton.OnVisibilityChangedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var cacheHelper: CacheHelper
    private val navHost: NavHostFragment by lazy { supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment }
    private val navController: NavController by lazy { navHost.navController }

    private val graph
        get() = if (cacheHelper.fetchBoolean(CacheHelper.PREFERENCE_USER_LOGGED)) {
            R.navigation.main_graph
        } else {
            R.navigation.auth_graph
        }
    private val appBarConfiguration
        get() = if (cacheHelper.fetchBoolean(CacheHelper.PREFERENCE_USER_LOGGED)) {
            AppBarConfiguration(binding.bottomBar.menu)
        } else {
            AppBarConfiguration(navController.graph)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        askPermissions()
        setupNavigation()

    }

    private fun setupNavigation() {
        if (graph ==  R.navigation.auth_graph) {
            binding.appbar.visibility = View.GONE
        }
        val navGraph = navHost.navController.navInflater.inflate(graph)
        navController.setGraph(navGraph, null)
        binding.bottomBar.setupWithNavController(navController)
        binding.toolbar.setupWithNavController(navController,appBarConfiguration)

        binding.fab.setOnClickListener {
            navController.currentDestination?.let {
                when (it.id) {
                    R.id.messages_screen -> {
                        val directions = MessagesFragmentDirections.actionMessagesScreenToGetContactScreen()
                        navController.navigate(directions)
                    }

                    R.id.groups_screen -> {

                    }

                    R.id.story_screen -> {

                    }

                    R.id.profile_screen -> {

                    }

                    else -> {

                    }
                }
            }
        }
        navController.addOnDestinationChangedListener { controller, destination, _ ->

            when (destination.id) {
                R.id.messages_screen -> {
                    binding.bottomBar.visibility = View.VISIBLE
                    binding.fab.apply {
                        setImageResource(R.drawable.ic_start_chat)
                        show()
                    }
                }

                R.id.groups_screen -> {
                    binding.bottomBar.visibility = View.VISIBLE
                    binding.fab.apply {
                        setImageResource(R.drawable.ic_groups)
                        show()
                    }
                }

                R.id.story_screen -> {
                    binding.bottomBar.visibility = View.VISIBLE
                    binding.fab.apply {
                        setImageResource(R.drawable.ic_story)
                        show()
                    }
                }

                R.id.profile_screen -> {
                    binding.bottomBar.visibility = View.VISIBLE
                    binding.fab.hide()

                }

                else -> {
//                    binding.toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_back)
                    binding.bottomBar.visibility = View.GONE
                    binding.fab.hide()
                }
            }
        }

    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onNavigateUp()
    }

    fun refreshActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    private fun askPermissions() {
        val permissionList = mutableListOf<String>()
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!checkPermission(Manifest.permission.POST_NOTIFICATIONS)) {
                permissionList.add(Manifest.permission.POST_NOTIFICATIONS)
            }

        }
        if (!checkPermission(Manifest.permission.CAMERA)) {
            permissionList.add(Manifest.permission.CAMERA)
        }
        if (!checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE)

        }
        if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        }

        if (!checkPermission(Manifest.permission.READ_CONTACTS)) {
            permissionList.add(Manifest.permission.READ_CONTACTS)

        }
        if (!checkPermission(Manifest.permission.WRITE_CONTACTS)) {
            permissionList.add(Manifest.permission.WRITE_CONTACTS)

        }
        if (permissionList.isNotEmpty()) {
            requestPermissions(permissionList.toTypedArray(), 10001)
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun attachBaseContext(newBase: Context) {
        cacheHelper = CacheHelper.getInstance(newBase.applicationContext)
        AppCompatDelegate.setDefaultNightMode(
            cacheHelper.fetchInteger(
                CacheHelper.PREFERENCE_THEME,
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        )
        super.attachBaseContext(
            setLocale(
                newBase,
                cacheHelper.fetchString(CacheHelper.PREFERENCE_LANGUAGE, getDefaultLanguage())
            )
        )
    }

}