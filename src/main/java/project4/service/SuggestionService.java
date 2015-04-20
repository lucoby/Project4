package project4.service;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.springframework.stereotype.Service;

import project4.login.model.StudentModel;
import project4.login.model.classroom.Course;
import project4.login.model.lpsolver.LpInput;
import project4.login.model.lpsolver.LpOutput;

class Student {

}

@Service
public class SuggestionService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String studentHistory = "OMS-CS-CourseGrades_Final.csv";
		LpInput in = constructLpInput(studentHistory);

		// for (StudentModel s : in.getStudents()) {
		// System.out.println(s.getId());
		// for (String c : s.getCoursesTaken()) {
		// System.out.println(c);
		// }
		// }

		writeLpFile(in, "suggestion.lp", 5);
		runGurobi("suggestion.lp","suggestion.sol");
		int[][] suggestion = new int[in.getStudents().size()][in.getRequiredCourses().size()];
		readSolFile(in,"suggestion.sol", suggestion);
		writeOptimizedSchedule("suggested_schedule.txt",suggestion,in);
		

	}

	public SuggestionService(LpInput in) {
		writeLpFile(in, "suggestion.lp", 5);
		runGurobi("suggestion.lp","suggestion.sol");
		int[][] suggestion = new int[in.getStudents().size()][in.getRequiredCourses().size()];
		readSolFile(in,"suggestion.sol", suggestion);
		LpOutput out = new LpOutput();
		out.setSuggestion(suggestion);
	}

	public static LpInput constructLpInput(String studentInputs) {
		// Because random is cooldom
		Random rand = new Random();

		// Create dummy LpInput
		LpInput ret = new LpInput();

		// Create coursesCatalog
		ArrayList<Course> coursesCatalog = new ArrayList<Course>();

		coursesCatalog.add(new Course("CS 6440"));
		coursesCatalog.add(new Course("CS 7641"));
		coursesCatalog.add(new Course("CS 6475"));
		coursesCatalog.add(new Course("CS 6300"));
		coursesCatalog.add(new Course("CS 8802"));
		coursesCatalog.add(new Course("CS 7637"));
		coursesCatalog.add(new Course("CS 6310"));
		coursesCatalog.add(new Course("CS 8803"));
		coursesCatalog.add(new Course("CS 4495"));
		coursesCatalog.add(new Course("CS 6210"));
		coursesCatalog.add(new Course("CS 6505"));
		coursesCatalog.add(new Course("CS 6250"));
		coursesCatalog.add(new Course("CS 6290"));
		coursesCatalog.add(new Course("CS 6035"));
		coursesCatalog.add(new Course("CS 7646"));

		ret.setCoursesCatalog(coursesCatalog);

		// Create dummy requiredCourses courses that will be offered next
		// semester
		ArrayList<Course> requiredCourses = new ArrayList<Course>();

		requiredCourses.add(new Course("CS 6440"));
		requiredCourses.add(new Course("CS 7641"));
		requiredCourses.add(new Course("CS 6475"));
		requiredCourses.add(new Course("CS 6300"));
		requiredCourses.add(new Course("CS 7637"));
		requiredCourses.add(new Course("CS 6310"));
		requiredCourses.add(new Course("CS 8803"));
		requiredCourses.add(new Course("CS 4495"));
		requiredCourses.add(new Course("CS 6210"));
		requiredCourses.add(new Course("CS 6505"));
		requiredCourses.add(new Course("CS 6250"));
		requiredCourses.add(new Course("CS 6290"));
		requiredCourses.add(new Course("CS 6035"));
		requiredCourses.add(new Course("CS 7646"));

		ret.setRequiredCourses(requiredCourses);

		// Create prereq data
		HashMap<String, String> prerequisite = new HashMap<String, String>();

		prerequisite.put("CS 7641", "CS 7646");
		prerequisite.put("CS 6300", "CS 6440");

		ret.setPrerequisite(prerequisite);

		// Create correq data
		HashMap<String, String> corequisite = new HashMap<String, String>();

		ret.setCorequisite(corequisite);

		// Create dummy enrollment limit data
		HashMap<String, Integer> enrollmentLimit = new HashMap<String, Integer>();

		enrollmentLimit.put("CS 6300", 550);
		enrollmentLimit.put("CS 6310", 550);

		ret.setEnrollmentLimit(enrollmentLimit);

		// Set default enrollment limit

		ret.setDefaultEnrollmentLimit(500);

		// Create student data from test file
		ArrayList<StudentModel> students = new ArrayList<StudentModel>();

		int numberStudents = 0;
		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(studentInputs);
			br = new BufferedReader(fr);

			String line;
			int lineNum = 0;

			// Read each line
			while ((line = br.readLine()) != null) {
				if (line.trim() != null && line.trim().length() > 0
						&& lineNum > 0) {
					StudentModel s = null;

					String[] tokens = line.split("[,]+");
					String id = (tokens[0] + "," + tokens[1] + "," + tokens[2])
							.replace("\"", "");

					if (numberStudents - 1 >= 0
							&& students.get(numberStudents - 1).getId()
									.equals(id)) {
						// new line is not a new student
						s = students.get(numberStudents - 1);
					} else {
						// new line is a new student, create a student with id,
						// password, and 5 randomly prioritized courses
						s = new StudentModel(id);

						s.setPwd("password" + id.replaceAll(",", ""));

						s.setDesiredCourses(5);

						HashMap<String, Integer> nextSemester = new HashMap<String, Integer>();
						String randCourse = getRandomCourse(rand,
								requiredCourses, nextSemester);
						nextSemester.put(randCourse, 1);
						randCourse = getRandomCourse(rand, requiredCourses,
								nextSemester);
						nextSemester.put(randCourse, 2);
						randCourse = getRandomCourse(rand, requiredCourses,
								nextSemester);
						nextSemester.put(randCourse, 3);
						randCourse = getRandomCourse(rand, requiredCourses,
								nextSemester);
						nextSemester.put(randCourse, 4);
						randCourse = getRandomCourse(rand, requiredCourses,
								nextSemester);
						nextSemester.put(randCourse, 5);

						s.setNextSemester(nextSemester);

						students.add(s);

						s.setCoursesTaken(new ArrayList<String>());

						s.setSeniority(0);
						numberStudents++;
					}

					// Add the course if the student didn't withdraw and
					// increase the student's seniority
					if (tokens.length == 9) {
						s.getCoursesTaken().add(tokens[4]);
						s.setSeniority(s.getSeniority() + 1);
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

		ret.setStudents(students);

		return ret;
	}

	private static String getRandomCourse(Random rand,
			ArrayList<Course> requiredCourses,
			HashMap<String, Integer> desiredCourse) {
		String randCourse = requiredCourses.get(
				rand.nextInt(requiredCourses.size())).getName();
		while (desiredCourse.containsKey(randCourse)) {
			randCourse = requiredCourses.get(
					rand.nextInt(requiredCourses.size())).getName();
		}
		return randCourse;
	}

	private static void writeLpFile(LpInput in, String lpFile, int maxPref) {
		try {
			File file = new File(lpFile);
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			ArrayList<StudentModel> students = (ArrayList<StudentModel>) in
					.getStudents();
			ArrayList<Course> courses = (ArrayList<Course>) in
					.getRequiredCourses();
			ArrayList<Course> allCourses = (ArrayList<Course>) in
					.getCoursesCatalog();

			// Objective Statement
			bw.write("Max X");
			bw.newLine();

			// Constraints
			bw.write("Subject To");
			bw.newLine();

			// Maximization function
			for (int i = 0; i < students.size(); i++) {
				int j = 0;
				for (String c : students.get(i).getNextSemester().keySet()) {
					j++;
					if (i == students.size() - 1
							&& j == students.get(i).getDesiredCourses()) {
						int senPref = ((students.get(i).getSeniority() * maxPref) + (maxPref - students
								.get(i).getNextSemester().get(c)));
						bw.write(senPref + " y_" + students.get(i).getId()
								+ "_" + c.substring(3) + " - X = 0");
					} else {
						int senPref = ((students.get(i).getSeniority() * maxPref) + (maxPref - students
								.get(i).getNextSemester().get(c)));
						bw.write(senPref + " y_" + students.get(i).getId()
								+ "_" + c.substring(3) + " + ");
					}
				}
			}
			bw.newLine();

			// Enrollment Constraint

			for (int i = 0; i < students.size(); i++) {
				for (int j = 0; j < courses.size() - 1; j++) {
					bw.write("y_" + students.get(i).getId() + "_"
							+ courses.get(j).getName().substring(3) + " + ");
				}
				bw.write(" y_"
						+ students.get(i).getId()
						+ "_"
						+ courses.get(courses.size() - 1).getName()
								.substring(3) + " <= 2");
				bw.newLine();
			}

			// Course Availability and Course Capacity Constraint
			for (int i = 0; i < allCourses.size(); i++) {
				for (int j = 0; j < students.size() - 1; j++) {
					bw.write("y_" + students.get(j).getId() + "_"
							+ allCourses.get(i).getName().substring(3) + " + ");
				}
				int limit = 0;
				boolean courseOffered = false;
				for (Course c : courses) {
					if (c.getName().equals(allCourses.get(i).getName())) {
						courseOffered = true;
					}
				}
				if (in.getEnrollmentLimit().containsKey(
						allCourses.get(i).getName())) {
					limit = in.getEnrollmentLimit().get(
							allCourses.get(i).getName());
				} else if (courseOffered) {
					limit = in.getDefaultEnrollmentLimit();
				}
				bw.write(" y_" + students.get(students.size() - 1).getId()
						+ "_" + allCourses.get(i).getName().substring(3)
						+ " <= " + limit);
				bw.newLine();
			}

			// PreReq Constraint
			for (StudentModel s : students) {
				for (String prereq : in.getPrerequisite().keySet()) {
					int taken = 0;
					for (String t : s.getCoursesTaken()) {
						if (t.equals(prereq)) {
							taken = 1;
						}
					}
					bw.write("y_" + s.getId() + "_"
							+ in.getPrerequisite().get(prereq).substring(3)
							+ " <= " + taken);
					bw.newLine();
				}
			}

			// Student not repeating courses Constraint
			for (StudentModel s : students) {
				for (Course c : courses) {
					int taken = 0;
					for (String t : s.getCoursesTaken()) {
						if (t.equals(c.getName())) {
							taken = 1;
						}
					}
					bw.write(taken + " y_" + s.getId() + "_"
							+ c.getName().substring(3) + " < 1");
					bw.newLine();
				}
			}

			// Variables
			bw.write("Binary");
			bw.newLine();
			for (int i = 0; i < students.size(); i++) {
				for (int j = 0; j < allCourses.size() - 1; j++) {
					bw.write("y_" + students.get(i).getId() + "_" + allCourses.get(j).getName().substring(3));
					bw.newLine();
				}
			}
			bw.write("end");
			bw.newLine();
			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void runGurobi(String lpFile, String solFile) {
		try {
			GRBEnv env = new GRBEnv();
			env.set(GRB.IntParam.OutputFlag, 0);
			GRBModel model = new GRBModel(env, lpFile);

			model.optimize();

			int optimstatus = model.get(GRB.IntAttr.Status);

			if (optimstatus == GRB.Status.INF_OR_UNBD) {
				model.getEnv().set(GRB.IntParam.Presolve, 0);
				model.optimize();
				optimstatus = model.get(GRB.IntAttr.Status);
			}

			if (optimstatus == GRB.Status.OPTIMAL) {
				double objval = model.get(GRB.DoubleAttr.ObjVal);
				System.out.println("Optimal objective: " + objval);
				model.write(solFile);
			} else if (optimstatus == GRB.Status.INFEASIBLE) {
				System.out.println("Model is infeasible");

				// Compute and write out IIS
				model.computeIIS();
				model.write("model.ilp");
			} else if (optimstatus == GRB.Status.UNBOUNDED) {
				System.out.println("Model is unbounded");
			} else {
				System.out.println("Optimization was stopped with status = "
						+ optimstatus);
			}

			// Dispose of model and environment
			model.dispose();
			env.dispose();

		} catch (GRBException e) {
			System.out.println("Error code: " + e.getErrorCode() + ". "
					+ e.getMessage());
		}
	}

	private static void readSolFile(LpInput in, String solFile, int[][] suggestion) {
		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(solFile);
			br = new BufferedReader(fr);

			String line;

			while ((line = br.readLine()) != null) {
				if (line.trim() != null && line.trim().length() > 0
						&& line.matches("y_\\d.*")) {
//					 System.out.println(line.replaceAll("[\\s_]+", " "));
					
					String[] t = line.split("[\\s_]+");
					int s = -1;
					for (int i = 0; i < in.getStudents().size(); i++) {
						if (t[1].equals(in.getStudents().get(i).getId())) {
							s = i;
						}
					}
					int c = -1;
					for (int i = 0; i < in.getRequiredCourses().size(); i++) {
						if (t[2].equals(in.getRequiredCourses().get(i).getName().substring(3))) {
							c = i;
						}
					}
					if (! (c == -1 || s == -1)) {
						suggestion[s][c] = Integer.valueOf(t[3]);
					}
				}

			}

//			for (int i = 0; i < in.getStudents().size(); i++) {
//				for (int j = 0; j < in.getRequiredCourses().size(); j++) {
//					System.out.print(suggestion[i][j] + " " );
//				}
//				System.out.println();
//			}
//			System.out.println(in.getStudents().size());

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
	}


	private static void writeOptimizedSchedule(String suggestedSchedule,
			int[][] suggestion, LpInput in) {
		try {
			File file = new File(suggestedSchedule);
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			for (int i = 0; i < in.getStudents().size(); i++) {
				bw.write("Student " + in.getStudents().get(i).getId() + "'s recommended courses:");
				bw.newLine();

				for (int j = 0; j < in.getRequiredCourses().size(); j++) {
					if(suggestion[i][j] == 1) {
						bw.write(in.getRequiredCourses().get(j).getName());
						bw.newLine();
					}
				}

				bw.newLine();
			}

			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
