package com.pinecone.hydra.service.registry.grpc.server.lifecycle;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: service_lifecycle.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ServiceLifecycleGrpc {

  private ServiceLifecycleGrpc() {}

  public static final java.lang.String SERVICE_NAME = "ServiceLifecycle";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceRequest,
      com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceReply> getRegisterServiceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RegisterService",
      requestType = com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceRequest.class,
      responseType = com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceRequest,
      com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceReply> getRegisterServiceMethod() {
    io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceRequest, com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceReply> getRegisterServiceMethod;
    if ((getRegisterServiceMethod = ServiceLifecycleGrpc.getRegisterServiceMethod) == null) {
      synchronized (ServiceLifecycleGrpc.class) {
        if ((getRegisterServiceMethod = ServiceLifecycleGrpc.getRegisterServiceMethod) == null) {
          ServiceLifecycleGrpc.getRegisterServiceMethod = getRegisterServiceMethod =
              io.grpc.MethodDescriptor.<com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceRequest, com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RegisterService"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceLifecycleMethodDescriptorSupplier("RegisterService"))
              .build();
        }
      }
    }
    return getRegisterServiceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.CreateInstanceMetaRequest,
      com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> getCreateInstanceMetaMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateInstanceMeta",
      requestType = com.pinecone.hydra.service.registry.grpc.server.lifecycle.CreateInstanceMetaRequest.class,
      responseType = com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.CreateInstanceMetaRequest,
      com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> getCreateInstanceMetaMethod() {
    io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.CreateInstanceMetaRequest, com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> getCreateInstanceMetaMethod;
    if ((getCreateInstanceMetaMethod = ServiceLifecycleGrpc.getCreateInstanceMetaMethod) == null) {
      synchronized (ServiceLifecycleGrpc.class) {
        if ((getCreateInstanceMetaMethod = ServiceLifecycleGrpc.getCreateInstanceMetaMethod) == null) {
          ServiceLifecycleGrpc.getCreateInstanceMetaMethod = getCreateInstanceMetaMethod =
              io.grpc.MethodDescriptor.<com.pinecone.hydra.service.registry.grpc.server.lifecycle.CreateInstanceMetaRequest, com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateInstanceMeta"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.lifecycle.CreateInstanceMetaRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceLifecycleMethodDescriptorSupplier("CreateInstanceMeta"))
              .build();
        }
      }
    }
    return getCreateInstanceMetaMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest,
      com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply> getDeregisterServiceByClientIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeregisterServiceByClientId",
      requestType = com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest.class,
      responseType = com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest,
      com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply> getDeregisterServiceByClientIdMethod() {
    io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest, com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply> getDeregisterServiceByClientIdMethod;
    if ((getDeregisterServiceByClientIdMethod = ServiceLifecycleGrpc.getDeregisterServiceByClientIdMethod) == null) {
      synchronized (ServiceLifecycleGrpc.class) {
        if ((getDeregisterServiceByClientIdMethod = ServiceLifecycleGrpc.getDeregisterServiceByClientIdMethod) == null) {
          ServiceLifecycleGrpc.getDeregisterServiceByClientIdMethod = getDeregisterServiceByClientIdMethod =
              io.grpc.MethodDescriptor.<com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest, com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeregisterServiceByClientId"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceLifecycleMethodDescriptorSupplier("DeregisterServiceByClientId"))
              .build();
        }
      }
    }
    return getDeregisterServiceByClientIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest,
      com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply> getDeregisterServiceByInstanceIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeregisterServiceByInstanceId",
      requestType = com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest.class,
      responseType = com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest,
      com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply> getDeregisterServiceByInstanceIdMethod() {
    io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest, com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply> getDeregisterServiceByInstanceIdMethod;
    if ((getDeregisterServiceByInstanceIdMethod = ServiceLifecycleGrpc.getDeregisterServiceByInstanceIdMethod) == null) {
      synchronized (ServiceLifecycleGrpc.class) {
        if ((getDeregisterServiceByInstanceIdMethod = ServiceLifecycleGrpc.getDeregisterServiceByInstanceIdMethod) == null) {
          ServiceLifecycleGrpc.getDeregisterServiceByInstanceIdMethod = getDeregisterServiceByInstanceIdMethod =
              io.grpc.MethodDescriptor.<com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest, com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeregisterServiceByInstanceId"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceLifecycleMethodDescriptorSupplier("DeregisterServiceByInstanceId"))
              .build();
        }
      }
    }
    return getDeregisterServiceByInstanceIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.ServiceIdRequest,
      com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> getHasOwnedServiceByServiceIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "HasOwnedServiceByServiceId",
      requestType = com.pinecone.hydra.service.registry.grpc.server.lifecycle.ServiceIdRequest.class,
      responseType = com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.ServiceIdRequest,
      com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> getHasOwnedServiceByServiceIdMethod() {
    io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.ServiceIdRequest, com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> getHasOwnedServiceByServiceIdMethod;
    if ((getHasOwnedServiceByServiceIdMethod = ServiceLifecycleGrpc.getHasOwnedServiceByServiceIdMethod) == null) {
      synchronized (ServiceLifecycleGrpc.class) {
        if ((getHasOwnedServiceByServiceIdMethod = ServiceLifecycleGrpc.getHasOwnedServiceByServiceIdMethod) == null) {
          ServiceLifecycleGrpc.getHasOwnedServiceByServiceIdMethod = getHasOwnedServiceByServiceIdMethod =
              io.grpc.MethodDescriptor.<com.pinecone.hydra.service.registry.grpc.server.lifecycle.ServiceIdRequest, com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "HasOwnedServiceByServiceId"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.lifecycle.ServiceIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceLifecycleMethodDescriptorSupplier("HasOwnedServiceByServiceId"))
              .build();
        }
      }
    }
    return getHasOwnedServiceByServiceIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest,
      com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> getHasOwnedServiceInstanceByClientIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "HasOwnedServiceInstanceByClientId",
      requestType = com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest.class,
      responseType = com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest,
      com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> getHasOwnedServiceInstanceByClientIdMethod() {
    io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest, com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> getHasOwnedServiceInstanceByClientIdMethod;
    if ((getHasOwnedServiceInstanceByClientIdMethod = ServiceLifecycleGrpc.getHasOwnedServiceInstanceByClientIdMethod) == null) {
      synchronized (ServiceLifecycleGrpc.class) {
        if ((getHasOwnedServiceInstanceByClientIdMethod = ServiceLifecycleGrpc.getHasOwnedServiceInstanceByClientIdMethod) == null) {
          ServiceLifecycleGrpc.getHasOwnedServiceInstanceByClientIdMethod = getHasOwnedServiceInstanceByClientIdMethod =
              io.grpc.MethodDescriptor.<com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest, com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "HasOwnedServiceInstanceByClientId"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceLifecycleMethodDescriptorSupplier("HasOwnedServiceInstanceByClientId"))
              .build();
        }
      }
    }
    return getHasOwnedServiceInstanceByClientIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest,
      com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> getHasOwnedServiceInstanceByInstanceIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "HasOwnedServiceInstanceByInstanceId",
      requestType = com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest.class,
      responseType = com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest,
      com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> getHasOwnedServiceInstanceByInstanceIdMethod() {
    io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest, com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> getHasOwnedServiceInstanceByInstanceIdMethod;
    if ((getHasOwnedServiceInstanceByInstanceIdMethod = ServiceLifecycleGrpc.getHasOwnedServiceInstanceByInstanceIdMethod) == null) {
      synchronized (ServiceLifecycleGrpc.class) {
        if ((getHasOwnedServiceInstanceByInstanceIdMethod = ServiceLifecycleGrpc.getHasOwnedServiceInstanceByInstanceIdMethod) == null) {
          ServiceLifecycleGrpc.getHasOwnedServiceInstanceByInstanceIdMethod = getHasOwnedServiceInstanceByInstanceIdMethod =
              io.grpc.MethodDescriptor.<com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest, com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "HasOwnedServiceInstanceByInstanceId"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceLifecycleMethodDescriptorSupplier("HasOwnedServiceInstanceByInstanceId"))
              .build();
        }
      }
    }
    return getHasOwnedServiceInstanceByInstanceIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest,
      com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> getHasOwnedServiceClientMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "HasOwnedServiceClient",
      requestType = com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest.class,
      responseType = com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest,
      com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> getHasOwnedServiceClientMethod() {
    io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest, com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> getHasOwnedServiceClientMethod;
    if ((getHasOwnedServiceClientMethod = ServiceLifecycleGrpc.getHasOwnedServiceClientMethod) == null) {
      synchronized (ServiceLifecycleGrpc.class) {
        if ((getHasOwnedServiceClientMethod = ServiceLifecycleGrpc.getHasOwnedServiceClientMethod) == null) {
          ServiceLifecycleGrpc.getHasOwnedServiceClientMethod = getHasOwnedServiceClientMethod =
              io.grpc.MethodDescriptor.<com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest, com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "HasOwnedServiceClient"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceLifecycleMethodDescriptorSupplier("HasOwnedServiceClient"))
              .build();
        }
      }
    }
    return getHasOwnedServiceClientMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyRequest,
      com.pinecone.hydra.service.registry.grpc.server.lifecycle.CountReply> getCountRegisteredServiceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CountRegisteredService",
      requestType = com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyRequest.class,
      responseType = com.pinecone.hydra.service.registry.grpc.server.lifecycle.CountReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyRequest,
      com.pinecone.hydra.service.registry.grpc.server.lifecycle.CountReply> getCountRegisteredServiceMethod() {
    io.grpc.MethodDescriptor<com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyRequest, com.pinecone.hydra.service.registry.grpc.server.lifecycle.CountReply> getCountRegisteredServiceMethod;
    if ((getCountRegisteredServiceMethod = ServiceLifecycleGrpc.getCountRegisteredServiceMethod) == null) {
      synchronized (ServiceLifecycleGrpc.class) {
        if ((getCountRegisteredServiceMethod = ServiceLifecycleGrpc.getCountRegisteredServiceMethod) == null) {
          ServiceLifecycleGrpc.getCountRegisteredServiceMethod = getCountRegisteredServiceMethod =
              io.grpc.MethodDescriptor.<com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyRequest, com.pinecone.hydra.service.registry.grpc.server.lifecycle.CountReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CountRegisteredService"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.pinecone.hydra.service.registry.grpc.server.lifecycle.CountReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceLifecycleMethodDescriptorSupplier("CountRegisteredService"))
              .build();
        }
      }
    }
    return getCountRegisteredServiceMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ServiceLifecycleStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ServiceLifecycleStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ServiceLifecycleStub>() {
        @java.lang.Override
        public ServiceLifecycleStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ServiceLifecycleStub(channel, callOptions);
        }
      };
    return ServiceLifecycleStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ServiceLifecycleBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ServiceLifecycleBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ServiceLifecycleBlockingStub>() {
        @java.lang.Override
        public ServiceLifecycleBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ServiceLifecycleBlockingStub(channel, callOptions);
        }
      };
    return ServiceLifecycleBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ServiceLifecycleFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ServiceLifecycleFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ServiceLifecycleFutureStub>() {
        @java.lang.Override
        public ServiceLifecycleFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ServiceLifecycleFutureStub(channel, callOptions);
        }
      };
    return ServiceLifecycleFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void registerService(com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterServiceMethod(), responseObserver);
    }

    /**
     */
    default void createInstanceMeta(com.pinecone.hydra.service.registry.grpc.server.lifecycle.CreateInstanceMetaRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateInstanceMetaMethod(), responseObserver);
    }

    /**
     */
    default void deregisterServiceByClientId(com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeregisterServiceByClientIdMethod(), responseObserver);
    }

    /**
     */
    default void deregisterServiceByInstanceId(com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeregisterServiceByInstanceIdMethod(), responseObserver);
    }

    /**
     */
    default void hasOwnedServiceByServiceId(com.pinecone.hydra.service.registry.grpc.server.lifecycle.ServiceIdRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHasOwnedServiceByServiceIdMethod(), responseObserver);
    }

    /**
     */
    default void hasOwnedServiceInstanceByClientId(com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHasOwnedServiceInstanceByClientIdMethod(), responseObserver);
    }

    /**
     */
    default void hasOwnedServiceInstanceByInstanceId(com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHasOwnedServiceInstanceByInstanceIdMethod(), responseObserver);
    }

    /**
     */
    default void hasOwnedServiceClient(com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHasOwnedServiceClientMethod(), responseObserver);
    }

    /**
     */
    default void countRegisteredService(com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.CountReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCountRegisteredServiceMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ServiceLifecycle.
   */
  public static abstract class ServiceLifecycleImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ServiceLifecycleGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ServiceLifecycle.
   */
  public static final class ServiceLifecycleStub
      extends io.grpc.stub.AbstractAsyncStub<ServiceLifecycleStub> {
    private ServiceLifecycleStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceLifecycleStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ServiceLifecycleStub(channel, callOptions);
    }

    /**
     */
    public void registerService(com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterServiceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createInstanceMeta(com.pinecone.hydra.service.registry.grpc.server.lifecycle.CreateInstanceMetaRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateInstanceMetaMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deregisterServiceByClientId(com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeregisterServiceByClientIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deregisterServiceByInstanceId(com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeregisterServiceByInstanceIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void hasOwnedServiceByServiceId(com.pinecone.hydra.service.registry.grpc.server.lifecycle.ServiceIdRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHasOwnedServiceByServiceIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void hasOwnedServiceInstanceByClientId(com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHasOwnedServiceInstanceByClientIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void hasOwnedServiceInstanceByInstanceId(com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHasOwnedServiceInstanceByInstanceIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void hasOwnedServiceClient(com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHasOwnedServiceClientMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void countRegisteredService(com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyRequest request,
        io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.CountReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCountRegisteredServiceMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ServiceLifecycle.
   */
  public static final class ServiceLifecycleBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ServiceLifecycleBlockingStub> {
    private ServiceLifecycleBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceLifecycleBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ServiceLifecycleBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceReply registerService(com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterServiceMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply createInstanceMeta(com.pinecone.hydra.service.registry.grpc.server.lifecycle.CreateInstanceMetaRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateInstanceMetaMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply deregisterServiceByClientId(com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeregisterServiceByClientIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply deregisterServiceByInstanceId(com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeregisterServiceByInstanceIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply hasOwnedServiceByServiceId(com.pinecone.hydra.service.registry.grpc.server.lifecycle.ServiceIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHasOwnedServiceByServiceIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply hasOwnedServiceInstanceByClientId(com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHasOwnedServiceInstanceByClientIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply hasOwnedServiceInstanceByInstanceId(com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHasOwnedServiceInstanceByInstanceIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply hasOwnedServiceClient(com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHasOwnedServiceClientMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.pinecone.hydra.service.registry.grpc.server.lifecycle.CountReply countRegisteredService(com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCountRegisteredServiceMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ServiceLifecycle.
   */
  public static final class ServiceLifecycleFutureStub
      extends io.grpc.stub.AbstractFutureStub<ServiceLifecycleFutureStub> {
    private ServiceLifecycleFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceLifecycleFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ServiceLifecycleFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceReply> registerService(
        com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterServiceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> createInstanceMeta(
        com.pinecone.hydra.service.registry.grpc.server.lifecycle.CreateInstanceMetaRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateInstanceMetaMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply> deregisterServiceByClientId(
        com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeregisterServiceByClientIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply> deregisterServiceByInstanceId(
        com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeregisterServiceByInstanceIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> hasOwnedServiceByServiceId(
        com.pinecone.hydra.service.registry.grpc.server.lifecycle.ServiceIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHasOwnedServiceByServiceIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> hasOwnedServiceInstanceByClientId(
        com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHasOwnedServiceInstanceByClientIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> hasOwnedServiceInstanceByInstanceId(
        com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHasOwnedServiceInstanceByInstanceIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply> hasOwnedServiceClient(
        com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHasOwnedServiceClientMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.pinecone.hydra.service.registry.grpc.server.lifecycle.CountReply> countRegisteredService(
        com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCountRegisteredServiceMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REGISTER_SERVICE = 0;
  private static final int METHODID_CREATE_INSTANCE_META = 1;
  private static final int METHODID_DEREGISTER_SERVICE_BY_CLIENT_ID = 2;
  private static final int METHODID_DEREGISTER_SERVICE_BY_INSTANCE_ID = 3;
  private static final int METHODID_HAS_OWNED_SERVICE_BY_SERVICE_ID = 4;
  private static final int METHODID_HAS_OWNED_SERVICE_INSTANCE_BY_CLIENT_ID = 5;
  private static final int METHODID_HAS_OWNED_SERVICE_INSTANCE_BY_INSTANCE_ID = 6;
  private static final int METHODID_HAS_OWNED_SERVICE_CLIENT = 7;
  private static final int METHODID_COUNT_REGISTERED_SERVICE = 8;

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
        case METHODID_REGISTER_SERVICE:
          serviceImpl.registerService((com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceRequest) request,
              (io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceReply>) responseObserver);
          break;
        case METHODID_CREATE_INSTANCE_META:
          serviceImpl.createInstanceMeta((com.pinecone.hydra.service.registry.grpc.server.lifecycle.CreateInstanceMetaRequest) request,
              (io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply>) responseObserver);
          break;
        case METHODID_DEREGISTER_SERVICE_BY_CLIENT_ID:
          serviceImpl.deregisterServiceByClientId((com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest) request,
              (io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply>) responseObserver);
          break;
        case METHODID_DEREGISTER_SERVICE_BY_INSTANCE_ID:
          serviceImpl.deregisterServiceByInstanceId((com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest) request,
              (io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply>) responseObserver);
          break;
        case METHODID_HAS_OWNED_SERVICE_BY_SERVICE_ID:
          serviceImpl.hasOwnedServiceByServiceId((com.pinecone.hydra.service.registry.grpc.server.lifecycle.ServiceIdRequest) request,
              (io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply>) responseObserver);
          break;
        case METHODID_HAS_OWNED_SERVICE_INSTANCE_BY_CLIENT_ID:
          serviceImpl.hasOwnedServiceInstanceByClientId((com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest) request,
              (io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply>) responseObserver);
          break;
        case METHODID_HAS_OWNED_SERVICE_INSTANCE_BY_INSTANCE_ID:
          serviceImpl.hasOwnedServiceInstanceByInstanceId((com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest) request,
              (io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply>) responseObserver);
          break;
        case METHODID_HAS_OWNED_SERVICE_CLIENT:
          serviceImpl.hasOwnedServiceClient((com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest) request,
              (io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply>) responseObserver);
          break;
        case METHODID_COUNT_REGISTERED_SERVICE:
          serviceImpl.countRegisteredService((com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyRequest) request,
              (io.grpc.stub.StreamObserver<com.pinecone.hydra.service.registry.grpc.server.lifecycle.CountReply>) responseObserver);
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
          getRegisterServiceMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceRequest,
              com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceReply>(
                service, METHODID_REGISTER_SERVICE)))
        .addMethod(
          getCreateInstanceMetaMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.pinecone.hydra.service.registry.grpc.server.lifecycle.CreateInstanceMetaRequest,
              com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply>(
                service, METHODID_CREATE_INSTANCE_META)))
        .addMethod(
          getDeregisterServiceByClientIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest,
              com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply>(
                service, METHODID_DEREGISTER_SERVICE_BY_CLIENT_ID)))
        .addMethod(
          getDeregisterServiceByInstanceIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest,
              com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply>(
                service, METHODID_DEREGISTER_SERVICE_BY_INSTANCE_ID)))
        .addMethod(
          getHasOwnedServiceByServiceIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.pinecone.hydra.service.registry.grpc.server.lifecycle.ServiceIdRequest,
              com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply>(
                service, METHODID_HAS_OWNED_SERVICE_BY_SERVICE_ID)))
        .addMethod(
          getHasOwnedServiceInstanceByClientIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest,
              com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply>(
                service, METHODID_HAS_OWNED_SERVICE_INSTANCE_BY_CLIENT_ID)))
        .addMethod(
          getHasOwnedServiceInstanceByInstanceIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest,
              com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply>(
                service, METHODID_HAS_OWNED_SERVICE_INSTANCE_BY_INSTANCE_ID)))
        .addMethod(
          getHasOwnedServiceClientMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest,
              com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply>(
                service, METHODID_HAS_OWNED_SERVICE_CLIENT)))
        .addMethod(
          getCountRegisteredServiceMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyRequest,
              com.pinecone.hydra.service.registry.grpc.server.lifecycle.CountReply>(
                service, METHODID_COUNT_REGISTERED_SERVICE)))
        .build();
  }

  private static abstract class ServiceLifecycleBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ServiceLifecycleBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.pinecone.hydra.service.registry.grpc.server.lifecycle.ServiceLifecycleProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ServiceLifecycle");
    }
  }

  private static final class ServiceLifecycleFileDescriptorSupplier
      extends ServiceLifecycleBaseDescriptorSupplier {
    ServiceLifecycleFileDescriptorSupplier() {}
  }

  private static final class ServiceLifecycleMethodDescriptorSupplier
      extends ServiceLifecycleBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ServiceLifecycleMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (ServiceLifecycleGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ServiceLifecycleFileDescriptorSupplier())
              .addMethod(getRegisterServiceMethod())
              .addMethod(getCreateInstanceMetaMethod())
              .addMethod(getDeregisterServiceByClientIdMethod())
              .addMethod(getDeregisterServiceByInstanceIdMethod())
              .addMethod(getHasOwnedServiceByServiceIdMethod())
              .addMethod(getHasOwnedServiceInstanceByClientIdMethod())
              .addMethod(getHasOwnedServiceInstanceByInstanceIdMethod())
              .addMethod(getHasOwnedServiceClientMethod())
              .addMethod(getCountRegisteredServiceMethod())
              .build();
        }
      }
    }
    return result;
  }
}
