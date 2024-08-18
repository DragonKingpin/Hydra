//package com.walnut.sparta.serviceTree;
//
//import com.walnut.sparta.serviceTree.Interface.DistributedScopeTreeDao;
//import com.walnut.sparta.mapper.ServiceNodeMapper;
//
//public class DataSourceFactory {
//
//    private ServiceNodeMapper mysqlDao;
//    private final String dataSource;
//
//    public DataSourceFactory(String dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    public DistributedScopeTreeDao createDataAccess() {
//        if ("mysql".equals(this.dataSource)) {
//            return mysqlDao;
//        }
////        } else if ("redis".equals(dataSource)) {
////            return redisDao;
////        }
//        throw new IllegalStateException("Unsupported data source: " + dataSource);
//    }
//}
