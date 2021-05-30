package fr.Enkofey.iotproject.View.Activity

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import fr.Enkofey.iotproject.Model.Networking.DeviceInfoModel
import fr.Enkofey.iotproject.R
import fr.Enkofey.iotproject.View.Adapter.DeviceAdapter

class DeviceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        // Get List of Paired Bluetooth Device
        val pairedDevices = bluetoothAdapter.bondedDevices
        val deviceList: MutableList<Any> = ArrayList()
        if (pairedDevices.size > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (device in pairedDevices) {
                val deviceName = device.name
                val deviceHardwareAddress = device.address // MAC address
                val deviceInfoModel = DeviceInfoModel(deviceName, deviceHardwareAddress)
                deviceList.add(deviceInfoModel)
            }
            // Display paired device using recyclerView
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewDevice)
            recyclerView.layoutManager = LinearLayoutManager(this)
            val deviceListAdapter = DeviceAdapter(this, deviceList)
            recyclerView.adapter = deviceListAdapter
            recyclerView.itemAnimator = DefaultItemAnimator()
        } else {
            val view: View = findViewById(R.id.recyclerViewDevice)
            val snackbar = Snackbar.make(
                view,
                "Activate Bluetooth or pair a Bluetooth device",
                Snackbar.LENGTH_INDEFINITE
            )
            snackbar.setAction("OK", object : View.OnClickListener {
                override fun onClick(view: View?) {}
            })
            snackbar.show()
        }
    }
}
