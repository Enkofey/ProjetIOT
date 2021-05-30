package fr.Enkofey.iotproject.Model.Networking

import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.util.Log
import fr.Enkofey.iotproject.ViewModel.ConnectionManagerViewHolder
import fr.Enkofey.iotproject.ViewModel.ConnectionManagerViewHolder.connectedThread
import fr.Enkofey.iotproject.ViewModel.ConnectionManagerViewHolder.handler
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ConnectedThread() : Thread() {

        public lateinit var mmSocket : BluetoothSocket
        private lateinit var mmInStream : InputStream
        private lateinit var mmOutStream : OutputStream

    public val MESSAGE_READ = 2

    constructor(socket: BluetoothSocket) : this() {
            mmSocket = socket
            var tmpIn : InputStream? = null
            var  tmpOut : OutputStream? = null

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream()
                tmpOut = socket.getOutputStream()
            } catch (e: IOException) { }

            if (tmpIn != null) {
                mmInStream = tmpIn
            }
            if (tmpOut != null) {
                mmOutStream = tmpOut
            }
        }

        override fun run() {
            var buffer = ByteArray(1024) // buffer store for the stream
            var bytes = 0 // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    /*
                    Read from the InputStream from Arduino until termination character is reached.
                    Then send the whole String message to GUI Handler.
                     */

                    //var byte = mmInStream.read()
                    buffer[bytes] = mmInStream.read().toByte()

                    var readMessage : String
                    if (buffer[bytes] == '\n'.toByte()){
                        readMessage = String(buffer, 0, bytes)
                        ConnectionManagerViewHolder.handler!!.obtainMessage(MESSAGE_READ,readMessage).sendToTarget()
                        bytes = 0
                    } else {
                        bytes++
                    }


                } catch (e: IOException) {
                    e.printStackTrace()
                    break
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        fun write(input: String) {
            var bytes : ByteArray  = input.toByteArray() //converts entered String into bytes
            try {
                mmOutStream.write(bytes)
                Log.d("success send",mmOutStream.toString())
            } catch (e: IOException) {
                Log.e("Send Error", "Unable to send message", e)
            }
        }

        /* Call this from the main activity to shutdown the connection */
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) { }
        }
    }
