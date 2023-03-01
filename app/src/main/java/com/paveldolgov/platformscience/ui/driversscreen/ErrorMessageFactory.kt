package com.paveldolgov.platformscience.ui.driversscreen

import android.content.res.Resources
import com.paveldolgov.platformscience.R
import com.paveldolgov.platformscience.model.DriversAndShipmentsError
import javax.inject.Inject

class ErrorMessageFactory @Inject constructor(private val resources: Resources) {

    fun getErrorMessage(error: DriversAndShipmentsError): String {
        return when (error) {
            DriversAndShipmentsError.NonRetryable -> resources.getString(R.string.non_retryable_error_message)
            DriversAndShipmentsError.Retryable -> resources.getString(R.string.retryable_error_message)
        }
    }
}