package com.acorn.skynet.device.grpc.protocol.lifecycle.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: device_lifecycle.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class DeviceLifecycleGrpc {

  private DeviceLifecycleGrpc() {}

  public static final java.lang.String SERVICE_NAME = "DeviceLifecycle";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame,
      com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame> getLifecycleMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Lifecycle",
      requestType = com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame.class,
      responseType = com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame,
      com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame> getLifecycleMethod() {
    io.grpc.MethodDescriptor<com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame, com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame> getLifecycleMethod;
    if ((getLifecycleMethod = DeviceLifecycleGrpc.getLifecycleMethod) == null) {
      synchronized (DeviceLifecycleGrpc.class) {
        if ((getLifecycleMethod = DeviceLifecycleGrpc.getLifecycleMethod) == null) {
          DeviceLifecycleGrpc.getLifecycleMethod = getLifecycleMethod =
              io.grpc.MethodDescriptor.<com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame, com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Lifecycle"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame.getDefaultInstance()))
              .setSchemaDescriptor(new DeviceLifecycleMethodDescriptorSupplier("Lifecycle"))
              .build();
        }
      }
    }
    return getLifecycleMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DeviceLifecycleStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DeviceLifecycleStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DeviceLifecycleStub>() {
        @java.lang.Override
        public DeviceLifecycleStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DeviceLifecycleStub(channel, callOptions);
        }
      };
    return DeviceLifecycleStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DeviceLifecycleBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DeviceLifecycleBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DeviceLifecycleBlockingStub>() {
        @java.lang.Override
        public DeviceLifecycleBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DeviceLifecycleBlockingStub(channel, callOptions);
        }
      };
    return DeviceLifecycleBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DeviceLifecycleFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DeviceLifecycleFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DeviceLifecycleFutureStub>() {
        @java.lang.Override
        public DeviceLifecycleFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DeviceLifecycleFutureStub(channel, callOptions);
        }
      };
    return DeviceLifecycleFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default io.grpc.stub.StreamObserver<com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame> lifecycle(
        io.grpc.stub.StreamObserver<com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getLifecycleMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service DeviceLifecycle.
   */
  public static abstract class DeviceLifecycleImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return DeviceLifecycleGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service DeviceLifecycle.
   */
  public static final class DeviceLifecycleStub
      extends io.grpc.stub.AbstractAsyncStub<DeviceLifecycleStub> {
    private DeviceLifecycleStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DeviceLifecycleStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DeviceLifecycleStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame> lifecycle(
        io.grpc.stub.StreamObserver<com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getLifecycleMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service DeviceLifecycle.
   */
  public static final class DeviceLifecycleBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<DeviceLifecycleBlockingStub> {
    private DeviceLifecycleBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DeviceLifecycleBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DeviceLifecycleBlockingStub(channel, callOptions);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service DeviceLifecycle.
   */
  public static final class DeviceLifecycleFutureStub
      extends io.grpc.stub.AbstractFutureStub<DeviceLifecycleFutureStub> {
    private DeviceLifecycleFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DeviceLifecycleFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DeviceLifecycleFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_LIFECYCLE = 0;

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
        case METHODID_LIFECYCLE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.lifecycle(
              (io.grpc.stub.StreamObserver<com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getLifecycleMethod(),
          io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
            new MethodHandlers<
              com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame,
              com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame>(
                service, METHODID_LIFECYCLE)))
        .build();
  }

  private static abstract class DeviceLifecycleBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DeviceLifecycleBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DeviceLifecycle");
    }
  }

  private static final class DeviceLifecycleFileDescriptorSupplier
      extends DeviceLifecycleBaseDescriptorSupplier {
    DeviceLifecycleFileDescriptorSupplier() {}
  }

  private static final class DeviceLifecycleMethodDescriptorSupplier
      extends DeviceLifecycleBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    DeviceLifecycleMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (DeviceLifecycleGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DeviceLifecycleFileDescriptorSupplier())
              .addMethod(getLifecycleMethod())
              .build();
        }
      }
    }
    return result;
  }
}
