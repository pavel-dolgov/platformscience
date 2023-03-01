package com.paveldolgov.platformscience.client

import kotlinx.coroutines.Dispatchers

class Client private constructor(service: Service) : ServiceApi by service {

    class Builder {
        fun build(): Client {
            val factory = ClientFactory()
            val service = Service(factory.createMoshi(), Dispatchers.IO)
            return Client(service)
        }
    }
}