package com.walnut.odin.proc.server.transport.grpc.lifecycle;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: remote_process_lifecycle.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class RemoteProcessLifecycleGrpc {

  private RemoteProcessLifecycleGrpc() {}

  public static final java.lang.String SERVICE_NAME = "walnut.odin.proc.control.lifecycle.RemoteProcessLifecycle";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame,
      com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame> getControlMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Control",
      requestType = com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame.class,
      responseType = com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame,
      com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame> getControlMethod() {
    io.grpc.MethodDescriptor<com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame, com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame> getControlMethod;
    if ((getControlMethod = RemoteProcessLifecycleGrpc.getControlMethod) == null) {
      synchronized (RemoteProcessLifecycleGrpc.class) {
        if ((getControlMethod = RemoteProcessLifecycleGrpc.getControlMethod) == null) {
          RemoteProcessLifecycleGrpc.getControlMethod = getControlMethod =
              io.grpc.MethodDescriptor.<com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame, com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Control"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame.getDefaultInstance()))
              .setSchemaDescriptor(new RemoteProcessLifecycleMethodDescriptorSupplier("Control"))
              .build();
        }
      }
    }
    return getControlMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RemoteProcessLifecycleStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RemoteProcessLifecycleStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RemoteProcessLifecycleStub>() {
        @java.lang.Override
        public RemoteProcessLifecycleStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RemoteProcessLifecycleStub(channel, callOptions);
        }
      };
    return RemoteProcessLifecycleStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RemoteProcessLifecycleBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RemoteProcessLifecycleBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RemoteProcessLifecycleBlockingStub>() {
        @java.lang.Override
        public RemoteProcessLifecycleBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RemoteProcessLifecycleBlockingStub(channel, callOptions);
        }
      };
    return RemoteProcessLifecycleBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RemoteProcessLifecycleFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RemoteProcessLifecycleFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RemoteProcessLifecycleFutureStub>() {
        @java.lang.Override
        public RemoteProcessLifecycleFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RemoteProcessLifecycleFutureStub(channel, callOptions);
        }
      };
    return RemoteProcessLifecycleFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default io.grpc.stub.StreamObserver<com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame> control(
        io.grpc.stub.StreamObserver<com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getControlMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service RemoteProcessLifecycle.
   */
  public static abstract class RemoteProcessLifecycleImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return RemoteProcessLifecycleGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service RemoteProcessLifecycle.
   */
  public static final class RemoteProcessLifecycleStub
      extends io.grpc.stub.AbstractAsyncStub<RemoteProcessLifecycleStub> {
    private RemoteProcessLifecycleStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RemoteProcessLifecycleStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RemoteProcessLifecycleStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame> control(
        io.grpc.stub.StreamObserver<com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getControlMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service RemoteProcessLifecycle.
   */
  public static final class RemoteProcessLifecycleBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<RemoteProcessLifecycleBlockingStub> {
    private RemoteProcessLifecycleBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RemoteProcessLifecycleBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RemoteProcessLifecycleBlockingStub(channel, callOptions);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service RemoteProcessLifecycle.
   */
  public static final class RemoteProcessLifecycleFutureStub
      extends io.grpc.stub.AbstractFutureStub<RemoteProcessLifecycleFutureStub> {
    private RemoteProcessLifecycleFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RemoteProcessLifecycleFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RemoteProcessLifecycleFutureStub(channel, callOptions);
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
              (io.grpc.stub.StreamObserver<com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame>) responseObserver);
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
              com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame,
              com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame>(
                service, METHODID_CONTROL)))
        .build();
  }

  private static abstract class RemoteProcessLifecycleBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RemoteProcessLifecycleBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessLifecycleProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RemoteProcessLifecycle");
    }
  }

  private static final class RemoteProcessLifecycleFileDescriptorSupplier
      extends RemoteProcessLifecycleBaseDescriptorSupplier {
    RemoteProcessLifecycleFileDescriptorSupplier() {}
  }

  private static final class RemoteProcessLifecycleMethodDescriptorSupplier
      extends RemoteProcessLifecycleBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    RemoteProcessLifecycleMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (RemoteProcessLifecycleGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RemoteProcessLifecycleFileDescriptorSupplier())
              .addMethod(getControlMethod())
              .build();
        }
      }
    }
    return result;
  }
}
