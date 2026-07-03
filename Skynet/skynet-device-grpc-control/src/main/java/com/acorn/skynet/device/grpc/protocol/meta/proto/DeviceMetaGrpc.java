package com.acorn.skynet.device.grpc.protocol.meta.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: device_meta.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class DeviceMetaGrpc {

  private DeviceMetaGrpc() {}

  public static final java.lang.String SERVICE_NAME = "DeviceMeta";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByPathRequest,
      com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply> getQueryDeviceMetaByPathMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "QueryDeviceMetaByPath",
      requestType = com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByPathRequest.class,
      responseType = com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByPathRequest,
      com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply> getQueryDeviceMetaByPathMethod() {
    io.grpc.MethodDescriptor<com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByPathRequest, com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply> getQueryDeviceMetaByPathMethod;
    if ((getQueryDeviceMetaByPathMethod = DeviceMetaGrpc.getQueryDeviceMetaByPathMethod) == null) {
      synchronized (DeviceMetaGrpc.class) {
        if ((getQueryDeviceMetaByPathMethod = DeviceMetaGrpc.getQueryDeviceMetaByPathMethod) == null) {
          DeviceMetaGrpc.getQueryDeviceMetaByPathMethod = getQueryDeviceMetaByPathMethod =
              io.grpc.MethodDescriptor.<com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByPathRequest, com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "QueryDeviceMetaByPath"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByPathRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply.getDefaultInstance()))
              .setSchemaDescriptor(new DeviceMetaMethodDescriptorSupplier("QueryDeviceMetaByPath"))
              .build();
        }
      }
    }
    return getQueryDeviceMetaByPathMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByGuidRequest,
      com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply> getQueryDeviceMetaByGuidMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "QueryDeviceMetaByGuid",
      requestType = com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByGuidRequest.class,
      responseType = com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByGuidRequest,
      com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply> getQueryDeviceMetaByGuidMethod() {
    io.grpc.MethodDescriptor<com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByGuidRequest, com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply> getQueryDeviceMetaByGuidMethod;
    if ((getQueryDeviceMetaByGuidMethod = DeviceMetaGrpc.getQueryDeviceMetaByGuidMethod) == null) {
      synchronized (DeviceMetaGrpc.class) {
        if ((getQueryDeviceMetaByGuidMethod = DeviceMetaGrpc.getQueryDeviceMetaByGuidMethod) == null) {
          DeviceMetaGrpc.getQueryDeviceMetaByGuidMethod = getQueryDeviceMetaByGuidMethod =
              io.grpc.MethodDescriptor.<com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByGuidRequest, com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "QueryDeviceMetaByGuid"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByGuidRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply.getDefaultInstance()))
              .setSchemaDescriptor(new DeviceMetaMethodDescriptorSupplier("QueryDeviceMetaByGuid"))
              .build();
        }
      }
    }
    return getQueryDeviceMetaByGuidMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByPathRequest,
      com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply> getUpdateDeviceMetaByPathMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateDeviceMetaByPath",
      requestType = com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByPathRequest.class,
      responseType = com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByPathRequest,
      com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply> getUpdateDeviceMetaByPathMethod() {
    io.grpc.MethodDescriptor<com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByPathRequest, com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply> getUpdateDeviceMetaByPathMethod;
    if ((getUpdateDeviceMetaByPathMethod = DeviceMetaGrpc.getUpdateDeviceMetaByPathMethod) == null) {
      synchronized (DeviceMetaGrpc.class) {
        if ((getUpdateDeviceMetaByPathMethod = DeviceMetaGrpc.getUpdateDeviceMetaByPathMethod) == null) {
          DeviceMetaGrpc.getUpdateDeviceMetaByPathMethod = getUpdateDeviceMetaByPathMethod =
              io.grpc.MethodDescriptor.<com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByPathRequest, com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateDeviceMetaByPath"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByPathRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply.getDefaultInstance()))
              .setSchemaDescriptor(new DeviceMetaMethodDescriptorSupplier("UpdateDeviceMetaByPath"))
              .build();
        }
      }
    }
    return getUpdateDeviceMetaByPathMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByGuidRequest,
      com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply> getUpdateDeviceMetaByGuidMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateDeviceMetaByGuid",
      requestType = com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByGuidRequest.class,
      responseType = com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByGuidRequest,
      com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply> getUpdateDeviceMetaByGuidMethod() {
    io.grpc.MethodDescriptor<com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByGuidRequest, com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply> getUpdateDeviceMetaByGuidMethod;
    if ((getUpdateDeviceMetaByGuidMethod = DeviceMetaGrpc.getUpdateDeviceMetaByGuidMethod) == null) {
      synchronized (DeviceMetaGrpc.class) {
        if ((getUpdateDeviceMetaByGuidMethod = DeviceMetaGrpc.getUpdateDeviceMetaByGuidMethod) == null) {
          DeviceMetaGrpc.getUpdateDeviceMetaByGuidMethod = getUpdateDeviceMetaByGuidMethod =
              io.grpc.MethodDescriptor.<com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByGuidRequest, com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateDeviceMetaByGuid"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByGuidRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply.getDefaultInstance()))
              .setSchemaDescriptor(new DeviceMetaMethodDescriptorSupplier("UpdateDeviceMetaByGuid"))
              .build();
        }
      }
    }
    return getUpdateDeviceMetaByGuidMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DeviceMetaStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DeviceMetaStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DeviceMetaStub>() {
        @java.lang.Override
        public DeviceMetaStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DeviceMetaStub(channel, callOptions);
        }
      };
    return DeviceMetaStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DeviceMetaBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DeviceMetaBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DeviceMetaBlockingStub>() {
        @java.lang.Override
        public DeviceMetaBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DeviceMetaBlockingStub(channel, callOptions);
        }
      };
    return DeviceMetaBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DeviceMetaFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DeviceMetaFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DeviceMetaFutureStub>() {
        @java.lang.Override
        public DeviceMetaFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DeviceMetaFutureStub(channel, callOptions);
        }
      };
    return DeviceMetaFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void queryDeviceMetaByPath(com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByPathRequest request,
        io.grpc.stub.StreamObserver<com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getQueryDeviceMetaByPathMethod(), responseObserver);
    }

    /**
     */
    default void queryDeviceMetaByGuid(com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByGuidRequest request,
        io.grpc.stub.StreamObserver<com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getQueryDeviceMetaByGuidMethod(), responseObserver);
    }

    /**
     */
    default void updateDeviceMetaByPath(com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByPathRequest request,
        io.grpc.stub.StreamObserver<com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateDeviceMetaByPathMethod(), responseObserver);
    }

    /**
     */
    default void updateDeviceMetaByGuid(com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByGuidRequest request,
        io.grpc.stub.StreamObserver<com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateDeviceMetaByGuidMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service DeviceMeta.
   */
  public static abstract class DeviceMetaImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return DeviceMetaGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service DeviceMeta.
   */
  public static final class DeviceMetaStub
      extends io.grpc.stub.AbstractAsyncStub<DeviceMetaStub> {
    private DeviceMetaStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DeviceMetaStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DeviceMetaStub(channel, callOptions);
    }

    /**
     */
    public void queryDeviceMetaByPath(com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByPathRequest request,
        io.grpc.stub.StreamObserver<com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getQueryDeviceMetaByPathMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void queryDeviceMetaByGuid(com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByGuidRequest request,
        io.grpc.stub.StreamObserver<com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getQueryDeviceMetaByGuidMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateDeviceMetaByPath(com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByPathRequest request,
        io.grpc.stub.StreamObserver<com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateDeviceMetaByPathMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateDeviceMetaByGuid(com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByGuidRequest request,
        io.grpc.stub.StreamObserver<com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateDeviceMetaByGuidMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service DeviceMeta.
   */
  public static final class DeviceMetaBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<DeviceMetaBlockingStub> {
    private DeviceMetaBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DeviceMetaBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DeviceMetaBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply queryDeviceMetaByPath(com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByPathRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getQueryDeviceMetaByPathMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply queryDeviceMetaByGuid(com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByGuidRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getQueryDeviceMetaByGuidMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply updateDeviceMetaByPath(com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByPathRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateDeviceMetaByPathMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply updateDeviceMetaByGuid(com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByGuidRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateDeviceMetaByGuidMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service DeviceMeta.
   */
  public static final class DeviceMetaFutureStub
      extends io.grpc.stub.AbstractFutureStub<DeviceMetaFutureStub> {
    private DeviceMetaFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DeviceMetaFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DeviceMetaFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply> queryDeviceMetaByPath(
        com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByPathRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getQueryDeviceMetaByPathMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply> queryDeviceMetaByGuid(
        com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByGuidRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getQueryDeviceMetaByGuidMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply> updateDeviceMetaByPath(
        com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByPathRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateDeviceMetaByPathMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply> updateDeviceMetaByGuid(
        com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByGuidRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateDeviceMetaByGuidMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_QUERY_DEVICE_META_BY_PATH = 0;
  private static final int METHODID_QUERY_DEVICE_META_BY_GUID = 1;
  private static final int METHODID_UPDATE_DEVICE_META_BY_PATH = 2;
  private static final int METHODID_UPDATE_DEVICE_META_BY_GUID = 3;

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
        case METHODID_QUERY_DEVICE_META_BY_PATH:
          serviceImpl.queryDeviceMetaByPath((com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByPathRequest) request,
              (io.grpc.stub.StreamObserver<com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply>) responseObserver);
          break;
        case METHODID_QUERY_DEVICE_META_BY_GUID:
          serviceImpl.queryDeviceMetaByGuid((com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByGuidRequest) request,
              (io.grpc.stub.StreamObserver<com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply>) responseObserver);
          break;
        case METHODID_UPDATE_DEVICE_META_BY_PATH:
          serviceImpl.updateDeviceMetaByPath((com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByPathRequest) request,
              (io.grpc.stub.StreamObserver<com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply>) responseObserver);
          break;
        case METHODID_UPDATE_DEVICE_META_BY_GUID:
          serviceImpl.updateDeviceMetaByGuid((com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByGuidRequest) request,
              (io.grpc.stub.StreamObserver<com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply>) responseObserver);
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
          getQueryDeviceMetaByPathMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByPathRequest,
              com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply>(
                service, METHODID_QUERY_DEVICE_META_BY_PATH)))
        .addMethod(
          getQueryDeviceMetaByGuidMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByGuidRequest,
              com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply>(
                service, METHODID_QUERY_DEVICE_META_BY_GUID)))
        .addMethod(
          getUpdateDeviceMetaByPathMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByPathRequest,
              com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply>(
                service, METHODID_UPDATE_DEVICE_META_BY_PATH)))
        .addMethod(
          getUpdateDeviceMetaByGuidMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByGuidRequest,
              com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply>(
                service, METHODID_UPDATE_DEVICE_META_BY_GUID)))
        .build();
  }

  private static abstract class DeviceMetaBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DeviceMetaBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DeviceMeta");
    }
  }

  private static final class DeviceMetaFileDescriptorSupplier
      extends DeviceMetaBaseDescriptorSupplier {
    DeviceMetaFileDescriptorSupplier() {}
  }

  private static final class DeviceMetaMethodDescriptorSupplier
      extends DeviceMetaBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    DeviceMetaMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (DeviceMetaGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DeviceMetaFileDescriptorSupplier())
              .addMethod(getQueryDeviceMetaByPathMethod())
              .addMethod(getQueryDeviceMetaByGuidMethod())
              .addMethod(getUpdateDeviceMetaByPathMethod())
              .addMethod(getUpdateDeviceMetaByGuidMethod())
              .build();
        }
      }
    }
    return result;
  }
}
