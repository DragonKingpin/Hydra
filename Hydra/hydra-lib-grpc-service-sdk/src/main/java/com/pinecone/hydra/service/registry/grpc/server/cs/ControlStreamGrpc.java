package com.pinecone.hydra.service.registry.grpc.server.cs;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: control_stream.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ControlStreamGrpc {

  private ControlStreamGrpc() {}

  public static final java.lang.String SERVICE_NAME = "ControlStream";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage,
      com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage> getConnectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Connect",
      requestType = com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage.class,
      responseType = com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage,
      com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage> getConnectMethod() {
    io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage, com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage> getConnectMethod;
    if ((getConnectMethod = ControlStreamGrpc.getConnectMethod) == null) {
      synchronized (ControlStreamGrpc.class) {
        if ((getConnectMethod = ControlStreamGrpc.getConnectMethod) == null) {
          ControlStreamGrpc.getConnectMethod = getConnectMethod =
              io.grpc.MethodDescriptor.<com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage, com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Connect"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage.getDefaultInstance()))
              .setSchemaDescriptor(new ControlStreamMethodDescriptorSupplier("Connect"))
              .build();
        }
      }
    }
    return getConnectMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ControlStreamStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ControlStreamStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ControlStreamStub>() {
        @java.lang.Override
        public ControlStreamStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ControlStreamStub(channel, callOptions);
        }
      };
    return ControlStreamStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ControlStreamBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ControlStreamBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ControlStreamBlockingStub>() {
        @java.lang.Override
        public ControlStreamBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ControlStreamBlockingStub(channel, callOptions);
        }
      };
    return ControlStreamBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ControlStreamFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ControlStreamFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ControlStreamFutureStub>() {
        @java.lang.Override
        public ControlStreamFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ControlStreamFutureStub(channel, callOptions);
        }
      };
    return ControlStreamFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage> connect(
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getConnectMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ControlStream.
   */
  public static abstract class ControlStreamImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ControlStreamGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ControlStream.
   */
  public static final class ControlStreamStub
      extends io.grpc.stub.AbstractAsyncStub<ControlStreamStub> {
    private ControlStreamStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ControlStreamStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ControlStreamStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage> connect(
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getConnectMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ControlStream.
   */
  public static final class ControlStreamBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ControlStreamBlockingStub> {
    private ControlStreamBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ControlStreamBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ControlStreamBlockingStub(channel, callOptions);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ControlStream.
   */
  public static final class ControlStreamFutureStub
      extends io.grpc.stub.AbstractFutureStub<ControlStreamFutureStub> {
    private ControlStreamFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ControlStreamFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ControlStreamFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_CONNECT = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CONNECT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.connect(
              (io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getConnectMethod(),
          io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
            new MethodHandlers<
              com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage,
              com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage>(
                service, METHODID_CONNECT)))
        .build();
  }

  private static abstract class ControlStreamBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ControlStreamBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.pinecone.hydra.service.registry.grpc.server.cs.ControlStreamOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ControlStream");
    }
  }

  private static final class ControlStreamFileDescriptorSupplier
      extends ControlStreamBaseDescriptorSupplier {
    ControlStreamFileDescriptorSupplier() {}
  }

  private static final class ControlStreamMethodDescriptorSupplier
      extends ControlStreamBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ControlStreamMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ControlStreamGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ControlStreamFileDescriptorSupplier())
              .addMethod(getConnectMethod())
              .build();
        }
      }
    }
    return result;
  }
}
