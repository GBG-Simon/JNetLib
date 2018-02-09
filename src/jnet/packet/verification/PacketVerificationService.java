package jnet.packet.verification;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jnet.packet.verification.IPacketVerificationSubsystem.EVerificationResult;
import jnet.packet.IPacket;

public class PacketVerificationService
{
	public static enum EVerificationMethod
	{
		CRC32("CRC32", PacketVerificationSubsystem_CRC32.class),
		ADLER32("ADLER32", PacketVerificationSubsystem_ADLER32.class),
		MD5("MD5", PacketVerificationSubsystem_MD5.class),
		SHA1("SHA-1", PacketVerificationSubsystem_SHA1.class);
		
		private EVerificationMethod(String code, Class<? extends IPacketVerificationSubsystem> subsystemClass)
		{
			this.Code = code;
			this.SubsystemClass = subsystemClass;
		}
		
		public final String Code;
		private final Class<? extends IPacketVerificationSubsystem> SubsystemClass;
	}	
	
	public static class VerificationArgs
	{
		public VerificationArgs(String subsystemName)
		{
			this.subsystemName = subsystemName;
		}		
		
		public String subsystemName;
	}
	
	static void registerSubsystem(String name, Class<? extends IPacketVerificationSubsystem> subsystemClass)
	{
		if (name == null || subsystemClass == null || subsystemClasses.containsKey(name))
		{
			return;
		}
		
		subsystemClasses.put(name, subsystemClass);
	}
	
	static void unregisterSubsystem(String name)
	{
		if (name == null || !subsystemClasses.containsKey(name))
		{
			return;
		}
		
		subsystemClasses.remove(name);
	}
	
	private static Map<String, Class<? extends IPacketVerificationSubsystem>> subsystemClasses = new HashMap<>();
	
	static
	{
		for (EVerificationMethod method : EVerificationMethod.values())
		{
			registerSubsystem(method.Code, method.SubsystemClass);
		}
	}
	
	public void validate(IPacket<? extends Serializable> packet)
	{
		
	}
	
	public void invalidate(IPacket<? extends Serializable> packet)
	{
		
	}
	
	public EVerificationResult verify(IPacket<? extends Serializable> packet, Optional<VerificationArgs> optionalArgs)
	{
		Class<? extends IPacketVerificationSubsystem> subsystemClass = verificationMethod.SubsystemClass;
		
		if (optionalArgs.isPresent())
		{
			VerificationArgs args = optionalArgs.get();
		}
		
		return null;
		
	}
	
	private EVerificationMethod verificationMethod;
	
}
