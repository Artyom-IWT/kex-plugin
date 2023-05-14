package org.vorpal.research.kex.plugin.net

import kotlinx.coroutines.delay
import java.net.ServerSocket
import java.util.function.Predicate

fun findFreePort(): Int {
    ServerSocket(0).use {
        it.reuseAddress = true
        return it.localPort
    }
}

suspend fun getConnectedClient(
    port: Int, timeout: Long,
    reconnectPause: Long = 2000,
    stopPredicate: Predicate<Client> = Predicate { it.isConnected }
): Client {
    val client = Client(port = port)

    var status = false
    var timeElapsed: Long = 0
    val startTime = System.currentTimeMillis()

    while (timeElapsed < timeout) {
        client.connect()
        if (stopPredicate.test(client)) {
            status = true
            break
        }
        delay(reconnectPause)
        timeElapsed = System.currentTimeMillis() - startTime
    }

    if (status && client.isConnected) return client
    else throw ConnectionFailedException()
}

class ConnectionFailedException : Exception()