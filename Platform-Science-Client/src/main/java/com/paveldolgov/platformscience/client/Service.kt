package com.paveldolgov.platformscience.client

import arrow.core.Either
import com.paveldolgov.platformscience.client.model.DriversAndShipmentsResponse
import com.paveldolgov.platformscience.client.model.error.ShipmentRequestError
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.IOException

internal class Service constructor(
    private val moshi: Moshi,
    private val dispatcher: CoroutineDispatcher
) : ServiceApi {
    companion object {
        const val JSON_FILE_NAME = "drivers_shipments.json"
    }

    override suspend fun getShipmentsAndDrivers(): Either<ShipmentRequestError, DriversAndShipmentsResponse> {
        return withContext(dispatcher) {
            // small delay to emulate heavy IO operation
            delay(1000)
            ensureActive()

            try {
                val inputStream =
                    javaClass.classLoader.getResourceAsStream(JSON_FILE_NAME)
                        ?: return@withContext Either.Left(ShipmentRequestError.IOException("Json file $JSON_FILE_NAME not found"))
                val jsonString = String(inputStream.readBytes(), Charsets.UTF_8)

                ensureActive()
                val driversAndShipmentsResponseAdapter =
                    moshi.adapter(DriversAndShipmentsResponse::class.java)
                val response =
                    driversAndShipmentsResponseAdapter.fromJson(jsonString)!!
                Either.Right(response)
            } catch (e: IOException) {
                Either.Left(ShipmentRequestError.IOException(e.message ?: "IOException occurred"))
            } catch (e: JsonDataException) {
                Either.Left(
                    ShipmentRequestError.JsonException(
                        e.message ?: "JsonDataException occurred"
                    )
                )
            }
        }
    }
}