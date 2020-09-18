package com.atguigu.springcloud.lb.impl;

import com.atguigu.springcloud.lb.ILoadBalancer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LoadBalancerImpl  implements ILoadBalancer {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 自选锁
     * @return
     */
    public final int getAndIncrement(){
        int current;
        int next;
        do{
            current = this.atomicInteger.get();
            next = current >= 2147483647 ? 0:current+1;
        }while (!this.atomicInteger.compareAndSet(current,next));
        System.out.println("*****次数next:"+next);
        return next;
    }

    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {
        int index = getAndIncrement() % serviceInstances.size();
        return serviceInstances.get(index);
    }


}
