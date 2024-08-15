package com.walnut.sparta.ServiceTree;

import com.walnut.sparta.ServiceTree.Interface.ServiceTreeDao;
import com.walnut.sparta.mapper.SystemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataSourceFactory {

    private final SystemMapper mysqlDao;
    private final DataSourceSelector dataSourceSelector;

    @Autowired
    public DataSourceFactory(SystemMapper mysqlDao,  DataSourceSelector dataSourceSelector) {
        this.mysqlDao = mysqlDao;
        this.dataSourceSelector = dataSourceSelector;
    }

    public ServiceTreeDao createDataAccess() {
        String dataSource = dataSourceSelector.selectDataSource();
        if ("mysql".equals(dataSource)) {
            return mysqlDao;
        }
//        } else if ("redis".equals(dataSource)) {
//            return redisDao;
//        }
        throw new IllegalStateException("Unsupported data source: " + dataSource);
    }
}
