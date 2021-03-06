package com.qinyadan.monitor.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ResponsePacket extends BasicPacket {

	private int requestId;

	public ResponsePacket() {
	}

	public ResponsePacket(byte[] payload) {
		super(payload);
	}

	public ResponsePacket(int requestId, byte[] payload) {
		super(payload);
		this.requestId = requestId;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	@Override
	public short getPacketType() {
		return PacketType.APPLICATION_RESPONSE;
	}

	@Override
	public ByteBuf toBuffer() {

		ByteBuf header = Unpooled.buffer(2 + 4 + 4);
		header.writeShort(PacketType.APPLICATION_RESPONSE);
		header.writeInt(requestId);

		return PayloadPacket.appendPayload(header, payload);
	}

	public static ResponsePacket readBuffer(short packetType, ByteBuf buffer) {
		assert packetType == PacketType.APPLICATION_RESPONSE;

		if (buffer.readableBytes() < 8) {
			buffer.resetReaderIndex();
			return null;
		}

		final int messageId = buffer.readInt();
		ByteBuf payload = PayloadPacket.readPayload(buffer);
		if (payload == null) {
			return null;
		}
		ResponsePacket responsePacket = new ResponsePacket(payload.array());
		responsePacket.setRequestId(messageId);

		return responsePacket;

	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("ResponsePacket");
		sb.append("{requestId=").append(requestId);
		sb.append(", ");
		if (payload == null) {
			sb.append("payload=null");
		} else {
			sb.append("payloadLength=").append(payload.length);
		}
		sb.append('}');
		return sb.toString();
	}
}
