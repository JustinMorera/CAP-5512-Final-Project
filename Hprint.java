/******************************************************************************
*  Hprint Class					    Final Version prepared by Hal Stringer, UCF
*
*  This class provides support for formatted output to the system console.
*  Most methods in the class accept two parameters: a primitive data
*  value that is to be printed and 2) a field size.  Each method will right
*  justify, left justify or center the data value within the specified field and
*  send the final string to the console.  For double values, the programmer
*  can optionally specify the number of decimal places to display.  Digits
*  occuring after the display point are rounded off.  Values that will not fit
*  within the specified field length will be replaced by #### symbols.
*
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class Hprint {

/*******************************************************************************
*                       STATIC VARIABLES (CLASS VARIABLES)                     *
*******************************************************************************/

//	Variable "pad" set to 50 spaces - determines maximum field size.
//	To increase maximum field length, add more spaces to the pad string.

	private static final String pad = "                                                  ";

/*******************************************************************************
*                     STATIC PRINTING METHODS (CLASS METHODS)                  *
*******************************************************************************/

	//	Print Left Justified Integer
	public static void left(int data, int fieldLength){
		String dataText = Integer.toString(data);
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength);
			return;
		}
		String padding = pad.substring(0, fieldLength-dataText.length());
		System.out.print(dataText + padding);
		return;
	}
	//	Print Right Justified Integer
	public static void right(int data, int fieldLength){
		String dataText = Integer.toString(data);
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength);
			return;
		}
		String padding = pad.substring(0, fieldLength-dataText.length());
		System.out.print(padding + dataText);
		return;
	}
	//	Print Centered Integer
	public static void center(int data, int fieldLength){
		String dataText = Integer.toString(data);
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength);
			return;
		}
		int totalPadLength = fieldLength - dataText.length();
		int endPadLength = totalPadLength / 2;
		int startPadLength = totalPadLength - endPadLength;
		String startPadding = pad.substring(0, startPadLength);
		String endPadding = pad.substring(0, endPadLength);
		System.out.print(startPadding + dataText + endPadding);
		return;
	}

/*******************************************************************************/

	//	Print Left Justified Double - Default Decimal Places
	public static void left(double data, int fieldLength){
		String dataText = Double.toString(data);
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength);
			return;
		}
		String padding = pad.substring(0, fieldLength-dataText.length());
		System.out.print(dataText + padding);
		return;
	}
	//	Print Right Justified Double - Default Decimal Places
	public static void right(double data, int fieldLength){
		String dataText = Double.toString(data);
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength);
			return;
		}
		String padding = pad.substring(0, fieldLength-dataText.length());
		System.out.print(padding + dataText);
		return;
	}
	//	Print Centered Double - Default Decimal Places
	public static void center(double data, int fieldLength){
		String dataText = Double.toString(data);
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength);
			return;
		}
		int totalPadLength = fieldLength - dataText.length();
		int endPadLength = totalPadLength / 2;
		int startPadLength = totalPadLength - endPadLength;
		String startPadding = pad.substring(0, startPadLength);
		String endPadding = pad.substring(0, endPadLength);
		System.out.print(startPadding + dataText + endPadding);
		return;
	}

/*******************************************************************************/

	//	Print Left Justified Double - User Specified Decimal Places
	public static void left(double data, int fieldLength, int places){
		String dataText = fixDecimals(data, places);
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength);
			return;
		}
		String padding = pad.substring(0, fieldLength-dataText.length());
		System.out.print(dataText + padding);
		return;
	}
	//	Print Right Justified Double - User Specified Decimal Places
	public static void right(double data, int fieldLength, int places){
		String dataText = fixDecimals(data, places);
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength);
			return;
		}
		String padding = pad.substring(0, fieldLength-dataText.length());
		System.out.print(padding + dataText);
		return;
	}
	//	Print Centered Double - User Specified Decimal Places
	public static void center(double data, int fieldLength, int places){
		String dataText = fixDecimals(data, places);
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength);
			return;
		}
		int totalPadLength = fieldLength - dataText.length();
		int endPadLength = totalPadLength / 2;
		int startPadLength = totalPadLength - endPadLength;
		String startPadding = pad.substring(0, startPadLength);
		String endPadding = pad.substring(0, endPadLength);
		System.out.print(startPadding + dataText + endPadding);
		return;
	}

/*******************************************************************************/

	//	Print Left Justified String
	public static void left(String dataText, int fieldLength){
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength);
			return;
		}
		String padding = pad.substring(0, fieldLength-dataText.length());
		System.out.print(dataText + padding);
		return;
	}
	//	Print Right Justified String
	public static void right(String dataText, int fieldLength){
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength);
			return;
		}
		String padding = pad.substring(0, fieldLength-dataText.length());
		System.out.print(padding + dataText);
		return;
	}
	//	Print Centered String
	public static void center(String dataText, int fieldLength){
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength);
			return;
		}
		int totalPadLength = fieldLength - dataText.length();
		int endPadLength = totalPadLength / 2;
		int startPadLength = totalPadLength - endPadLength;
		String startPadding = pad.substring(0, startPadLength);
		String endPadding = pad.substring(0, endPadLength);
		System.out.print(startPadding + dataText + endPadding);
		return;
	}

/*******************************************************************************/

	private static void fieldLengthError(int fieldLength){
		for(int i=1; i<=fieldLength; ++i){
			System.out.print("#");
		}
		return;
	}

	private static String fixDecimals(double data, int places){
		data=Math.round(data * Math.pow(10,places)) / Math.pow(10,places);
		String dataText = Double.toString(data);
		return(dataText);
	}

}   // End of Hprint.java