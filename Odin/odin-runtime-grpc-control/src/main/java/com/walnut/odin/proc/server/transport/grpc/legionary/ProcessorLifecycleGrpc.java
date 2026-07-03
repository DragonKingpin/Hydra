package com.walnut.odin.proc.server.transport.grpc.legionary;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: processor_lifecycle.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ProcessorLifecycleGrpc {

  private ProcessorLifecycleGrpc() {}

  public static final java.lang.String SERVICE_NAME = "walnut.odin.proc.control.legionary.ProcessorLifecycle";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinRequest,
      com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinResponse> getJoinRegimentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "JoinRegiment",
      requestType = com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinRequest.class,
      responseType = com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinRequest,
      com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinResponse> getJoinRegimentMethod() {
    io.grpc.MethodDescriptor<com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinRequest, com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinResponse> getJoinRegimentMethod;
    if ((getJoinRegimentMethod = ProcessorLifecycleGrpc.getJoinRegimentMethod) == null) {
      synchronized (ProcessorLifecycleGrpc.class) {
        if ((getJoinRegimentMethod = ProcessorLifecycleGrpc.getJoinRegimentMethod) == null) {
          ProcessorLifecycleGrpc.getJoinRegimentMethod = getJoinRegimentMethod =
              io.grpc.MethodDescriptor.<com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinRequest, com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "JoinRegiment"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ProcessorLifecycleMethodDescriptorSupplier("JoinRegiment"))
              .build();
        }
      }
    }
    return getJoinRegimentMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ProcessorLifecycleStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ProcessorLifecycleStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ProcessorLifecycleStub>() {
        @java.lang.Override
        public ProcessorLifecycleStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ProcessorLifecycleStub(channel, callOptions);
        }
      };
    return ProcessorLifecycleStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ProcessorLifecycleBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ProcessorLifecycleBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ProcessorLifecycleBlockingStub>() {
        @java.lang.Override
        public ProcessorLifecycleBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ProcessorLifecycleBlockingStub(channel, callOptions);
        }
      };
    return ProcessorLifecycleBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ProcessorLifecycleFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ProcessorLifecycleFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ProcessorLifecycleFutureStub>() {
        @java.lang.Override
        public ProcessorLifecycleFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ProcessorLifecycleFutureStub(channel, callOptions);
        }
      };
    return ProcessorLifecycleFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void joinRegiment(com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinRequest request,
        io.grpc.stub.StreamObserver<com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getJoinRegimentMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ProcessorLifecycle.
   */
  public static abstract class ProcessorLifecycleImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ProcessorLifecycleGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ProcessorLifecycle.
   */
  public static final class ProcessorLifecycleStub
      extends io.grpc.stub.AbstractAsyncStub<ProcessorLifecycleStub> {
    private ProcessorLifecycleStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ProcessorLifecycleStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ProcessorLifecycleStub(channel, callOptions);
    }

    /**
     */
    public void joinRegiment(com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinRequest request,
        io.grpc.stub.StreamObserver<com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getJoinRegimentMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ProcessorLifecycle.
   */
  public static final class ProcessorLifecycleBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ProcessorLifecycleBlockingStub> {
    private ProcessorLifecycleBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ProcessorLifecycleBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ProcessorLifecycleBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinResponse joinRegiment(com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getJoinRegimentMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ProcessorLifecycle.
   */
  public static final class ProcessorLifecycleFutureStub
      extends io.grpc.stub.AbstractFutureStub<ProcessorLifecycleFutureStub> {
    private ProcessorLifecycleFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ProcessorLifecycleFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ProcessorLifecycleFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinResponse> joinRegiment(
        com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getJoinRegimentMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_JOIN_REGIMENT = 0;

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
        case METHODID_JOIN_REGIMENT:
          serviceImpl.joinRegiment((com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinRequest) request,
              (io.grpc.stub.StreamObserver<com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getJoinRegimentMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinRequest,
              com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinResponse>(
                service, METHODID_JOIN_REGIMENT)))
        .build();
  }

  private static abstract class ProcessorLifecycleBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ProcessorLifecycleBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.walnut.odin.proc.server.transport.grpc.legionary.ProcessorLifecycleProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ProcessorLifecycle");
    }
  }

  private static final class ProcessorLifecycleFileDescriptorSupplier
      extends ProcessorLifecycleBaseDescriptorSupplier {
    ProcessorLifecycleFileDescriptorSupplier() {}
  }

  private static final class ProcessorLifecycleMethodDescriptorSupplier
      extends ProcessorLifecycleBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ProcessorLifecycleMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (ProcessorLifecycleGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ProcessorLifecycleFileDescriptorSupplier())
              .addMethod(getJoinRegimentMethod())
              .build();
        }
      }
    }
    return result;
  }
}
