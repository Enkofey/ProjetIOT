package fr.Enkofey.iotproject.View.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.Enkofey.iotproject.Model.Networking.DeviceInfoModel
import fr.Enkofey.iotproject.R
import fr.Enkofey.iotproject.View.Activity.MainActivity
import fr.Enkofey.iotproject.ViewModel.ConnectionManagerViewHolder


class DeviceAdapter(context: Context, deviceList: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val context: Context
        private val deviceList: List<Any>

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            lateinit var textName: TextView
            lateinit var textAddress: TextView
            lateinit var linearLayout: LinearLayout

            init {
                textName = v.findViewById(R.id.textViewDeviceName)
                textAddress = v.findViewById(R.id.textViewDeviceAddress)
                linearLayout = v.findViewById(R.id.linearLayoutDeviceInfo)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val v: View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_device_list, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val itemHolder = holder as ViewHolder
            val deviceInfoModel: DeviceInfoModel = deviceList[position] as DeviceInfoModel
            itemHolder.textName.setText(deviceInfoModel.deviceName)
            itemHolder.textAddress.setText(deviceInfoModel.deviceHardwareAddress)

            // When a device is selected
            itemHolder.linearLayout.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    val intent = Intent(context, MainActivity::class.java)
                    // Send device details to the MainActivity
                    ConnectionManagerViewHolder.deviceName = deviceInfoModel.deviceName
                    ConnectionManagerViewHolder.deviceAdress = deviceInfoModel.deviceHardwareAddress
                    // Call MainActivity
                    context.startActivity(intent)
                }
            })
        }

        override fun getItemCount(): Int {
            return deviceList.size
        }

        init {
            this.context = context
            this.deviceList = deviceList
        }
}