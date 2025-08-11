package com.mrclassic.mcv.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.mrclassic.mcv.R
import com.mrclassic.mcv.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val titles = listOf(
            getString(R.string.tab_configurations),
            getString(R.string.tab_host_scanner),
            getString(R.string.tab_logs),
            getString(R.string.tab_analytics),
            getString(R.string.tab_settings),
            getString(R.string.tab_about)
        )
        val fragments = listOf(
            PlaceholderFragment.newInstance("Configurations"),
            PlaceholderFragment.newInstance("Host Scanner"),
            PlaceholderFragment.newInstance("Logs"),
            PlaceholderFragment.newInstance("Analytics"),
            PlaceholderFragment.newInstance("Settings"),
            PlaceholderFragment.newInstance("About")
        )

        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragments.size
            override fun createFragment(position: Int) = fragments[position]
        }
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = fragments.size

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()

        lifecycleScope.launch {
            val shouldShow = viewModel.shouldShowFirstRunDialog().first()
            if (shouldShow) showFirstRunDialog()
        }
    }

    private fun showFirstRunDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.first_run_title)
            .setMessage(R.string.first_run_message)
            .setPositiveButton(R.string.first_run_open) { dialog, _ ->
                val uri = Uri.parse("https://t.me/iq_Holy_Space")
                startActivity(Intent(Intent.ACTION_VIEW, uri))
                dialog.dismiss()
            }
            .setNeutralButton(R.string.first_run_dont_show) { dialog, _ ->
                lifecycleScope.launch { viewModel.setFirstRunShown() }
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}