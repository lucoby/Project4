package project4.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import project4.login.model.StudentModel;

public class CourseAnalysis {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String file = "OMS-CS-CourseGrades_Final.csv";
		lpSolve(file);

	}

	public static void lpSolve(String studentInputs) {
		// Setup students
		HashMap<String,ArrayList<String>> courses = new HashMap<String,ArrayList<String>>();
		int numberStudents = 0;
		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(studentInputs);
			br = new BufferedReader(fr);

			String line;
			int lineNum = 0;
			while ((line = br.readLine()) != null) {
				if (line.trim() != null && line.trim().length() > 0
						&& lineNum > 0) {
					
					System.out.println(line);
					// System.out.println(line.replaceAll("[,]+"," "));
					String[] tokens = line.split("[,]+");
					String id = (tokens[4]);
					if(!courses.containsKey(id)) {
						courses.put(id,new ArrayList<String>());
						courses.get(id).add(tokens[3].substring(4));
						System.out.println(tokens[3].substring(4));
					} else {
						boolean sem = false;
						for(String s:courses.get(id)) {
							if (s.equals(tokens[3].substring(4))) {
								sem = true;
							}
						}
						if(!sem) {
							courses.get(id).add(tokens[3].substring(4));
						}
					}
				}
				lineNum++;
			}
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for(String s:courses.keySet()) {
			System.out.println(s);
			for (String sem:courses.get(s)) {
				System.out.println(sem);
			}
		}
	}
}
