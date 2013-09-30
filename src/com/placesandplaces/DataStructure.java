package com.placesandplaces;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;



/*----------------

this class used as a working example - not 
for final submission or evaluation/grading purposes.

----------------*/

class DataStructure {
	public DataStructure(int type, ArrayList<Integer> indexPos,
			ArrayList<Integer> zeroOne, ArrayList<String> fullString,
			ArrayList<String> upDown,int supportCount) {
		super();
		this.type = type;
		this.indexPos = indexPos;
		this.zeroOne = zeroOne;
		this.fullString = fullString;
		this.upDown = upDown;
		this.supportCount = supportCount;
	}
	int type;
	ArrayList<Integer>indexPos;
	ArrayList<Integer>zeroOne;
	ArrayList<String>fullString;
	ArrayList<String>upDown;
	int supportCount;
}
