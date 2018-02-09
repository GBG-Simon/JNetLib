package jnet.packet.verification;

import java.io.Serializable;

import jnet.packet.IPacket;

public class PacketVerificationSubsystem_ADLER32 implements IPacketVerificationSubsystem
{

	@Override
	public boolean isValid(IPacket<? extends Serializable> packet)
	{
		return false;
	}

	@Override
	public void validate(IPacket<? extends Serializable> packet)
	{
		
	}

	@Override
	public void invalidate(IPacket<? extends Serializable> packet)
	{

	}

}
