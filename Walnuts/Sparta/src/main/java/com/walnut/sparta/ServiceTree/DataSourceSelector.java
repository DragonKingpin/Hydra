package com.walnut.sparta.ServiceTree;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DataSourceSelector {
    @Value("${sparta.datasource:mysql}") // 默认值为 "mysql"
    private String dataSourceName;

    public String selectDataSource() {
        return dataSourceName;
    }
}
