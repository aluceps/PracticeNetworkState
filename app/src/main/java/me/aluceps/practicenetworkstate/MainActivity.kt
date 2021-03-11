package me.aluceps.practicenetworkstate

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
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
        Snackbar.make(binding.root, createNetworkStateMessage(this), Snackbar.LENGTH_SHORT).show()
    }

    private fun createNetworkStateMessage(context: Context): String =
            MESSAGE_FORMAT.format(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getNetworkState(context)
            } else {
                getConnectionState(context)
            })

    /**
     * ネットワークの接続状態を取得
     * @see https://developer.android.com/training/basics/network-ops/managing?hl=ja
     */
    private fun getConnectionState(context: Context): ConnectionState =
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).let { manager ->
                manager.activeNetworkInfo.let { networkInfo ->
                    when (networkInfo != null && networkInfo.isConnected) {
                        true -> ConnectionState.ONLINE
                        else -> ConnectionState.OFFLINE
                    }
                }
            }

    /**
     * ネットワークの状態を取得
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun getNetworkState(context: Context): NetworkState =
            context.getSystemService(ConnectivityManager::class.java).let { manager ->
                manager.getNetworkCapabilities(manager.activeNetwork).let { capabilities ->
                    if (capabilities != null) {
                        when {
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkState.WIFI
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkState.MOBILE
                            else -> NetworkState.OTHER
                        }
                    } else {
                        NetworkState.NOT_CONNECTED
                    }
                }
            }

    enum class ConnectionState {
        ONLINE,
        OFFLINE
    }

    enum class NetworkState {
        WIFI,
        MOBILE,
        OTHER,
        NOT_CONNECTED
    }

    companion object {
        const val MESSAGE_FORMAT = "Network is: %s"
    }
}