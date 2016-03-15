package Server;

import java.nio.IntBuffer;
import java.util.HashMap;

public class Decoder {
	
	private static HashMap<Integer, String> CodeValues = new HashMap(); 
	private static boolean AlreadyInitialized = false;
	
	public static String DecodeMessage(IntBuffer aMessage)
	{
		String lDecodedMessage = "";
		
		for(int lIndex = 0; lIndex < 300; lIndex++)
		{
			lDecodedMessage += CodeValues.get(aMessage.get(lIndex));
		}
		
		return lDecodedMessage;
	}
	
	public static void CreateCodeValues()
	{
		if(AlreadyInitialized)
			return; //has already been populated
		else
			AlreadyInitialized = true;
		
		//Capital Letters
		CodeValues.put(100, "A");
		CodeValues.put(53532, "B");
		CodeValues.put(32942, "C");
		CodeValues.put(3594289, "D");
		CodeValues.put(94289, "E");
		CodeValues.put(90424, "F");
		CodeValues.put(5, "G");
		CodeValues.put(2315, "H");
		CodeValues.put(7654, "I");
		CodeValues.put(3248, "J");
		CodeValues.put(8679, "K");
		CodeValues.put(868, "L");
		CodeValues.put(29, "M");
		CodeValues.put(2277, "N");
		CodeValues.put(7646, "O");
		CodeValues.put(77, "P");
		CodeValues.put(9999, "Q");
		CodeValues.put(325, "R");
		CodeValues.put(886637, "S");
		CodeValues.put(5902, "T");
		CodeValues.put(6603, "U");
		CodeValues.put(232, "V");
		CodeValues.put(4, "W");
		CodeValues.put(909, "X");
		CodeValues.put(55558, "Y");
		CodeValues.put(221789, "Z");
		
		//Lower Letters
		CodeValues.put(10090, "a");
		CodeValues.put(8, "b");
		CodeValues.put(2390, "c");
		CodeValues.put(321, "d");
		CodeValues.put(942829, "e");
		CodeValues.put(54525, "f");
		CodeValues.put(3, "g");
		CodeValues.put(324922, "h");
		CodeValues.put(22147, "i");
		CodeValues.put(57432, "j");
		CodeValues.put(247, "k");
		CodeValues.put(112, "l");
		CodeValues.put(9324, "m");
		CodeValues.put(28967, "n");
		CodeValues.put(353566, "o");
		CodeValues.put(2021, "p");
		CodeValues.put(2389, "q");
		CodeValues.put(12345678, "r");
		CodeValues.put(905634, "s");
		CodeValues.put(2221, "t");
		CodeValues.put(23134, "u");
		CodeValues.put(8987, "v");
		CodeValues.put(33226, "w");
		CodeValues.put(218, "x");
		CodeValues.put(919, "y");
		CodeValues.put(3774872, "z");
		
		//Numbers
		CodeValues.put(4812, "0");
		CodeValues.put(652, "1");
		CodeValues.put(21209, "2");
		CodeValues.put(2784, "3");
		CodeValues.put(3856, "4");
		CodeValues.put(221133, "5");
		CodeValues.put(784321, "6");
		CodeValues.put(3214, "7");
		CodeValues.put(83, "8");
		CodeValues.put(4312, "9");
		
		//Special Characters
		CodeValues.put(614324, ":");
		CodeValues.put(8873, "-");
		CodeValues.put(48907, "|");
		CodeValues.put(24571, " ");
	}

}
