package fr.Enkofey.iotproject.ViewModel

import android.bluetooth.BluetoothSocket
import android.os.Handler
import fr.Enkofey.iotproject.Model.Networking.ConnectedThread
import fr.Enkofey.iotproject.Model.Networking.CreateConnectThread
import fr.Enkofey.iotproject.View.Activity.MainActivity


object ConnectionManagerViewHolder {
    public var deviceAdress : String? = null
    public var deviceName : String? = null
    public var deviceStrength : String? = null
    public var handler: Handler? = null
    var mmSocket: BluetoothSocket? = null
    var connectedThread: ConnectedThread? = null
    var createConnectThread: CreateConnectThread? = null
}