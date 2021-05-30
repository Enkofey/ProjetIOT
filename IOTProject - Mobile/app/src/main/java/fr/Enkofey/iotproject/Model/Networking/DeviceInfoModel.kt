package fr.Enkofey.iotproject.Model.Networking

class DeviceInfoModel {
    var deviceName: String? = null
        private set
    var deviceHardwareAddress: String? = null
        private set

    constructor(deviceName: String?, deviceHardwareAddress: String?) {
        this.deviceName = deviceName
        this.deviceHardwareAddress = deviceHardwareAddress
    }
}