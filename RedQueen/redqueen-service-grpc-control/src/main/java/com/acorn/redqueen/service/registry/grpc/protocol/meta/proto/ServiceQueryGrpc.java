package com.acorn.redqueen.service.registry.grpc.protocol.meta.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: service_query.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ServiceQueryGrpc {

  private ServiceQueryGrpc() {}

  public static final java.lang.String SERVICE_NAME = "ServiceQuery";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServicePageRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServicePageReply> getFetchServicePageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "FetchServicePage",
      requestType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServicePageRequest.class,
      responseType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServicePageReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServicePageRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServicePageReply> getFetchServicePageMethod() {
    io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServicePageRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServicePageReply> getFetchServicePageMethod;
    if ((getFetchServicePageMethod = ServiceQueryGrpc.getFetchServicePageMethod) == null) {
      synchronized (ServiceQueryGrpc.class) {
        if ((getFetchServicePageMethod = ServiceQueryGrpc.getFetchServicePageMethod) == null) {
          ServiceQueryGrpc.getFetchServicePageMethod = getFetchServicePageMethod =
              io.grpc.MethodDescriptor.<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServicePageRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServicePageReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "FetchServicePage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServicePageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServicePageReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceQueryMethodDescriptorSupplier("FetchServicePage"))
              .build();
        }
      }
    }
    return getFetchServicePageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInstancePageRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstancePageReply> getFetchServiceInstancePageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "FetchServiceInstancePage",
      requestType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInstancePageRequest.class,
      responseType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstancePageReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInstancePageRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstancePageReply> getFetchServiceInstancePageMethod() {
    io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInstancePageRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstancePageReply> getFetchServiceInstancePageMethod;
    if ((getFetchServiceInstancePageMethod = ServiceQueryGrpc.getFetchServiceInstancePageMethod) == null) {
      synchronized (ServiceQueryGrpc.class) {
        if ((getFetchServiceInstancePageMethod = ServiceQueryGrpc.getFetchServiceInstancePageMethod) == null) {
          ServiceQueryGrpc.getFetchServiceInstancePageMethod = getFetchServiceInstancePageMethod =
              io.grpc.MethodDescriptor.<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInstancePageRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstancePageReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "FetchServiceInstancePage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInstancePageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstancePageReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceQueryMethodDescriptorSupplier("FetchServiceInstancePage"))
              .build();
        }
      }
    }
    return getFetchServiceInstancePageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceReply> getQueryServiceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "QueryService",
      requestType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceRequest.class,
      responseType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceReply> getQueryServiceMethod() {
    io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceReply> getQueryServiceMethod;
    if ((getQueryServiceMethod = ServiceQueryGrpc.getQueryServiceMethod) == null) {
      synchronized (ServiceQueryGrpc.class) {
        if ((getQueryServiceMethod = ServiceQueryGrpc.getQueryServiceMethod) == null) {
          ServiceQueryGrpc.getQueryServiceMethod = getQueryServiceMethod =
              io.grpc.MethodDescriptor.<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "QueryService"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceQueryMethodDescriptorSupplier("QueryService"))
              .build();
        }
      }
    }
    return getQueryServiceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceInstanceRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstanceReply> getQueryServiceInstanceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "QueryServiceInstance",
      requestType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceInstanceRequest.class,
      responseType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstanceReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceInstanceRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstanceReply> getQueryServiceInstanceMethod() {
    io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceInstanceRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstanceReply> getQueryServiceInstanceMethod;
    if ((getQueryServiceInstanceMethod = ServiceQueryGrpc.getQueryServiceInstanceMethod) == null) {
      synchronized (ServiceQueryGrpc.class) {
        if ((getQueryServiceInstanceMethod = ServiceQueryGrpc.getQueryServiceInstanceMethod) == null) {
          ServiceQueryGrpc.getQueryServiceInstanceMethod = getQueryServiceInstanceMethod =
              io.grpc.MethodDescriptor.<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceInstanceRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstanceReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "QueryServiceInstance"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceInstanceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstanceReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceQueryMethodDescriptorSupplier("QueryServiceInstance"))
              .build();
        }
      }
    }
    return getQueryServiceInstanceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchNamespaceChildrenRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.NamespaceChildrenReply> getFetchNamespaceChildrenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "FetchNamespaceChildren",
      requestType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchNamespaceChildrenRequest.class,
      responseType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.NamespaceChildrenReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchNamespaceChildrenRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.NamespaceChildrenReply> getFetchNamespaceChildrenMethod() {
    io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchNamespaceChildrenRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.NamespaceChildrenReply> getFetchNamespaceChildrenMethod;
    if ((getFetchNamespaceChildrenMethod = ServiceQueryGrpc.getFetchNamespaceChildrenMethod) == null) {
      synchronized (ServiceQueryGrpc.class) {
        if ((getFetchNamespaceChildrenMethod = ServiceQueryGrpc.getFetchNamespaceChildrenMethod) == null) {
          ServiceQueryGrpc.getFetchNamespaceChildrenMethod = getFetchNamespaceChildrenMethod =
              io.grpc.MethodDescriptor.<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchNamespaceChildrenRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.NamespaceChildrenReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "FetchNamespaceChildren"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchNamespaceChildrenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.NamespaceChildrenReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceQueryMethodDescriptorSupplier("FetchNamespaceChildren"))
              .build();
        }
      }
    }
    return getFetchNamespaceChildrenMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ServiceQueryStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ServiceQueryStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ServiceQueryStub>() {
        @java.lang.Override
        public ServiceQueryStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ServiceQueryStub(channel, callOptions);
        }
      };
    return ServiceQueryStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ServiceQueryBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ServiceQueryBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ServiceQueryBlockingStub>() {
        @java.lang.Override
        public ServiceQueryBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ServiceQueryBlockingStub(channel, callOptions);
        }
      };
    return ServiceQueryBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ServiceQueryFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ServiceQueryFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ServiceQueryFutureStub>() {
        @java.lang.Override
        public ServiceQueryFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ServiceQueryFutureStub(channel, callOptions);
        }
      };
    return ServiceQueryFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void fetchServicePage(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServicePageRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServicePageReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFetchServicePageMethod(), responseObserver);
    }

    /**
     */
    default void fetchServiceInstancePage(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInstancePageRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstancePageReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFetchServiceInstancePageMethod(), responseObserver);
    }

    /**
     */
    default void queryService(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getQueryServiceMethod(), responseObserver);
    }

    /**
     */
    default void queryServiceInstance(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceInstanceRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstanceReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getQueryServiceInstanceMethod(), responseObserver);
    }

    /**
     */
    default void fetchNamespaceChildren(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchNamespaceChildrenRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.NamespaceChildrenReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFetchNamespaceChildrenMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ServiceQuery.
   */
  public static abstract class ServiceQueryImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ServiceQueryGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ServiceQuery.
   */
  public static final class ServiceQueryStub
      extends io.grpc.stub.AbstractAsyncStub<ServiceQueryStub> {
    private ServiceQueryStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceQueryStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ServiceQueryStub(channel, callOptions);
    }

    /**
     */
    public void fetchServicePage(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServicePageRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServicePageReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFetchServicePageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void fetchServiceInstancePage(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInstancePageRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstancePageReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFetchServiceInstancePageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void queryService(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getQueryServiceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void queryServiceInstance(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceInstanceRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstanceReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getQueryServiceInstanceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void fetchNamespaceChildren(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchNamespaceChildrenRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.NamespaceChildrenReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFetchNamespaceChildrenMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ServiceQuery.
   */
  public static final class ServiceQueryBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ServiceQueryBlockingStub> {
    private ServiceQueryBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceQueryBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ServiceQueryBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServicePageReply fetchServicePage(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServicePageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFetchServicePageMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstancePageReply fetchServiceInstancePage(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInstancePageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFetchServiceInstancePageMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceReply queryService(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getQueryServiceMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstanceReply queryServiceInstance(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceInstanceRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getQueryServiceInstanceMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.NamespaceChildrenReply fetchNamespaceChildren(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchNamespaceChildrenRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFetchNamespaceChildrenMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ServiceQuery.
   */
  public static final class ServiceQueryFutureStub
      extends io.grpc.stub.AbstractFutureStub<ServiceQueryFutureStub> {
    private ServiceQueryFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceQueryFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ServiceQueryFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServicePageReply> fetchServicePage(
        com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServicePageRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFetchServicePageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstancePageReply> fetchServiceInstancePage(
        com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInstancePageRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFetchServiceInstancePageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceReply> queryService(
        com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getQueryServiceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstanceReply> queryServiceInstance(
        com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceInstanceRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getQueryServiceInstanceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.NamespaceChildrenReply> fetchNamespaceChildren(
        com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchNamespaceChildrenRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFetchNamespaceChildrenMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_FETCH_SERVICE_PAGE = 0;
  private static final int METHODID_FETCH_SERVICE_INSTANCE_PAGE = 1;
  private static final int METHODID_QUERY_SERVICE = 2;
  private static final int METHODID_QUERY_SERVICE_INSTANCE = 3;
  private static final int METHODID_FETCH_NAMESPACE_CHILDREN = 4;

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
        case METHODID_FETCH_SERVICE_PAGE:
          serviceImpl.fetchServicePage((com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServicePageRequest) request,
              (io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServicePageReply>) responseObserver);
          break;
        case METHODID_FETCH_SERVICE_INSTANCE_PAGE:
          serviceImpl.fetchServiceInstancePage((com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInstancePageRequest) request,
              (io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstancePageReply>) responseObserver);
          break;
        case METHODID_QUERY_SERVICE:
          serviceImpl.queryService((com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceRequest) request,
              (io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceReply>) responseObserver);
          break;
        case METHODID_QUERY_SERVICE_INSTANCE:
          serviceImpl.queryServiceInstance((com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceInstanceRequest) request,
              (io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstanceReply>) responseObserver);
          break;
        case METHODID_FETCH_NAMESPACE_CHILDREN:
          serviceImpl.fetchNamespaceChildren((com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchNamespaceChildrenRequest) request,
              (io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.NamespaceChildrenReply>) responseObserver);
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
          getFetchServicePageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServicePageRequest,
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServicePageReply>(
                service, METHODID_FETCH_SERVICE_PAGE)))
        .addMethod(
          getFetchServiceInstancePageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInstancePageRequest,
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstancePageReply>(
                service, METHODID_FETCH_SERVICE_INSTANCE_PAGE)))
        .addMethod(
          getQueryServiceMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceRequest,
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceReply>(
                service, METHODID_QUERY_SERVICE)))
        .addMethod(
          getQueryServiceInstanceMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceInstanceRequest,
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstanceReply>(
                service, METHODID_QUERY_SERVICE_INSTANCE)))
        .addMethod(
          getFetchNamespaceChildrenMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchNamespaceChildrenRequest,
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.NamespaceChildrenReply>(
                service, METHODID_FETCH_NAMESPACE_CHILDREN)))
        .build();
  }

  private static abstract class ServiceQueryBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ServiceQueryBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceQueryProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ServiceQuery");
    }
  }

  private static final class ServiceQueryFileDescriptorSupplier
      extends ServiceQueryBaseDescriptorSupplier {
    ServiceQueryFileDescriptorSupplier() {}
  }

  private static final class ServiceQueryMethodDescriptorSupplier
      extends ServiceQueryBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ServiceQueryMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (ServiceQueryGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ServiceQueryFileDescriptorSupplier())
              .addMethod(getFetchServicePageMethod())
              .addMethod(getFetchServiceInstancePageMethod())
              .addMethod(getQueryServiceMethod())
              .addMethod(getQueryServiceInstanceMethod())
              .addMethod(getFetchNamespaceChildrenMethod())
              .build();
        }
      }
    }
    return result;
  }
}



