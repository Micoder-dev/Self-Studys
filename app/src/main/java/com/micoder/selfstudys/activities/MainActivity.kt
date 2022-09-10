package com.micoder.selfstudys.activities

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.switchmaterial.SwitchMaterial
import com.micoder.selfstudys.R
import com.micoder.selfstudys.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding // view binding

    private var mToolBar: MaterialToolbar? = null

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // view binding implementation
        setContentView(binding.root)


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
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_home)

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


        // nav drawer
        drawerLayout = findViewById<View>(R.id.drawerLayout) as DrawerLayout
        navigationView = findViewById<View>(R.id.navigation_view) as NavigationView
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        navigationView.setNavigationItemSelectedListener(this)

        // navigationView nightmode switch control
        val menuItem = navigationView.menu.findItem(R.id.nightModeFab) // first insialize MenuItem

        @SuppressLint("UseSwitchCompatOrMaterialCode") val switchButton =
            menuItem.actionView.findViewById<View>(R.id.drawer_switch) as SwitchMaterial
        switchButton.setOnCheckedChangeListener { compoundButton: CompoundButton?, b: Boolean ->
            if (b) {
                Toast.makeText(this, "True", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "False", Toast.LENGTH_SHORT).show()
            }
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.share_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //nav drawer//
        if (toggle.onOptionsItemSelected(item)){
            return true
        }//nav drawer complete//


        when (item.itemId){
            R.id.shareMenuItem -> Toast.makeText(this,"Share Selected",Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    //nav drawer
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profileFab -> {
                drawerClose()
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
            }
            R.id.changeExamFab -> {
                drawerClose()
                Toast.makeText(this, "Change Exam", Toast.LENGTH_SHORT).show()
            }
            R.id.nightModeFab -> {
                drawerClose()
                Toast.makeText(this, "Night Mode", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }


    private fun drawerClose() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }


}