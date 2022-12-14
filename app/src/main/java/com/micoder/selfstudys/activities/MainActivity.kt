package com.micoder.selfstudys.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.andremion.floatingnavigationview.FloatingNavigationView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.micoder.selfstudys.R
import com.micoder.selfstudys.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding // view binding

    private var mToolBar: MaterialToolbar? = null

    private lateinit var mFloatingNavigationView: FloatingNavigationView

    private lateinit var switchMaterial: SwitchMaterial
    private val MyPREFERENCES: String = "nightModePrefs"
    private val KEY_ISNIGHTMODE: String = "isNightMode"
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // view binding implementation
        setContentView(binding.root)

        // Shared pref
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)

        // Action Bar
        mToolBar = findViewById<View>(R.id.main_page_toolbar) as MaterialToolbar
        setSupportActionBar(mToolBar)

        val radius =
            resources.getDimension(R.dimen.activity_half_margin)
        val toolbarBackground = mToolBar!!.getBackground() as MaterialShapeDrawable
        toolbarBackground.shapeAppearanceModel = toolbarBackground.shapeAppearanceModel
            .toBuilder()
            .setBottomRightCorner(CornerFamily.ROUNDED, radius)
            .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
            .build()

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayShowCustomEnabled(true)

        val layoutInflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val actionBarView: View = layoutInflater.inflate(R.layout.app_bar_item, null)
        val text = actionBarView.findViewById<TextView>(R.id.actionBarTitle)

        val spannableString = SpannableString("SelfStudys")
        spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.liteBlue)), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.liteGreen)), 4, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.setText(spannableString)
        actionBar?.setCustomView(actionBarView)

        // bottom nav
        navController = findNavController(R.id.main_fragment)
        setupActionBarWithNavController(navController)
        setupSmoothBottomMenu()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.updatesFragment) {
                mToolBar!!.navigationIcon = null
            }
            if (destination.id == R.id.notificationFragment) {
                mToolBar!!.navigationIcon = null
            }
            if (destination.id == R.id.profileFragment) {
                mToolBar!!.navigationIcon = null
            }
        }


        // Floating Navigation View
        mFloatingNavigationView = findViewById<View>(R.id.floating_navigation_view) as FloatingNavigationView
        mFloatingNavigationView.setOnClickListener { mFloatingNavigationView.open() }
        mFloatingNavigationView.setNavigationItemSelectedListener { item ->

            when (item.itemId){
                R.id.changeExamFab -> {
                    startActivity(Intent(this, SelectCBActivity::class.java))
                }
            }

            mFloatingNavigationView.close()
            true
        }

        // navigationView nightmode switch control
        val menuItem = mFloatingNavigationView.menu.findItem(R.id.nightModeFab) // first insialize MenuItem

        switchMaterial = menuItem.actionView?.findViewById<View>(R.id.drawer_switch) as SwitchMaterial

        checkNightModeActivated()

        switchMaterial.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                Toast.makeText(this, "Dark Mode Enabled", Toast.LENGTH_SHORT).show()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                saveNightModeState(true)
                recreate()
            } else {
                Toast.makeText(this, "Dark Mode Disabled", Toast.LENGTH_SHORT).show()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                saveNightModeState(false)
                recreate()
            }
        }
    }

    private fun saveNightModeState(nightMode: Boolean) {
        var editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(KEY_ISNIGHTMODE, nightMode)
        editor.apply()
    }
    private fun checkNightModeActivated() {
        if (sharedPreferences.getBoolean(KEY_ISNIGHTMODE, false)) {
            switchMaterial.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            switchMaterial.isChecked = false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    // bottom nav bar with smooth nav bar
    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.bottom_nav_menu)
        val menu = popupMenu.menu
        binding.bottomBar.setupWithNavController(menu, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    // Floating Navigation View
    override fun onBackPressed() {
        if (mFloatingNavigationView.isOpened) {
            mFloatingNavigationView.close()
        } else {
            super.onBackPressed()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.share_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.shareMenuItem -> {
                Toast.makeText(this,"Share Selected",Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}