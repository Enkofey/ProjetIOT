package fr.Enkofey.iotproject.Model.Networking

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import fr.Enkofey.iotproject.ViewModel.ConnectionManagerViewHolder
import fr.Enkofey.iotproject.ViewModel.ConnectionManagerViewHolder.connectedThread
import fr.Enkofey.iotproject.ViewModel.ConnectionManagerViewHolder.handler
import fr.Enkofey.iotproject.ViewModel.ConnectionManagerViewHolder.mmSocket
import java.io.IOException
import java.util.*

class CreateConnectThread() : Thread() {


    public val CONNECTING_STATUS = 1 // used in bluetooth handler to identify message status

        constructor(bluetoothAdapter: BluetoothAdapter, address : String) : this() {
            /*
            Use a temporary object that is later assigned to mmSocket
            because mmSocket is final.
             */
            var bluetoothDevice : BluetoothDevice = bluetoothAdapter.getRemoteDevice(address)
            var tmp : BluetoothSocket? = null
            var uuid : UUID = bluetoothDevice.getUuids()[0].getUuid()

            try {
                /*
                Get a BluetoothSocket to connect with the given BluetoothDevice.
                Due to Android device varieties,the method below may not work fo different devices.
                You should try using other methods i.e. :
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID)
                 */
                tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid)

            } catch (e : IOException) {
                Log.e("error", "Socket's create() method failed", e)
            }
            mmSocket = tmp!!
        }

        override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            var bluetoothAdapter : BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            bluetoothAdapter.cancelDiscovery()
            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket!!.connect()
                Log.e("Status", "Device connected")
                handler!!.obtainMessage(CONNECTING_STATUS, 1, -1).sendToTarget()
            } catch (connectException : IOException) {
                // Unable to connect close the socket and return.
                try {
                    mmSocket!!.close()
                    Log.e("Status", "Cannot connect to device")
                    handler!!.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget()
                } catch ( closeException : IOException) {
                    Log.e("error", "Could not close the client socket", closeException)
                }
                return
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            connectedThread = ConnectedThread(mmSocket!!)
            connectedThread!!.run()
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket!!.close()
            } catch (e : IOException) {
                Log.e("error", "Could not close the client socket", e)
            }
        }
    }