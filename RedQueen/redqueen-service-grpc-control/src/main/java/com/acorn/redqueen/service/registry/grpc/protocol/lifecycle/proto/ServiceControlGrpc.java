package com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: service_control.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ServiceControlGrpc {

  private ServiceControlGrpc() {}

  public static final java.lang.String SERVICE_NAME = "ServiceControl";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame,
      com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame> getControlMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Control",
      requestType = com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame.class,
      responseType = com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame,
      com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame> getControlMethod() {
    io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame, com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame> getControlMethod;
    if ((getControlMethod = ServiceControlGrpc.getControlMethod) == null) {
      synchronized (ServiceControlGrpc.class) {
        if ((getControlMethod = ServiceControlGrpc.getControlMethod) == null) {
          ServiceControlGrpc.getControlMethod = getControlMethod =
              io.grpc.MethodDescriptor.<com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame, com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Control"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceControlMethodDescriptorSupplier("Control"))
              .build();
        }
      }
    }
    return getControlMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ServiceControlStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ServiceControlStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ServiceControlStub>() {
        @java.lang.Override
        public ServiceControlStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ServiceControlStub(channel, callOptions);
        }
      };
    return ServiceControlStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ServiceControlBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ServiceControlBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ServiceControlBlockingStub>() {
        @java.lang.Override
        public ServiceControlBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ServiceControlBlockingStub(channel, callOptions);
        }
      };
    return ServiceControlBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ServiceControlFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ServiceControlFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ServiceControlFutureStub>() {
        @java.lang.Override
        public ServiceControlFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ServiceControlFutureStub(channel, callOptions);
        }
      };
    return ServiceControlFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame> control(
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getControlMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ServiceControl.
   */
  public static abstract class ServiceControlImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ServiceControlGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ServiceControl.
   */
  public static final class ServiceControlStub
      extends io.grpc.stub.AbstractAsyncStub<ServiceControlStub> {
    private ServiceControlStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceControlStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ServiceControlStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame> control(
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getControlMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ServiceControl.
   */
  public static final class ServiceControlBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ServiceControlBlockingStub> {
    private ServiceControlBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceControlBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ServiceControlBlockingStub(channel, callOptions);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ServiceControl.
   */
  public static final class ServiceControlFutureStub
      extends io.grpc.stub.AbstractFutureStub<ServiceControlFutureStub> {
    private ServiceControlFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceControlFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ServiceControlFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_CONTROL = 0;

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
        case METHODID_CONTROL:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.control(
              (io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getControlMethod(),
          io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
            new MethodHandlers<
              com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame,
              com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame>(
                service, METHODID_CONTROL)))
        .build();
  }

  private static abstract class ServiceControlBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ServiceControlBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ServiceControl");
    }
  }

  private static final class ServiceControlFileDescriptorSupplier
      extends ServiceControlBaseDescriptorSupplier {
    ServiceControlFileDescriptorSupplier() {}
  }

  private static final class ServiceControlMethodDescriptorSupplier
      extends ServiceControlBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ServiceControlMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (ServiceControlGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ServiceControlFileDescriptorSupplier())
              .addMethod(getControlMethod())
              .build();
        }
      }
    }
    return result;
  }
}
