/******************************************************************************
*  Hwrite Class					    Final Version prepared by Hal Stringer, UCF
*
*  This class provides support for formatted output to a FileWriter called
*  The pointer to the FileWriter is passed to the method along with the output
*  data and any formatting parameters.
*
*  Most methods in the class accept two parameters: a primitive data
*  value that is to be printed and 2) a field size.  Each method will right
*  justify, left justify or center the data value within the specified field and
*  send the final string to output file.  For double values, the programmer
*  can optionally specify the number of decimal places to output.  Digits
*  occuring after the number specified are rounded off.  Values that do not fit
*  within the specified field length will be replaced by #### symbols.
*
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class Hwrite {

/*******************************************************************************
*                       STATIC VARIABLES (CLASS VARIABLES)                     *
*******************************************************************************/

//	Variable "pad" set to 50 spaces - determines maximum field size.
//	To increase maximum field length, add more spaces to the pad string.

	private static final String pad = "                                                  ";

/*******************************************************************************
*                     STATIC PRINTING METHODS (CLASS METHODS)                  *
*******************************************************************************/

	//	Write Left Justified Integer
	public static void left(int data, int fieldLength, FileWriter output) throws java.io.IOException {
		String dataText = Integer.toString(data);
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength, output);
			return;
		}
		String padding = pad.substring(0, fieldLength-dataText.length());
		output.write(dataText + padding);
		return;
	}

	//	Write Right Justified Integer
	public static void right(int data, int fieldLength, FileWriter output) throws java.io.IOException {
		String dataText = Integer.toString(data);
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength, output);
			return;
		}
		String padding = pad.substring(0, fieldLength-dataText.length());
		output.write(padding + dataText);
		return;
	}

	//	Write Centered Integer
	public static void center(int data, int fieldLength, FileWriter output) throws java.io.IOException {
		String dataText = Integer.toString(data);
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength, output);
			return;
		}
		int totalPadLength = fieldLength - dataText.length();
		int endPadLength = totalPadLength / 2;
		int startPadLength = totalPadLength - endPadLength;
		String startPadding = pad.substring(0, startPadLength);
		String endPadding = pad.substring(0, endPadLength);
		output.write(startPadding + dataText + endPadding);
		return;
	}

/*******************************************************************************/

	//	Write Left Justified Double - Default Decimal Places
	public static void left(double data, int fieldLength, FileWriter output) throws java.io.IOException {
		String dataText = Double.toString(data);
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength, output);
			return;
		}
		String padding = pad.substring(0, fieldLength-dataText.length());
		output.write(dataText + padding);
		return;
	}

	//	Write Right Justified Double - Default Decimal Places
	public static void right(double data, int fieldLength, FileWriter output) throws java.io.IOException {
		String dataText = Double.toString(data);
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength, output);
			return;
		}
		String padding = pad.substring(0, fieldLength-dataText.length());
		output.write(padding + dataText);
		return;
	}

	//	Write Centered Double - Default Decimal Places
	public static void center(double data, int fieldLength, FileWriter output) throws java.io.IOException {
		String dataText = Double.toString(data);
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength, output);
			return;
		}
		int totalPadLength = fieldLength - dataText.length();
		int endPadLength = totalPadLength / 2;
		int startPadLength = totalPadLength - endPadLength;
		String startPadding = pad.substring(0, startPadLength);
		String endPadding = pad.substring(0, endPadLength);
		output.write(startPadding + dataText + endPadding);
		return;
	}

/*******************************************************************************/

	//	Write Left Justified Double - User Specified Decimal Places
	public static void left(double data, int fieldLength, int places, FileWriter output) throws java.io.IOException {
		String dataText = fixDecimals(data, places);
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength, output);
			return;
		}
		String padding = pad.substring(0, fieldLength-dataText.length());
		output.write(dataText + padding);
		return;
	}

	//	Write Right Justified Double - User Specified Decimal Places
	public static void right(double data, int fieldLength, int places, FileWriter output) throws java.io.IOException {
		String dataText = fixDecimals(data, places);
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength, output);
			return;
		}
		String padding = pad.substring(0, fieldLength-dataText.length());
		output.write(padding + dataText);
		return;
	}

	//	Write Centered Double - User Specified Decimal Places
	public static void center(double data, int fieldLength, int places, FileWriter output) throws java.io.IOException {
		String dataText = fixDecimals(data, places);
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength, output);
			return;
		}
		int totalPadLength = fieldLength - dataText.length();
		int endPadLength = totalPadLength / 2;
		int startPadLength = totalPadLength - endPadLength;
		String startPadding = pad.substring(0, startPadLength);
		String endPadding = pad.substring(0, endPadLength);
		output.write(startPadding + dataText + endPadding);
		return;
	}

/*******************************************************************************/

	//	Write Left Justified String
	public static void left(String dataText, int fieldLength, FileWriter output) throws java.io.IOException {
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength, output);
			return;
		}
		String padding = pad.substring(0, fieldLength-dataText.length());
		output.write(dataText + padding);
		return;
	}

	//	Write Right Justified String
	public static void right(String dataText, int fieldLength, FileWriter output) throws java.io.IOException {
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength, output);
			return;
		}
		String padding = pad.substring(0, fieldLength-dataText.length());
		output.write(padding + dataText);
		return;
	}

	//	Write Centered String
	public static void center(String dataText, int fieldLength, FileWriter output) throws java.io.IOException {
		if (fieldLength < dataText.length()){
			fieldLengthError(fieldLength, output);
			return;
		}
		int totalPadLength = fieldLength - dataText.length();
		int endPadLength = totalPadLength / 2;
		int startPadLength = totalPadLength - endPadLength;
		String startPadding = pad.substring(0, startPadLength);
		String endPadding = pad.substring(0, endPadLength);
		output.write(startPadding + dataText + endPadding);
		return;
	}

/*******************************************************************************/

	private static void fieldLengthError(int fieldLength, FileWriter output) throws java.io.IOException {
		for(int i=1; i<=fieldLength; ++i){
			output.write("#");
		}
		return;
	}

	private static String fixDecimals(double data, int places){
		data=Math.round(data * Math.pow(10,places)) / Math.pow(10,places);
		String dataText = Double.toString(data);
		return(dataText);
	}

}   // End of Hwrite.java