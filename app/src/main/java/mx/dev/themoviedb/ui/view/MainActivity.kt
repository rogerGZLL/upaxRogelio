package mx.dev.themoviedb.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import mx.dev.themoviedb.R
import mx.dev.themoviedb.databinding.ActivityMainBinding
import mx.dev.themoviedb.databinding.ActivitySplashBinding
import mx.dev.themoviedb.ui.view.fragments.FragmentLocation
import mx.dev.themoviedb.ui.view.fragments.FragmentMovies
import mx.dev.themoviedb.ui.view.fragments.FragmentStorage

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val moviesFragment = FragmentMovies()
        val locationFragment = FragmentLocation()
        val storageFragment = FragmentStorage()
        setCurrentFragment(moviesFragment)
        setSupportActionBar(binding.include.toolbar)
        setToolbarTitle(getString(R.string.menu_movies))
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.ic_movies -> {
                    setCurrentFragment(moviesFragment)
                    setToolbarTitle(getString(R.string.menu_movies))
                }
                R.id.ic_location -> {
                    setCurrentFragment(locationFragment)
                    setToolbarTitle(getString(R.string.menu_location))
                }
                R.id.ic_storage -> {
                    setCurrentFragment(storageFragment)
                    setToolbarTitle(getString(R.string.menu_storage))
                }
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(binding.flContainer.id, fragment, "1")
            commit()
        }
    }

    private fun setToolbarTitle(title: String){
        supportActionBar?.setTitle(title)
    }

}