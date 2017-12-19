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
	//handler, 包括编码、解码、消息处理
		public static ServerAioHandler aioHandler = new MyServerAioHandler();

		//事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
		public static ServerAioListener aioListener = null;

		//一组连接共用的上下文对象
		public static ServerGroupContext serverGroupContext = new ServerGroupContext(aioHandler, aioListener);

		//aioServer对象
		public static AioServer aioServer = new AioServer(serverGroupContext);

		//有时候需要绑定ip，不需要则null
		public static String serverIp = null;

		//监听的端口
		public static int serverPort = Const.PORT;

		/**
		 * 启动程序入口
		 */
		public static void domain() throws IOException {
			serverGroupContext.setHeartbeatTimeout(Const.TIMEOUT);

			aioServer.start(serverIp, serverPort);
		}

    
}
