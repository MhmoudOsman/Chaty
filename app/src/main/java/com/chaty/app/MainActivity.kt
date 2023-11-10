package com.chaty.app

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.chaty.app.databinding.ActivityMainBinding
import com.chaty.app.tools.getDefaultLanguage
import com.chaty.app.tools.setLocale
import com.chaty.data.tools.CacheHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var cacheHelper: CacheHelper
    private val navHost: NavHostFragment by lazy { supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment }
    private val navController :NavController by lazy { navHost.navController }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()

    }

    private fun setupNavigation() {
        val graph  = if (cacheHelper.fetchBoolean(CacheHelper.PREFERENCE_USER_LOGGED)) {
            R.navigation.main_graph
        }else {
            R.navigation.auth_graph
        }
        val navGraph = navHost.navController.navInflater.inflate(graph)
        navController.setGraph(navGraph,null)

        binding.toolbar.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            if (controller.graph.id == R.id.main_graph) {
                binding.bottomBar.visibility = View.VISIBLE
//                binding.bottomBar.setupWithNavController(controller)
            } else {
                binding.bottomBar.visibility = View.GONE
            }
        }
    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onNavigateUp()
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