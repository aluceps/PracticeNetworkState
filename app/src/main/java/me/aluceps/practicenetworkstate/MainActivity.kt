package me.aluceps.practicenetworkstate

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import me.aluceps.practicenetworkstate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.root
    }

    override fun onResume() {
        super.onResume()
        Snackbar.make(binding.root, "Network is online? : %b".format(isOnline(this)), Snackbar.LENGTH_SHORT).show()
    }

    /**
     * ネットワークの接続状態を取得
     * @see https://developer.android.com/training/basics/network-ops/managing?hl=ja
     */
    private fun isOnline(context: Context): Boolean {
        val connectiveManager = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            context.getSystemService(ConnectivityManager::class.java)
        } else {
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }

        val networkInfo = connectiveManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}