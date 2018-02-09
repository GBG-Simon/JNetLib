package jnet.packet;

import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class PacketDecoder extends ReplayingDecoder<PacketDecoderState>
{
	public PacketDecoder()
	{
		super(PacketDecoderState.READ_HEADER);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
	{
		// PacketCryptoManager.decrypt(ByteBuf rawPacketData);
		
		switch (state())
		{
		case READ_LENGTH:
			break;
		case READ_HEADER:
			break;
		case READ_CONTENT:
			break;
		case READ_CHECKSUM:
			break;
		default:
			break;
		}
	}
	
}

enum PacketDecoderState
{
	READ_LENGTH,
	READ_HEADER,
	READ_CONTENT,
	READ_CHECKSUM
}
