package project4.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import project4.login.model.StudentModel;

public class PasswordGenService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String students = "OMS-CS-CourseGrades_Final.csv";
		passwordGen(students);
	}

	
	public static void passwordGen(String studentInputs) {
		// Setup students
		ArrayList<StudentModel> students = new ArrayList<StudentModel>(600);
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
					StudentModel s = null;
					System.out.println(line);
					// System.out.println(line.replaceAll("[,]+"," "));
					
					String[] tokens = line.split("[,]+");
					String id = (tokens[0] + "," + tokens[1] + "," + tokens[2])
							.replace("\"", "");
					if (numberStudents - 1 >= 0
							&& students.get(numberStudents - 1).getId()
									.equals(id)) {
						s = students.get(numberStudents - 1);
					} else {
						s = new StudentModel(id);
						students.add(s);
						numberStudents++;
					}
					if (tokens.length == 9) {
						if (s.getCoursesTaken() == null) {
							s.setCoursesTaken(new ArrayList<String>());
							s.getCoursesTaken().add(tokens[4]);
						} else {
							s.getCoursesTaken().add(tokens[4]);
						}
						s.setSeniority(s.getSeniority() + 1);
					}
					 System.out.println(students.get(numberStudents -
					 1).getSeniority());

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
		for(StudentModel s:students) {
			System.out.println(s.getId() + "," + "pw" + s.getId().replaceAll(",",""));
			
		}
		
		try {
			File file = new File("student_passwords.txt");
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(StudentModel s:students) {
				bw.write(s.getId() + "," + "pw" + s.getId().replaceAll(",",""));
				bw.newLine();
			}
					
			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
