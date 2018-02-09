package jnet.packet;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Calendar;
import java.util.Date;

public class PacketVersion implements Externalizable, Comparable<PacketVersion>, Cloneable
{
	public final static PacketVersion INVALID_VERSION = new PacketVersion((byte)-1, (byte)-1, (byte)-1, (byte)-1);
//	public final static PacketVersion CURRENT_VERSION = new PacketVersion((byte)18, (byte)1, (byte)9, (byte)PacketHeader.getChecksumManager().CurrentlyUsedAlgorithm);
	
	public PacketVersion()
	{
		this(INVALID_VERSION);
	}
	
	public PacketVersion(final PacketVersion other)
	{
		this(other._1, other._2, other._3, other._4, other.compatibleVersion);
	}
	
	public PacketVersion(byte _1, byte _2, byte _3, byte _4)
	{
		this(_1, _2, _3, _4, null);
	}
	
	public PacketVersion(byte _1, byte _2, byte _3, byte _4, PacketVersion compatibleVersion)
	{
		this._1 = _1;
		this._2 = _2;
		this._3 = _3;
		this._4 = _4;
		this.compatibleVersion = compatibleVersion;
	}
	
	// TODO: implement this method
	public static PacketVersion fromDate(Date date)
	{
		return null;
	}
	
	public static PacketVersion valueOf(int compressedData)
	{
		byte[] extractedData = extractVersionData(compressedData);
		
		return new PacketVersion(extractedData[0], extractedData[1], extractedData[2], extractedData[3]);
	}
	
	public static PacketVersion valueOf(long compactData)
	{
		PacketVersion myVersion = valueOf((int) (compactData & 0xFFFFFFFFL));		
		myVersion.compatibleVersion = valueOf((int) (compactData >>> 32));
		
		return myVersion;
	}
	
	private static byte[] extractVersionData(int compactVersionData)
	{
		return new byte[]
				{ 
					(byte) ((compactVersionData /* & 0xFF000000 */) >>> 25), 
					(byte) ((compactVersionData & 0x00FF0000) >>> 17), 
					(byte) ((compactVersionData & 0x0000FF00) >>> 9), 
					(byte)  (compactVersionData & 0x000000FF)
				};
	}
	
	public boolean isLowerThan(PacketVersion version)
	{
		if (version == null)
		{
			return false;
		}
		
		return this._1 >= version._1? 
				(this._1 == version._1? 
						(this._2 >= version._2? 
								(this._2 == version._2? (this._3 < version._3) : false)
						: true)
				: false)
			   : false;
	}
	
	public boolean isSameWith(PacketVersion version)
	{
		if (version == null)
		{
			return false;
		}
		
		return this._1 == version._1 && this._2 == version._2 && this._3 == version._3;
	}
	
	public boolean isHigherThan(PacketVersion version)
	{
		return !(isSameWith(version) || isLowerThan(version));
	}
	
	public boolean isCompatibleWith(PacketVersion version)
	{
		return isSameWith(version) || isLowerThan(version);
	}
	
	public int getCompressed()
	{
		return _1 << 23 | _2 << 15 | _3 << 7 | _4;
	}
	
	public long getCompactData()
	{
		return compatibleVersion == null? (long) getCompressed() : ((long)compatibleVersion.getCompressed()) << 32 | getCompressed();
	}
	
	public String toCompactString()
	{
		return String.format("%d.%d.%d_%d", _1, _2, _3, _4);
	}
	
	@Override
	public String toString()
	{
		return String.format("PacketVersion: %d.%d.%d_%d, Compatible PacketVersion: %s", _1, _2, _3, _4, compatibleVersion);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof PacketVersion)
		{
			PacketVersion version = (PacketVersion) obj;
			
			return this._1 == version._1 && this._2 == version._2 && this._3 == version._3 && this._4 == version._4;
		}
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return getCompressed();
	}
	
	@Override
	public int compareTo(PacketVersion version)
	{
		if (isSameWith(version))
		{
			return 0;
		}
		
		else if (isLowerThan(version))
		{
			return -1;
		}
		
		return 1;
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeLong(getCompactData());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		PacketVersion versionData = valueOf(in.readLong());
		
		this._1 = versionData._1;
		this._2 = versionData._2;
		this._3 = versionData._3;
		this._4 = versionData._4;
		this.compatibleVersion = versionData.compatibleVersion;
	}
	
	/**
	 * First element of PacketVersion (typically the last two digits of the year when the version was made e.g. 18 for 2018)
	 */
	private byte _1;
	
	/**
	 * Second element of PacketVersion (typically the month when the version was made e.g. 01 for January)
	 */
	private byte _2;
	
	/**
	 * Third element of PacketVersion (typically the date when the version was made)
	 */
	private byte _3;
	
	/**
	 * Last element of PacketVersion (typically used as a custom number to provide certain information)
	 */
	private byte _4;
	
	PacketVersion compatibleVersion;
	
	public PacketVersion getCompatibleVersion()
	{
		return compatibleVersion;
	}
}
