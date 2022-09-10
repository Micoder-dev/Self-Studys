package com.micoder.selfstudys

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.micoder.selfstudys.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding // view binding

    private var mToolBar: MaterialToolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // view binding implementation
        setContentView(binding.root)


        mToolBar = findViewById<View>(R.id.main_page_toolbar) as MaterialToolbar
        setSupportActionBar(mToolBar)

        val radius =
            resources.getDimension(com.shashank.sony.fancywalkthroughlib.R.dimen.activity_half_margin)
        val toolbarBackground = mToolBar!!.getBackground() as MaterialShapeDrawable
        toolbarBackground.shapeAppearanceModel = toolbarBackground.shapeAppearanceModel
            .toBuilder()
            .setBottomRightCorner(CornerFamily.ROUNDED, radius)
            .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
            .build()

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayShowCustomEnabled(true)

        val layoutInflater =
            this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val actionBarView: View = layoutInflater.inflate(R.layout.app_bar_item, null)
        actionBar?.setCustomView(actionBarView)


        navController = findNavController(R.id.main_fragment)
        setupActionBarWithNavController(navController)
        setupSmoothBottomMenu()
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

}