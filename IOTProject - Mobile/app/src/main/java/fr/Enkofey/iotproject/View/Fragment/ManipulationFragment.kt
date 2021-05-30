package fr.Enkofey.iotproject.View.Fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import fr.Enkofey.iotproject.Model.Networking.BluetoothConnectionAdapter
import fr.Enkofey.iotproject.R
import fr.Enkofey.iotproject.View.Activity.DeviceActivity
import fr.Enkofey.iotproject.ViewModel.ConnectionManagerViewHolder

class ManipulationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manipulation, container, false)
    }

    lateinit var downButton: ImageButton
    lateinit var upButton: ImageButton
    lateinit var leftButton: ImageButton
    lateinit var rightButton: ImageButton
    lateinit var bluetoothButton: ImageView
    lateinit var  centerButton: ImageButton
    lateinit var imageState : ImageView
    lateinit var textSignalStrength: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var successToast = Toast.makeText(
            requireActivity(),
            "Connected to : " + ConnectionManagerViewHolder.deviceName,
            Toast.LENGTH_SHORT
        )

        var failToast = Toast.makeText(
            requireActivity(),
            "Couldn't connect to device",
            Toast.LENGTH_SHORT
        )

        textSignalStrength = view.findViewById(R.id.manipulation_state_Strength_TextView)
        downButton = view.findViewById(R.id.manipulation_down_button)
        leftButton = view.findViewById(R.id.manipulation_left_button)
        rightButton = view.findViewById(R.id.manipulation_right_button)
        centerButton = view.findViewById(R.id.manipulation_center_button)
        upButton = view.findViewById(R.id.manipulation_up_button)
        bluetoothButton = view.findViewById(R.id.manipulation_bluetooth_button)
        imageState = view.findViewById(R.id.manipulation_state_image_view)

        BluetoothConnectionAdapter.bluetoothAdapter


        bluetoothButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent = Intent(requireActivity(), DeviceActivity::class.java)
                startActivity(intent)
            }
        })

        centerButton.setOnClickListener{
            if(ConnectionManagerViewHolder.connectedThread != null){
                val cmdText = "S"
                Log.d("commande",cmdText)
                ConnectionManagerViewHolder.connectedThread!!.write(cmdText)
            }
        }

        downButton.setOnClickListener{
            if(ConnectionManagerViewHolder.connectedThread != null){
                val cmdText = "B"
                Log.d("commande",cmdText)
                ConnectionManagerViewHolder.connectedThread!!.write(cmdText)
            }
        }
        rightButton.setOnClickListener{
            if(ConnectionManagerViewHolder.connectedThread != null){
                val cmdText = "R"
                Log.d("commande",cmdText)
                ConnectionManagerViewHolder.connectedThread!!.write(cmdText)
            }
        }
        upButton.setOnClickListener{
            if(ConnectionManagerViewHolder.connectedThread != null){
                val cmdText = "F"
                Log.d("commande",cmdText)
                ConnectionManagerViewHolder.connectedThread!!.write(cmdText)
            }
        }
        leftButton.setOnClickListener{
            if(ConnectionManagerViewHolder.connectedThread != null){
                val cmdText = "L"
                Log.d("commande",cmdText)
                ConnectionManagerViewHolder.connectedThread!!.write(cmdText)
            }
        }


        ConnectionManagerViewHolder.handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    ConnectionManagerViewHolder.createConnectThread!!.CONNECTING_STATUS -> when (msg.arg1) {
                        1 -> {
                            successToast.show()
                            textSignalStrength.setText("Etablie")
                            Glide.with(requireContext()).load(R.drawable.robot_sleep).override(100).into(imageState)
                        }
                        -1 -> {
                            textSignalStrength.setText("Non établie")
                            failToast.show()
                        }
                    }
                    ConnectionManagerViewHolder.connectedThread!!.MESSAGE_READ -> {
                        val arduinoMsg = msg.obj.toString() // Read message from Arduino
                        when (arduinoMsg.toLowerCase()) {
                            "sm" -> {
                                Log.d("receive","sm")
                                Glide.with(requireContext()).load(R.drawable.robot).override(100).into(imageState)
                            }
                            "st" -> {
                                Log.d("receive","st")
                                Glide.with(requireContext()).load(R.drawable.robot_sleep).override(100).into(imageState)
                            }
                        }
                    }
                }
            }
        }

    }
}