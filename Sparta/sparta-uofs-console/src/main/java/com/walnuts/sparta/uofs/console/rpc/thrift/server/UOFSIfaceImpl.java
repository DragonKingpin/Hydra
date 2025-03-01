package com.walnuts.sparta.uofs.console.rpc.thrift.server;

import com.walnuts.sparta.uofs.thrift.UOFSIface;
import org.apache.thrift.TException;
import org.springframework.stereotype.Component;

@Component
public class UOFSIfaceImpl implements UOFSIface.Iface {
    @Override
    public String test(String msg) throws TException {
        System.out.println( msg );
        return null;
    }
}
