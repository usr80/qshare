package com.qshare.services;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.tio.server.AioServer;
import org.tio.server.ServerGroupContext;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;
import org.tio.websocket.server.WsServerStarter;
import org.tio.websocket.server.handler.IWsMsgHandler;

import com.qshare.common.Const;
import com.qshare.server.MyServerAioHandler;

@Service
public class ServerService{
	//handler, �������롢���롢��Ϣ����
		public static ServerAioHandler aioHandler = new MyServerAioHandler();

		//�¼�������������Ϊnull���������Լ�ʵ�ָýӿڣ����Բο�showcase�˽�Щ�ӿ�
		public static ServerAioListener aioListener = null;

		//һ�����ӹ��õ������Ķ���
		public static ServerGroupContext serverGroupContext = new ServerGroupContext(aioHandler, aioListener);

		//aioServer����
		public static AioServer aioServer = new AioServer(serverGroupContext);

		//��ʱ����Ҫ��ip������Ҫ��null
		public static String serverIp = null;

		//�����Ķ˿�
		public static int serverPort = Const.PORT;

		/**
		 * �����������
		 */
		public static void domain() throws IOException {
			serverGroupContext.setHeartbeatTimeout(Const.TIMEOUT);

			aioServer.start(serverIp, serverPort);
		}

    
}
