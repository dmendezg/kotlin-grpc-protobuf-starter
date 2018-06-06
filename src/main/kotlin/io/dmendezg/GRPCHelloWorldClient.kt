package io.dmendezg

import io.grpc.ManagedChannelBuilder
import java.util.concurrent.TimeUnit
import java.util.logging.Logger


fun main(args: Array<String>) {
    val client = GRPCClient()
    client.execute()
    client.shutdown()
}

class GRPCClient(private val host: String = "localhost", private val port: Int = 50051) {

    private val logger = Logger.getLogger(GRPCClient::class.java.name)
    private val channel = ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext(true)
        .build()
    private val blockingStub = HelloWorldServiceGrpc.newBlockingStub(channel)

    fun execute() {
        val input = "hello world"
        val request = HelloWorldRequest.newBuilder().setInput(input).build()
        logger.info("Input: $input")
        val response = blockingStub.execute(request)
        logger.info("Output: ${response.output}")
    }

    fun shutdown() = channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)

}