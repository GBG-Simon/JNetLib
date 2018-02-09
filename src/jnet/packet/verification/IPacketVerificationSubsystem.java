package jnet.packet.verification;

import java.io.Serializable;

import jnet.packet.IPacket;

public interface IPacketVerificationSubsystem
{
	public boolean isValid(IPacket<? extends Serializable> packet);
	public void validate(IPacket<? extends Serializable> packet);
	public void invalidate(IPacket<? extends Serializable> packet);
	
	public static enum EVerificationResult
	{
		SUCCESS(0x01),
		
		FAILURE(0x0A),
		ERR_INVALID_PACKET(0x0B),
		ERR_INVALID_SUBSYSTEM(0x0C);
		
		private EVerificationResult(int value)
		{
			this.Value = value;
		}
		
		public final int Value;
	}
}
