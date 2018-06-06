package io.dmendezg

import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import java.util.logging.Logger

fun main(args: Array<String>) {
    val server = GRPCServer
    server.start()
    server.blockUntilShutdown()
}

object GRPCServer {

    private var server: Server? = null
    private val logger = Logger.getLogger(GRPCServer::class.java.name)

    fun start() {
        val port = 50051
        server = ServerBuilder.forPort(port)
            .addService(HelloWorldServiceImpl())
            .build()
            .start()
        logger.info("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                logger.info("*** shutting down gRPC server since JVM is shutting down")
                this@GRPCServer.stop()
                logger.info("*** server shut down")
            }
        })
    }

    fun stop() = server?.shutdown()
    fun blockUntilShutdown() = server?.awaitTermination()

}

class HelloWorldServiceImpl : HelloWorldServiceGrpc.HelloWorldServiceImplBase() {

    override fun execute(request: HelloWorldRequest, responseObserver: StreamObserver<HelloWorldResponse>) {
        val response = HelloWorldResponse.newBuilder()
            .setOutput(request.input.toUpperCase()).build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

}