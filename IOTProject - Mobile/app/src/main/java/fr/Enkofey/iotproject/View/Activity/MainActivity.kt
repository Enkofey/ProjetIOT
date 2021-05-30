package fr.Enkofey.iotproject.View.Activity

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.Enkofey.iotproject.Model.Networking.CreateConnectThread
import fr.Enkofey.iotproject.R
import fr.Enkofey.iotproject.View.Fragment.ManipulationFragment
import fr.Enkofey.iotproject.View.Fragment.PetFragment
import fr.Enkofey.iotproject.ViewModel.ConnectionManagerViewHolder


class MainActivity : AppCompatActivity() {

    val REQUEST_ENABLE_BT = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initializes Bluetooth adapter.
        val bluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter

        var successToast = Toast.makeText(
            this,
            "Connected to : " + ConnectionManagerViewHolder.deviceName,
            Toast.LENGTH_SHORT
        )

        var failToast = Toast.makeText(
            this,
            "Couldn't connect to device",
            Toast.LENGTH_SHORT
        )

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        if(ConnectionManagerViewHolder.deviceName != null){
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            val deviceAddress : String = ConnectionManagerViewHolder.deviceAdress!!
            ConnectionManagerViewHolder.createConnectThread = CreateConnectThread(
                bluetoothAdapter,
                deviceAddress
            )
            ConnectionManagerViewHolder.createConnectThread!!.start()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.menuNavigationBar)
        bottomNav.setOnNavigationItemSelectedListener(navListener)
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container, ManipulationFragment()
        ).commit()

        //registerReceiver(receiver,)

    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment? = null
        when (item.itemId) {
            R.id.nav_manipulation -> selectedFragment = ManipulationFragment()
            R.id.nav_pet -> selectedFragment = PetFragment()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, selectedFragment!!).commit()
        true
    }

    override fun onBackPressed() {
        // Terminate Bluetooth Connection and close app
        if (ConnectionManagerViewHolder.createConnectThread != null){
            ConnectionManagerViewHolder.createConnectThread!!.cancel()
        }
        var intent : Intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}