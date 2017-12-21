package org.laopopo.example.generic.test_3;

import org.laopopo.client.consumer.ConsumerClient;
import org.laopopo.client.consumer.ConsumerConfig;
import org.laopopo.client.consumer.Consumer.SubscribeManager;
import org.laopopo.client.consumer.proxy.ProxyFactory;
import org.laopopo.common.exception.rpc.NoServiceException;
import org.laopopo.remoting.netty.NettyClientConfig;

public class ConsumerTest {
	
	public static void main(String[] args) throws Exception  {
		
		NettyClientConfig registryNettyClientConfig = new NettyClientConfig();
		registryNettyClientConfig.setDefaultAddress("127.0.0.1:18010");

		NettyClientConfig provideClientConfig = new NettyClientConfig();

		ConsumerClient client = new ConsumerClient(registryNettyClientConfig, provideClientConfig, new ConsumerConfig());

		client.start();
		
		SubscribeManager subscribeManager = client.subscribeService("LAOPOPO.TEST.SAYHELLO");

		if (!subscribeManager.waitForAvailable(3000l)) {
			throw new NoServiceException("no service provider");
		}
		
		HelloService helloService = ProxyFactory.factory(HelloService.class).consumer(client).timeoutMillis(3000l).newProxyInstance();
		
		for(int index = 0;index < 100000;index++){
			
			String str = helloService.sayHello("Lyncc");
			
			System.out.println(str);
			
			Thread.sleep(500l);
		}
		
		
	}

}
