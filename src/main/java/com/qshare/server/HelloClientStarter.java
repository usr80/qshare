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
	//�������ڵ�
		public static Node serverNode = new Node(Const.SERVER, Const.PORT);

		//handler, �������롢���롢��Ϣ����
		public static HelloClientAioHandler aioClientHandler = new HelloClientAioHandler();

		//�¼�������������Ϊnull���������Լ�ʵ�ָýӿڣ����Բο�showcase�˽�Щ�ӿ�
		public static ClientAioListener aioListener = null;

		//�������Զ����ӵģ������Զ���������Ϊnull
		private static ReconnConf reconnConf = new ReconnConf(5000L);

		//һ�����ӹ��õ������Ķ���
		public static ClientGroupContext clientGroupContext = new ClientGroupContext(aioClientHandler, aioListener, reconnConf);

		public static AioClient aioClient = null;
		public static ClientChannelContext clientChannelContext = null;

		/**
		 * �����������
		 */
		public static void main(String[] args) throws Exception {
			clientGroupContext.setHeartbeatTimeout(Const.TIMEOUT);
			aioClient = new AioClient(clientGroupContext);
			clientChannelContext = aioClient.connect(serverNode);
			//���Ϻ󣬷�����Ϣ����
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
