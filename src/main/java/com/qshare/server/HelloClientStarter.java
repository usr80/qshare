package com.qshare.server;

import java.nio.ByteBuffer;

import org.tio.client.AioClient;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Aio;
import org.tio.core.Node;
import com.qshare.common.Const;
import com.qshare.common.HelloPacket;

public class HelloClientStarter {
	//服务器节点
		public static Node serverNode = new Node(Const.SERVER, Const.PORT);

		//handler, 包括编码、解码、消息处理
		public static HelloClientAioHandler aioClientHandler = new HelloClientAioHandler();

		//事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
		public static ClientAioListener aioListener = null;

		//断链后自动连接的，不想自动连接请设为null
		private static ReconnConf reconnConf = new ReconnConf(5000L);

		//一组连接共用的上下文对象
		public static ClientGroupContext clientGroupContext = new ClientGroupContext(aioClientHandler, aioListener, reconnConf);

		public static AioClient aioClient = null;
		public static ClientChannelContext clientChannelContext = null;

		/**
		 * 启动程序入口
		 */
		public static void main(String[] args) throws Exception {
			clientGroupContext.setHeartbeatTimeout(Const.TIMEOUT);
			aioClient = new AioClient(clientGroupContext);
			clientChannelContext = aioClient.connect(serverNode);
			//连上后，发条消息玩玩
			send();
			
			HelloPacket packet = new HelloPacket();
			packet.setBody("hello world".getBytes(HelloPacket.CHARSET));
			ByteBuffer buffer = aioClientHandler.encode(packet, clientGroupContext, clientChannelContext);
			
			for (int i = 0; i < buffer.array().length; i++) {
				System.out.print(Integer.toHexString(buffer.array()[i]));
			}
		}

		private static void send() throws Exception {
			HelloPacket packet = new HelloPacket();
			packet.setBody("hello world".getBytes(HelloPacket.CHARSET));
			System.out.println(packet.getPreEncodedByteBuffer());
			Aio.send(clientChannelContext, packet);
		}
}
