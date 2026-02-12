package com.pinecone.hydra.service.registry.grpc.server.meta;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: service_meta.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ServiceMetaGrpc {

  private ServiceMetaGrpc() {}

  public static final java.lang.String SERVICE_NAME = "pinecone.meta.ServiceMeta";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.meta.ClientIdRequest,
      com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply> getFetchServiceInsMetaByClientIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "FetchServiceInsMetaByClientId",
      requestType = com.pinecone.hydra.service.registry.grpc.server.meta.ClientIdRequest.class,
      responseType = com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.meta.ClientIdRequest,
      com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply> getFetchServiceInsMetaByClientIdMethod() {
    io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.meta.ClientIdRequest, com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply> getFetchServiceInsMetaByClientIdMethod;
    if ((getFetchServiceInsMetaByClientIdMethod = ServiceMetaGrpc.getFetchServiceInsMetaByClientIdMethod) == null) {
      synchronized (ServiceMetaGrpc.class) {
        if ((getFetchServiceInsMetaByClientIdMethod = ServiceMetaGrpc.getFetchServiceInsMetaByClientIdMethod) == null) {
          ServiceMetaGrpc.getFetchServiceInsMetaByClientIdMethod = getFetchServiceInsMetaByClientIdMethod =
              io.grpc.MethodDescriptor.<com.pinecone.hydra.service.registry.grpc.server.meta.ClientIdRequest, com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "FetchServiceInsMetaByClientId"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.meta.ClientIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceMetaMethodDescriptorSupplier("FetchServiceInsMetaByClientId"))
              .build();
        }
      }
    }
    return getFetchServiceInsMetaByClientIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceIdRequest,
      com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply> getFetchServiceInsMetaByServiceIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "FetchServiceInsMetaByServiceId",
      requestType = com.pinecone.hydra.service.registry.grpc.server.meta.ServiceIdRequest.class,
      responseType = com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceIdRequest,
      com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply> getFetchServiceInsMetaByServiceIdMethod() {
    io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceIdRequest, com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply> getFetchServiceInsMetaByServiceIdMethod;
    if ((getFetchServiceInsMetaByServiceIdMethod = ServiceMetaGrpc.getFetchServiceInsMetaByServiceIdMethod) == null) {
      synchronized (ServiceMetaGrpc.class) {
        if ((getFetchServiceInsMetaByServiceIdMethod = ServiceMetaGrpc.getFetchServiceInsMetaByServiceIdMethod) == null) {
          ServiceMetaGrpc.getFetchServiceInsMetaByServiceIdMethod = getFetchServiceInsMetaByServiceIdMethod =
              io.grpc.MethodDescriptor.<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceIdRequest, com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "FetchServiceInsMetaByServiceId"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.meta.ServiceIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceMetaMethodDescriptorSupplier("FetchServiceInsMetaByServiceId"))
              .build();
        }
      }
    }
    return getFetchServiceInsMetaByServiceIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.meta.PathRequest,
      com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply> getQueryServiceMetaByPathMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "QueryServiceMetaByPath",
      requestType = com.pinecone.hydra.service.registry.grpc.server.meta.PathRequest.class,
      responseType = com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.meta.PathRequest,
      com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply> getQueryServiceMetaByPathMethod() {
    io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.meta.PathRequest, com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply> getQueryServiceMetaByPathMethod;
    if ((getQueryServiceMetaByPathMethod = ServiceMetaGrpc.getQueryServiceMetaByPathMethod) == null) {
      synchronized (ServiceMetaGrpc.class) {
        if ((getQueryServiceMetaByPathMethod = ServiceMetaGrpc.getQueryServiceMetaByPathMethod) == null) {
          ServiceMetaGrpc.getQueryServiceMetaByPathMethod = getQueryServiceMetaByPathMethod =
              io.grpc.MethodDescriptor.<com.pinecone.hydra.service.registry.grpc.server.meta.PathRequest, com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "QueryServiceMetaByPath"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.meta.PathRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceMetaMethodDescriptorSupplier("QueryServiceMetaByPath"))
              .build();
        }
      }
    }
    return getQueryServiceMetaByPathMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.meta.GuidRequest,
      com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply> getQueryServiceMetaByGuidMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "QueryServiceMetaByGuid",
      requestType = com.pinecone.hydra.service.registry.grpc.server.meta.GuidRequest.class,
      responseType = com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.meta.GuidRequest,
      com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply> getQueryServiceMetaByGuidMethod() {
    io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.meta.GuidRequest, com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply> getQueryServiceMetaByGuidMethod;
    if ((getQueryServiceMetaByGuidMethod = ServiceMetaGrpc.getQueryServiceMetaByGuidMethod) == null) {
      synchronized (ServiceMetaGrpc.class) {
        if ((getQueryServiceMetaByGuidMethod = ServiceMetaGrpc.getQueryServiceMetaByGuidMethod) == null) {
          ServiceMetaGrpc.getQueryServiceMetaByGuidMethod = getQueryServiceMetaByGuidMethod =
              io.grpc.MethodDescriptor.<com.pinecone.hydra.service.registry.grpc.server.meta.GuidRequest, com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "QueryServiceMetaByGuid"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.meta.GuidRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceMetaMethodDescriptorSupplier("QueryServiceMetaByGuid"))
              .build();
        }
      }
    }
    return getQueryServiceMetaByGuidMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.meta.EvalRequest,
      com.pinecone.hydra.service.registry.grpc.server.meta.StringReply> getEvalCreationStatementMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "EvalCreationStatement",
      requestType = com.pinecone.hydra.service.registry.grpc.server.meta.EvalRequest.class,
      responseType = com.pinecone.hydra.service.registry.grpc.server.meta.StringReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.meta.EvalRequest,
      com.pinecone.hydra.service.registry.grpc.server.meta.StringReply> getEvalCreationStatementMethod() {
    io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.meta.EvalRequest, com.pinecone.hydra.service.registry.grpc.server.meta.StringReply> getEvalCreationStatementMethod;
    if ((getEvalCreationStatementMethod = ServiceMetaGrpc.getEvalCreationStatementMethod) == null) {
      synchronized (ServiceMetaGrpc.class) {
        if ((getEvalCreationStatementMethod = ServiceMetaGrpc.getEvalCreationStatementMethod) == null) {
          ServiceMetaGrpc.getEvalCreationStatementMethod = getEvalCreationStatementMethod =
              io.grpc.MethodDescriptor.<com.pinecone.hydra.service.registry.grpc.server.meta.EvalRequest, com.pinecone.hydra.service.registry.grpc.server.meta.StringReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "EvalCreationStatement"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.meta.EvalRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.meta.StringReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceMetaMethodDescriptorSupplier("EvalCreationStatement"))
              .build();
        }
      }
    }
    return getEvalCreationStatementMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.meta.CreateNewServiceRequest,
      com.pinecone.hydra.service.registry.grpc.server.meta.StringReply> getCreateNewServiceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateNewService",
      requestType = com.pinecone.hydra.service.registry.grpc.server.meta.CreateNewServiceRequest.class,
      responseType = com.pinecone.hydra.service.registry.grpc.server.meta.StringReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.meta.CreateNewServiceRequest,
      com.pinecone.hydra.service.registry.grpc.server.meta.StringReply> getCreateNewServiceMethod() {
    io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.meta.CreateNewServiceRequest, com.pinecone.hydra.service.registry.grpc.server.meta.StringReply> getCreateNewServiceMethod;
    if ((getCreateNewServiceMethod = ServiceMetaGrpc.getCreateNewServiceMethod) == null) {
      synchronized (ServiceMetaGrpc.class) {
        if ((getCreateNewServiceMethod = ServiceMetaGrpc.getCreateNewServiceMethod) == null) {
          ServiceMetaGrpc.getCreateNewServiceMethod = getCreateNewServiceMethod =
              io.grpc.MethodDescriptor.<com.pinecone.hydra.service.registry.grpc.server.meta.CreateNewServiceRequest, com.pinecone.hydra.service.registry.grpc.server.meta.StringReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateNewService"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.meta.CreateNewServiceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.meta.StringReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceMetaMethodDescriptorSupplier("CreateNewService"))
              .build();
        }
      }
    }
    return getCreateNewServiceMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ServiceMetaStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ServiceMetaStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ServiceMetaStub>() {
        @java.lang.Override
        public ServiceMetaStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ServiceMetaStub(channel, callOptions);
        }
      };
    return ServiceMetaStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ServiceMetaBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ServiceMetaBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ServiceMetaBlockingStub>() {
        @java.lang.Override
        public ServiceMetaBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ServiceMetaBlockingStub(channel, callOptions);
        }
      };
    return ServiceMetaBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ServiceMetaFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ServiceMetaFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ServiceMetaFutureStub>() {
        @java.lang.Override
        public ServiceMetaFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ServiceMetaFutureStub(channel, callOptions);
        }
      };
    return ServiceMetaFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void fetchServiceInsMetaByClientId(com.pinecone.hydra.service.registry.grpc.server.meta.ClientIdRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFetchServiceInsMetaByClientIdMethod(), responseObserver);
    }

    /**
     */
    default void fetchServiceInsMetaByServiceId(com.pinecone.hydra.service.registry.grpc.server.meta.ServiceIdRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFetchServiceInsMetaByServiceIdMethod(), responseObserver);
    }

    /**
     */
    default void queryServiceMetaByPath(com.pinecone.hydra.service.registry.grpc.server.meta.PathRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getQueryServiceMetaByPathMethod(), responseObserver);
    }

    /**
     */
    default void queryServiceMetaByGuid(com.pinecone.hydra.service.registry.grpc.server.meta.GuidRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getQueryServiceMetaByGuidMethod(), responseObserver);
    }

    /**
     */
    default void evalCreationStatement(com.pinecone.hydra.service.registry.grpc.server.meta.EvalRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.meta.StringReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getEvalCreationStatementMethod(), responseObserver);
    }

    /**
     */
    default void createNewService(com.pinecone.hydra.service.registry.grpc.server.meta.CreateNewServiceRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.meta.StringReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateNewServiceMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ServiceMeta.
   */
  public static abstract class ServiceMetaImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ServiceMetaGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ServiceMeta.
   */
  public static final class ServiceMetaStub
      extends io.grpc.stub.AbstractAsyncStub<ServiceMetaStub> {
    private ServiceMetaStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceMetaStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ServiceMetaStub(channel, callOptions);
    }

    /**
     */
    public void fetchServiceInsMetaByClientId(com.pinecone.hydra.service.registry.grpc.server.meta.ClientIdRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFetchServiceInsMetaByClientIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void fetchServiceInsMetaByServiceId(com.pinecone.hydra.service.registry.grpc.server.meta.ServiceIdRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFetchServiceInsMetaByServiceIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void queryServiceMetaByPath(com.pinecone.hydra.service.registry.grpc.server.meta.PathRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getQueryServiceMetaByPathMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void queryServiceMetaByGuid(com.pinecone.hydra.service.registry.grpc.server.meta.GuidRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getQueryServiceMetaByGuidMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void evalCreationStatement(com.pinecone.hydra.service.registry.grpc.server.meta.EvalRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.meta.StringReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getEvalCreationStatementMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createNewService(com.pinecone.hydra.service.registry.grpc.server.meta.CreateNewServiceRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.meta.StringReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateNewServiceMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ServiceMeta.
   */
  public static final class ServiceMetaBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ServiceMetaBlockingStub> {
    private ServiceMetaBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceMetaBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ServiceMetaBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply fetchServiceInsMetaByClientId(com.pinecone.hydra.service.registry.grpc.server.meta.ClientIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFetchServiceInsMetaByClientIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply fetchServiceInsMetaByServiceId(com.pinecone.hydra.service.registry.grpc.server.meta.ServiceIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFetchServiceInsMetaByServiceIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply queryServiceMetaByPath(com.pinecone.hydra.service.registry.grpc.server.meta.PathRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getQueryServiceMetaByPathMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply queryServiceMetaByGuid(com.pinecone.hydra.service.registry.grpc.server.meta.GuidRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getQueryServiceMetaByGuidMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.pinecone.hydra.service.registry.grpc.server.meta.StringReply evalCreationStatement(com.pinecone.hydra.service.registry.grpc.server.meta.EvalRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getEvalCreationStatementMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.pinecone.hydra.service.registry.grpc.server.meta.StringReply createNewService(com.pinecone.hydra.service.registry.grpc.server.meta.CreateNewServiceRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateNewServiceMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ServiceMeta.
   */
  public static final class ServiceMetaFutureStub
      extends io.grpc.stub.AbstractFutureStub<ServiceMetaFutureStub> {
    private ServiceMetaFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceMetaFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ServiceMetaFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply> fetchServiceInsMetaByClientId(
        com.pinecone.hydra.service.registry.grpc.server.meta.ClientIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFetchServiceInsMetaByClientIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply> fetchServiceInsMetaByServiceId(
        com.pinecone.hydra.service.registry.grpc.server.meta.ServiceIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFetchServiceInsMetaByServiceIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply> queryServiceMetaByPath(
        com.pinecone.hydra.service.registry.grpc.server.meta.PathRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getQueryServiceMetaByPathMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply> queryServiceMetaByGuid(
        com.pinecone.hydra.service.registry.grpc.server.meta.GuidRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getQueryServiceMetaByGuidMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.pinecone.hydra.service.registry.grpc.server.meta.StringReply> evalCreationStatement(
        com.pinecone.hydra.service.registry.grpc.server.meta.EvalRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getEvalCreationStatementMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.pinecone.hydra.service.registry.grpc.server.meta.StringReply> createNewService(
        com.pinecone.hydra.service.registry.grpc.server.meta.CreateNewServiceRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateNewServiceMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_FETCH_SERVICE_INS_META_BY_CLIENT_ID = 0;
  private static final int METHODID_FETCH_SERVICE_INS_META_BY_SERVICE_ID = 1;
  private static final int METHODID_QUERY_SERVICE_META_BY_PATH = 2;
  private static final int METHODID_QUERY_SERVICE_META_BY_GUID = 3;
  private static final int METHODID_EVAL_CREATION_STATEMENT = 4;
  private static final int METHODID_CREATE_NEW_SERVICE = 5;

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
        case METHODID_FETCH_SERVICE_INS_META_BY_CLIENT_ID:
          serviceImpl.fetchServiceInsMetaByClientId((com.pinecone.hydra.service.registry.grpc.server.meta.ClientIdRequest) request,
              (io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply>) responseObserver);
          break;
        case METHODID_FETCH_SERVICE_INS_META_BY_SERVICE_ID:
          serviceImpl.fetchServiceInsMetaByServiceId((com.pinecone.hydra.service.registry.grpc.server.meta.ServiceIdRequest) request,
              (io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply>) responseObserver);
          break;
        case METHODID_QUERY_SERVICE_META_BY_PATH:
          serviceImpl.queryServiceMetaByPath((com.pinecone.hydra.service.registry.grpc.server.meta.PathRequest) request,
              (io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply>) responseObserver);
          break;
        case METHODID_QUERY_SERVICE_META_BY_GUID:
          serviceImpl.queryServiceMetaByGuid((com.pinecone.hydra.service.registry.grpc.server.meta.GuidRequest) request,
              (io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply>) responseObserver);
          break;
        case METHODID_EVAL_CREATION_STATEMENT:
          serviceImpl.evalCreationStatement((com.pinecone.hydra.service.registry.grpc.server.meta.EvalRequest) request,
              (io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.meta.StringReply>) responseObserver);
          break;
        case METHODID_CREATE_NEW_SERVICE:
          serviceImpl.createNewService((com.pinecone.hydra.service.registry.grpc.server.meta.CreateNewServiceRequest) request,
              (io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.meta.StringReply>) responseObserver);
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
          getFetchServiceInsMetaByClientIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.pinecone.hydra.service.registry.grpc.server.meta.ClientIdRequest,
              com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply>(
                service, METHODID_FETCH_SERVICE_INS_META_BY_CLIENT_ID)))
        .addMethod(
          getFetchServiceInsMetaByServiceIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.pinecone.hydra.service.registry.grpc.server.meta.ServiceIdRequest,
              com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply>(
                service, METHODID_FETCH_SERVICE_INS_META_BY_SERVICE_ID)))
        .addMethod(
          getQueryServiceMetaByPathMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.pinecone.hydra.service.registry.grpc.server.meta.PathRequest,
              com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply>(
                service, METHODID_QUERY_SERVICE_META_BY_PATH)))
        .addMethod(
          getQueryServiceMetaByGuidMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.pinecone.hydra.service.registry.grpc.server.meta.GuidRequest,
              com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply>(
                service, METHODID_QUERY_SERVICE_META_BY_GUID)))
        .addMethod(
          getEvalCreationStatementMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.pinecone.hydra.service.registry.grpc.server.meta.EvalRequest,
              com.pinecone.hydra.service.registry.grpc.server.meta.StringReply>(
                service, METHODID_EVAL_CREATION_STATEMENT)))
        .addMethod(
          getCreateNewServiceMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.pinecone.hydra.service.registry.grpc.server.meta.CreateNewServiceRequest,
              com.pinecone.hydra.service.registry.grpc.server.meta.StringReply>(
                service, METHODID_CREATE_NEW_SERVICE)))
        .build();
  }

  private static abstract class ServiceMetaBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ServiceMetaBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ServiceMeta");
    }
  }

  private static final class ServiceMetaFileDescriptorSupplier
      extends ServiceMetaBaseDescriptorSupplier {
    ServiceMetaFileDescriptorSupplier() {}
  }

  private static final class ServiceMetaMethodDescriptorSupplier
      extends ServiceMetaBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ServiceMetaMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (ServiceMetaGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ServiceMetaFileDescriptorSupplier())
              .addMethod(getFetchServiceInsMetaByClientIdMethod())
              .addMethod(getFetchServiceInsMetaByServiceIdMethod())
              .addMethod(getQueryServiceMetaByPathMethod())
              .addMethod(getQueryServiceMetaByGuidMethod())
              .addMethod(getEvalCreationStatementMethod())
              .addMethod(getCreateNewServiceMethod())
              .build();
        }
      }
    }
    return result;
  }
}
