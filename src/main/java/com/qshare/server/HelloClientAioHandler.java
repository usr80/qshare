package com.qshare.server;

import java.nio.ByteBuffer;

import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

import com.qshare.common.HelloPacket;

public class HelloClientAioHandler implements ClientAioHandler {
	private static HelloPacket heartbeatPacket = new HelloPacket();


	/**
	 * ���룺�ѽ��յ���ByteBuffer�������Ӧ�ÿ���ʶ���ҵ����Ϣ��
	 * �ܵ���Ϣ�ṹ����Ϣͷ + ��Ϣ��
	 * ��Ϣͷ�ṹ��    4���ֽڣ��洢��Ϣ��ĳ���
	 * ��Ϣ��ṹ��   �����json����byte[]
	 */
	public HelloPacket decode(ByteBuffer buffer, ChannelContext channelContext) throws AioDecodeException {
		int readableLength = buffer.limit() - buffer.position();
		//�յ��������鲻��ҵ������򷵻�null�Ը��߿�����ݲ���
		if (readableLength < HelloPacket.HEADER_LENGHT) {
			return null;
		}

		//��ȡ��Ϣ��ĳ���
		int bodyLength = buffer.getInt();

		//���ݲ���ȷ�����׳�AioDecodeException�쳣
		if (bodyLength < 0) {
			throw new AioDecodeException("bodyLength [" + bodyLength + "] is not right, remote:" + channelContext.getClientNode());
		}

		//���㱾����Ҫ�����ݳ���
		int neededLength = HelloPacket.HEADER_LENGHT + bodyLength;
		//�յ��������Ƿ��㹻���
		int isDataEnough = readableLength - neededLength;
		// ������Ϣ�峤��(ʣ�µ�buffe�鲻����Ϣ��)
		if (isDataEnough < 0) {
			return null;
		} else //����ɹ�
		{
			HelloPacket imPacket = new HelloPacket();
			if (bodyLength > 0) {
				byte[] dst = new byte[bodyLength];
				buffer.get(dst);
				imPacket.setBody(dst);
			}
			return imPacket;
		}
	}

	/**
	 * ���룺��ҵ����Ϣ������Ϊ���Է��͵�ByteBuffer
	 * �ܵ���Ϣ�ṹ����Ϣͷ + ��Ϣ��
	 * ��Ϣͷ�ṹ��    4���ֽڣ��洢��Ϣ��ĳ���
	 * ��Ϣ��ṹ��   �����json����byte[]
	 */
	public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
		HelloPacket helloPacket = (HelloPacket) packet;
		byte[] body = helloPacket.getBody();
		int bodyLen = 0;
		if (body != null) {
			bodyLen = body.length;
		}

		//bytebuffer���ܳ����� = ��Ϣͷ�ĳ��� + ��Ϣ��ĳ���
		int allLen = HelloPacket.HEADER_LENGHT + bodyLen;
		//����һ���µ�bytebuffer
		ByteBuffer buffer = ByteBuffer.allocate(allLen);
		//�����ֽ���
		buffer.order(groupContext.getByteOrder());

		//д����Ϣͷ----��Ϣͷ�����ݾ�����Ϣ��ĳ���
		buffer.putInt(bodyLen);

		//д����Ϣ��
		if (body != null) {
			buffer.put(body);
		}
		return buffer;
	}
	
	/**
	 * ������Ϣ
	 */
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {
		HelloPacket helloPacket = (HelloPacket) packet;
		byte[] body = helloPacket.getBody();
		if (body != null) {
			String str = new String(body, HelloPacket.CHARSET);
			System.out.println("�յ���Ϣ��" + str);
		}

		return;
	}

	/**
	 * �˷����������null����ܲ����򲻻ᷢ������������ط�null����ܲ���ᶨʱ�����������ص���Ϣ��
	 */
	public HelloPacket heartbeatPacket() {
		return heartbeatPacket;
	}
}