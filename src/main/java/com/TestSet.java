package com;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class TestSet {

	public static void main(String[] args){
		Set<String> s1 = new HashSet<String>();
		s1.add("1");
		s1.add("2");
		s1.add("3");
        s1.add("5");
		s1.add("4");
		Set<String> s2 = new LinkedHashSet<String>();
		s2.add("5");


		s2.addAll(s1);

		for (String str : s2) {
			System.out.print(str);
		}

	}


}
