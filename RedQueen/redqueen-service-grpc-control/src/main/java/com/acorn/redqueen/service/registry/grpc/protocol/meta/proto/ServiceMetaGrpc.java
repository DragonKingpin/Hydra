package com.acorn.redqueen.service.registry.grpc.protocol.meta.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: service_meta.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ServiceMetaGrpc {

  private ServiceMetaGrpc() {}

  public static final java.lang.String SERVICE_NAME = "ServiceMeta";

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
    if ((getFetchServicePageMethod = ServiceMetaGrpc.getFetchServicePageMethod) == null) {
      synchronized (ServiceMetaGrpc.class) {
        if ((getFetchServicePageMethod = ServiceMetaGrpc.getFetchServicePageMethod) == null) {
          ServiceMetaGrpc.getFetchServicePageMethod = getFetchServicePageMethod =
              io.grpc.MethodDescriptor.<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServicePageRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServicePageReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "FetchServicePage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServicePageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServicePageReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceMetaMethodDescriptorSupplier("FetchServicePage"))
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
    if ((getFetchServiceInstancePageMethod = ServiceMetaGrpc.getFetchServiceInstancePageMethod) == null) {
      synchronized (ServiceMetaGrpc.class) {
        if ((getFetchServiceInstancePageMethod = ServiceMetaGrpc.getFetchServiceInstancePageMethod) == null) {
          ServiceMetaGrpc.getFetchServiceInstancePageMethod = getFetchServiceInstancePageMethod =
              io.grpc.MethodDescriptor.<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInstancePageRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstancePageReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "FetchServiceInstancePage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInstancePageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstancePageReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceMetaMethodDescriptorSupplier("FetchServiceInstancePage"))
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
    if ((getQueryServiceMethod = ServiceMetaGrpc.getQueryServiceMethod) == null) {
      synchronized (ServiceMetaGrpc.class) {
        if ((getQueryServiceMethod = ServiceMetaGrpc.getQueryServiceMethod) == null) {
          ServiceMetaGrpc.getQueryServiceMethod = getQueryServiceMethod =
              io.grpc.MethodDescriptor.<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "QueryService"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceMetaMethodDescriptorSupplier("QueryService"))
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
    if ((getQueryServiceInstanceMethod = ServiceMetaGrpc.getQueryServiceInstanceMethod) == null) {
      synchronized (ServiceMetaGrpc.class) {
        if ((getQueryServiceInstanceMethod = ServiceMetaGrpc.getQueryServiceInstanceMethod) == null) {
          ServiceMetaGrpc.getQueryServiceInstanceMethod = getQueryServiceInstanceMethod =
              io.grpc.MethodDescriptor.<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceInstanceRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstanceReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "QueryServiceInstance"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceInstanceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstanceReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceMetaMethodDescriptorSupplier("QueryServiceInstance"))
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
    if ((getFetchNamespaceChildrenMethod = ServiceMetaGrpc.getFetchNamespaceChildrenMethod) == null) {
      synchronized (ServiceMetaGrpc.class) {
        if ((getFetchNamespaceChildrenMethod = ServiceMetaGrpc.getFetchNamespaceChildrenMethod) == null) {
          ServiceMetaGrpc.getFetchNamespaceChildrenMethod = getFetchNamespaceChildrenMethod =
              io.grpc.MethodDescriptor.<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchNamespaceChildrenRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.NamespaceChildrenReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "FetchNamespaceChildren"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchNamespaceChildrenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.NamespaceChildrenReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceMetaMethodDescriptorSupplier("FetchNamespaceChildren"))
              .build();
        }
      }
    }
    return getFetchNamespaceChildrenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByClientIdRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply> getFetchServiceInsMetaByClientIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "FetchServiceInsMetaByClientId",
      requestType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByClientIdRequest.class,
      responseType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByClientIdRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply> getFetchServiceInsMetaByClientIdMethod() {
    io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByClientIdRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply> getFetchServiceInsMetaByClientIdMethod;
    if ((getFetchServiceInsMetaByClientIdMethod = ServiceMetaGrpc.getFetchServiceInsMetaByClientIdMethod) == null) {
      synchronized (ServiceMetaGrpc.class) {
        if ((getFetchServiceInsMetaByClientIdMethod = ServiceMetaGrpc.getFetchServiceInsMetaByClientIdMethod) == null) {
          ServiceMetaGrpc.getFetchServiceInsMetaByClientIdMethod = getFetchServiceInsMetaByClientIdMethod =
              io.grpc.MethodDescriptor.<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByClientIdRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "FetchServiceInsMetaByClientId"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByClientIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceMetaMethodDescriptorSupplier("FetchServiceInsMetaByClientId"))
              .build();
        }
      }
    }
    return getFetchServiceInsMetaByClientIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByServiceIdRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply> getFetchServiceInsMetaByServiceIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "FetchServiceInsMetaByServiceId",
      requestType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByServiceIdRequest.class,
      responseType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByServiceIdRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply> getFetchServiceInsMetaByServiceIdMethod() {
    io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByServiceIdRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply> getFetchServiceInsMetaByServiceIdMethod;
    if ((getFetchServiceInsMetaByServiceIdMethod = ServiceMetaGrpc.getFetchServiceInsMetaByServiceIdMethod) == null) {
      synchronized (ServiceMetaGrpc.class) {
        if ((getFetchServiceInsMetaByServiceIdMethod = ServiceMetaGrpc.getFetchServiceInsMetaByServiceIdMethod) == null) {
          ServiceMetaGrpc.getFetchServiceInsMetaByServiceIdMethod = getFetchServiceInsMetaByServiceIdMethod =
              io.grpc.MethodDescriptor.<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByServiceIdRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "FetchServiceInsMetaByServiceId"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByServiceIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceMetaMethodDescriptorSupplier("FetchServiceInsMetaByServiceId"))
              .build();
        }
      }
    }
    return getFetchServiceInsMetaByServiceIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByPathRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply> getQueryServiceMetaByPathMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "QueryServiceMetaByPath",
      requestType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByPathRequest.class,
      responseType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByPathRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply> getQueryServiceMetaByPathMethod() {
    io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByPathRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply> getQueryServiceMetaByPathMethod;
    if ((getQueryServiceMetaByPathMethod = ServiceMetaGrpc.getQueryServiceMetaByPathMethod) == null) {
      synchronized (ServiceMetaGrpc.class) {
        if ((getQueryServiceMetaByPathMethod = ServiceMetaGrpc.getQueryServiceMetaByPathMethod) == null) {
          ServiceMetaGrpc.getQueryServiceMetaByPathMethod = getQueryServiceMetaByPathMethod =
              io.grpc.MethodDescriptor.<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByPathRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "QueryServiceMetaByPath"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByPathRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceMetaMethodDescriptorSupplier("QueryServiceMetaByPath"))
              .build();
        }
      }
    }
    return getQueryServiceMetaByPathMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByGuidRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply> getQueryServiceMetaByGuidMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "QueryServiceMetaByGuid",
      requestType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByGuidRequest.class,
      responseType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByGuidRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply> getQueryServiceMetaByGuidMethod() {
    io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByGuidRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply> getQueryServiceMetaByGuidMethod;
    if ((getQueryServiceMetaByGuidMethod = ServiceMetaGrpc.getQueryServiceMetaByGuidMethod) == null) {
      synchronized (ServiceMetaGrpc.class) {
        if ((getQueryServiceMetaByGuidMethod = ServiceMetaGrpc.getQueryServiceMetaByGuidMethod) == null) {
          ServiceMetaGrpc.getQueryServiceMetaByGuidMethod = getQueryServiceMetaByGuidMethod =
              io.grpc.MethodDescriptor.<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByGuidRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "QueryServiceMetaByGuid"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByGuidRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceMetaMethodDescriptorSupplier("QueryServiceMetaByGuid"))
              .build();
        }
      }
    }
    return getQueryServiceMetaByGuidMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.EvalCreationStatementRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply> getEvalCreationStatementMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "EvalCreationStatement",
      requestType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.EvalCreationStatementRequest.class,
      responseType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.EvalCreationStatementRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply> getEvalCreationStatementMethod() {
    io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.EvalCreationStatementRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply> getEvalCreationStatementMethod;
    if ((getEvalCreationStatementMethod = ServiceMetaGrpc.getEvalCreationStatementMethod) == null) {
      synchronized (ServiceMetaGrpc.class) {
        if ((getEvalCreationStatementMethod = ServiceMetaGrpc.getEvalCreationStatementMethod) == null) {
          ServiceMetaGrpc.getEvalCreationStatementMethod = getEvalCreationStatementMethod =
              io.grpc.MethodDescriptor.<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.EvalCreationStatementRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "EvalCreationStatement"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.EvalCreationStatementRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceMetaMethodDescriptorSupplier("EvalCreationStatement"))
              .build();
        }
      }
    }
    return getEvalCreationStatementMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.CreateNewServiceRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply> getCreateNewServiceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateNewService",
      requestType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.CreateNewServiceRequest.class,
      responseType = com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.CreateNewServiceRequest,
      com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply> getCreateNewServiceMethod() {
    io.grpc.MethodDescriptor<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.CreateNewServiceRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply> getCreateNewServiceMethod;
    if ((getCreateNewServiceMethod = ServiceMetaGrpc.getCreateNewServiceMethod) == null) {
      synchronized (ServiceMetaGrpc.class) {
        if ((getCreateNewServiceMethod = ServiceMetaGrpc.getCreateNewServiceMethod) == null) {
          ServiceMetaGrpc.getCreateNewServiceMethod = getCreateNewServiceMethod =
              io.grpc.MethodDescriptor.<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.CreateNewServiceRequest, com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateNewService"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.CreateNewServiceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply.getDefaultInstance()))
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

    /**
     */
    default void fetchServiceInsMetaByClientId(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByClientIdRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFetchServiceInsMetaByClientIdMethod(), responseObserver);
    }

    /**
     */
    default void fetchServiceInsMetaByServiceId(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByServiceIdRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFetchServiceInsMetaByServiceIdMethod(), responseObserver);
    }

    /**
     */
    default void queryServiceMetaByPath(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByPathRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getQueryServiceMetaByPathMethod(), responseObserver);
    }

    /**
     */
    default void queryServiceMetaByGuid(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByGuidRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getQueryServiceMetaByGuidMethod(), responseObserver);
    }

    /**
     */
    default void evalCreationStatement(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.EvalCreationStatementRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getEvalCreationStatementMethod(), responseObserver);
    }

    /**
     */
    default void createNewService(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.CreateNewServiceRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply> responseObserver) {
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

    /**
     */
    public void fetchServiceInsMetaByClientId(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByClientIdRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFetchServiceInsMetaByClientIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void fetchServiceInsMetaByServiceId(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByServiceIdRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFetchServiceInsMetaByServiceIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void queryServiceMetaByPath(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByPathRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getQueryServiceMetaByPathMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void queryServiceMetaByGuid(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByGuidRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getQueryServiceMetaByGuidMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void evalCreationStatement(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.EvalCreationStatementRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getEvalCreationStatementMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createNewService(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.CreateNewServiceRequest request,
        io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply> responseObserver) {
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

    /**
     */
    public com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply fetchServiceInsMetaByClientId(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByClientIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFetchServiceInsMetaByClientIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply fetchServiceInsMetaByServiceId(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByServiceIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFetchServiceInsMetaByServiceIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply queryServiceMetaByPath(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByPathRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getQueryServiceMetaByPathMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply queryServiceMetaByGuid(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByGuidRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getQueryServiceMetaByGuidMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply evalCreationStatement(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.EvalCreationStatementRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getEvalCreationStatementMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply createNewService(com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.CreateNewServiceRequest request) {
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

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply> fetchServiceInsMetaByClientId(
        com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByClientIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFetchServiceInsMetaByClientIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply> fetchServiceInsMetaByServiceId(
        com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByServiceIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFetchServiceInsMetaByServiceIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply> queryServiceMetaByPath(
        com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByPathRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getQueryServiceMetaByPathMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply> queryServiceMetaByGuid(
        com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByGuidRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getQueryServiceMetaByGuidMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply> evalCreationStatement(
        com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.EvalCreationStatementRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getEvalCreationStatementMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply> createNewService(
        com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.CreateNewServiceRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateNewServiceMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_FETCH_SERVICE_PAGE = 0;
  private static final int METHODID_FETCH_SERVICE_INSTANCE_PAGE = 1;
  private static final int METHODID_QUERY_SERVICE = 2;
  private static final int METHODID_QUERY_SERVICE_INSTANCE = 3;
  private static final int METHODID_FETCH_NAMESPACE_CHILDREN = 4;
  private static final int METHODID_FETCH_SERVICE_INS_META_BY_CLIENT_ID = 5;
  private static final int METHODID_FETCH_SERVICE_INS_META_BY_SERVICE_ID = 6;
  private static final int METHODID_QUERY_SERVICE_META_BY_PATH = 7;
  private static final int METHODID_QUERY_SERVICE_META_BY_GUID = 8;
  private static final int METHODID_EVAL_CREATION_STATEMENT = 9;
  private static final int METHODID_CREATE_NEW_SERVICE = 10;

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
        case METHODID_FETCH_SERVICE_INS_META_BY_CLIENT_ID:
          serviceImpl.fetchServiceInsMetaByClientId((com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByClientIdRequest) request,
              (io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply>) responseObserver);
          break;
        case METHODID_FETCH_SERVICE_INS_META_BY_SERVICE_ID:
          serviceImpl.fetchServiceInsMetaByServiceId((com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByServiceIdRequest) request,
              (io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply>) responseObserver);
          break;
        case METHODID_QUERY_SERVICE_META_BY_PATH:
          serviceImpl.queryServiceMetaByPath((com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByPathRequest) request,
              (io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply>) responseObserver);
          break;
        case METHODID_QUERY_SERVICE_META_BY_GUID:
          serviceImpl.queryServiceMetaByGuid((com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByGuidRequest) request,
              (io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply>) responseObserver);
          break;
        case METHODID_EVAL_CREATION_STATEMENT:
          serviceImpl.evalCreationStatement((com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.EvalCreationStatementRequest) request,
              (io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply>) responseObserver);
          break;
        case METHODID_CREATE_NEW_SERVICE:
          serviceImpl.createNewService((com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.CreateNewServiceRequest) request,
              (io.grpc.stub.StreamObserver<com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply>) responseObserver);
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
        .addMethod(
          getFetchServiceInsMetaByClientIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByClientIdRequest,
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply>(
                service, METHODID_FETCH_SERVICE_INS_META_BY_CLIENT_ID)))
        .addMethod(
          getFetchServiceInsMetaByServiceIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByServiceIdRequest,
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply>(
                service, METHODID_FETCH_SERVICE_INS_META_BY_SERVICE_ID)))
        .addMethod(
          getQueryServiceMetaByPathMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByPathRequest,
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply>(
                service, METHODID_QUERY_SERVICE_META_BY_PATH)))
        .addMethod(
          getQueryServiceMetaByGuidMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByGuidRequest,
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply>(
                service, METHODID_QUERY_SERVICE_META_BY_GUID)))
        .addMethod(
          getEvalCreationStatementMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.EvalCreationStatementRequest,
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply>(
                service, METHODID_EVAL_CREATION_STATEMENT)))
        .addMethod(
          getCreateNewServiceMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.CreateNewServiceRequest,
              com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply>(
                service, METHODID_CREATE_NEW_SERVICE)))
        .build();
  }

  private static abstract class ServiceMetaBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ServiceMetaBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaProto.getDescriptor();
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
              .addMethod(getFetchServicePageMethod())
              .addMethod(getFetchServiceInstancePageMethod())
              .addMethod(getQueryServiceMethod())
              .addMethod(getQueryServiceInstanceMethod())
              .addMethod(getFetchNamespaceChildrenMethod())
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
