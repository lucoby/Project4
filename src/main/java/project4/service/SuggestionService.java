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

class Student {

}

@Service
public class SuggestionService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String studentHistory = "OMS-CS-CourseGrades_Final.csv";
		LpInput in = constructLpInput(studentHistory);
		
//		for (StudentModel s:in.getStudents()) {
//			System.out.println(s.getId());
//			for(String c:s.getNextSemester().keySet()) {
//				System.out.println(c+ " pref: " + s.getNextSemester().get(c));
//			}
//		}
		
		writeLpFile(in, "suggestion.lp", 5);
		
	}

	public SuggestionService(LpInput input) {

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

		prerequisite.put("7641", "7646");
		prerequisite.put("6300", "6440");

		ret.setPrerequisite(prerequisite);

		// Create correq data
		HashMap<String, String> corequisite = new HashMap<String, String>();

		ret.setCorequisite(corequisite);

		// Create dummy enrollment limit data
		HashMap<String, Integer> enrollmentLimit = new HashMap<String, Integer>();

		enrollmentLimit.put("CS 6440", 100);
		enrollmentLimit.put("CS 7641", 100);
		enrollmentLimit.put("CS 6475", 100);
		enrollmentLimit.put("CS 6300", 100);
		enrollmentLimit.put("CS 7637", 100);
		enrollmentLimit.put("CS 6310", 100);
		enrollmentLimit.put("CS 8803", 100);
		enrollmentLimit.put("CS 4495", 100);
		enrollmentLimit.put("CS 6210", 100);
		enrollmentLimit.put("CS 6505", 100);
		enrollmentLimit.put("CS 6250", 100);
		enrollmentLimit.put("CS 6290", 100);
		enrollmentLimit.put("CS 6035", 100);
		enrollmentLimit.put("CS 7646", 100);

		ret.setEnrollmentLimit(enrollmentLimit);

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
						//new line is not a new student
						s = students.get(numberStudents - 1);
					} else {
						//new line is a new student, create a student with id, password, and 5 randomly prioritized courses
						s = new StudentModel(id);
						
						s.setPwd("password" + id.replaceAll(",", ""));
						
						s.setDesiredCourses(5);
						
						HashMap<String, Integer> nextSemester = new HashMap<String, Integer>();
						String randCourse = getRandomCourse(rand,
								requiredCourses, nextSemester);
						nextSemester.put(randCourse,1);
						randCourse = getRandomCourse(rand, requiredCourses, nextSemester);
						nextSemester.put(randCourse,2);
						randCourse = getRandomCourse(rand, requiredCourses, nextSemester);
						nextSemester.put(randCourse,3);
						randCourse = getRandomCourse(rand, requiredCourses, nextSemester);
						nextSemester.put(randCourse,4);
						randCourse = getRandomCourse(rand, requiredCourses, nextSemester);
						nextSemester.put(randCourse,5);
						
						s.setNextSemester(nextSemester);
						
						students.add(s);
						numberStudents++;
					}
					
					// Add the course if the student didn't withdraw and increase the student's seniority
					if (tokens.length == 9) {
						if (s.getCoursesTaken() == null) {
							s.setCoursesTaken(new ArrayList<String>());
							s.getCoursesTaken().add(tokens[4]);
						} else {
							s.getCoursesTaken().add(tokens[4]);
						}
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
		String randCourse = requiredCourses.get(rand.nextInt(requiredCourses.size())).getName();
		while(desiredCourse.containsKey(randCourse)) {
			randCourse = requiredCourses.get(rand.nextInt(requiredCourses.size())).getName();
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

			ArrayList<StudentModel> s = (ArrayList<StudentModel>) in.getStudents();
			// Objective Statement
			bw.write("Max X");
			bw.newLine();

			// Constraints
			bw.write("Subject To");
			bw.newLine();

			// Maximization function
			for (int i = 0; i < s.size(); i++) {
				int j = 0;
				for (String c: s.get(i).getNextSemester().keySet()) {
					j++;
					if (i == s.size() - 1 && j == s.get(i).getDesiredCourses()) {
						int senPref = ((s.get(i).getSeniority() * maxPref) + (maxPref - s.get(i).getNextSemester().get(c)));
						bw.write(senPref + " y_" + s.get(i).getId() + "_" + c.substring(3) + " - X = 0");
					} else {
						int senPref = ((s.get(i).getSeniority() * maxPref) + (maxPref - s.get(i).getNextSemester().get(c)));
						System.out.print(senPref + " y_" + s.get(i).getId() + "_" + c.substring(3) + " + ");
					}
				}
			}
			bw.newLine();
			
			
			// Enrollment Constraint


			// Course Availability and Course Capacity Constraint


			// PreReq Constraint


			// Student not repeating courses Constraint


			// Variables
			bw.write("Binary");
			bw.newLine();

			
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

	private static void readSolFile(String solFile, int[][][] schedule) {
		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(solFile);
			br = new BufferedReader(fr);

			String line;

			while ((line = br.readLine()) != null) {
				if (line.trim() != null && line.trim().length() > 0
						&& line.matches("y_\\d.*")) {
					// System.out.println(line);

					String[] t = line.split("[\\s_]+");
					schedule[Integer.valueOf(t[1]) - 1][Integer.valueOf(t[2]) - 1][Integer
							.valueOf(t[3]) - 1] = Integer.valueOf(t[4]);

				}

			}

			// for (int i = 0; i < numberStudents; i++) {
			// for (int j = 0; j < numberCourses; j++) {
			// for (int k = 0; k < numberSemesters; k++) {
			// System.out.print(schedule[i][j][k]);
			// }
			// System.out.println();
			// }
			// }

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

	private static void readOldSolFile(String solFile, int[][][] schedule) {
		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(solFile);
			br = new BufferedReader(fr);

			String line;

			while ((line = br.readLine()) != null) {
				if (line.trim() != null && line.trim().length() > 0
						&& line.matches("y\\d.*")) {
					// System.out.println(line);

					String[] t = line.split("[\\sy_]+");
					schedule[Integer.valueOf(t[1]) - 1][Integer.valueOf(t[2]) - 1][Integer
							.valueOf(t[3]) - 1] = Integer.valueOf(t[4]);

				}

			}

			// for (int i = 0; i < numberStudents; i++) {
			// for (int j = 0; j < numberCourses; j++) {
			// for (int k = 0; k < numberSemesters; k++) {
			// System.out.print(schedule[i][j][k]);
			// }
			// System.out.println();
			// }
			// }

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

	private static void writeOptimizedSchedule(String optimizedSchedule,
			int[][][] schedule, int numberStudents, int numberCourses,
			int numberSemesters) {
		try {
			File file = new File(optimizedSchedule);
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			int year = 15;
			String lineSem = "";
			int coursesThisSemester = 0;
			for (int k = 0; k < numberSemesters; k++) {
				switch ((k + 2) % 3) {
				case 2:
					lineSem = lineSem + "FA" + String.format("%02d", year)
							+ " ";
					year++;
					break;
				case 0:
					lineSem = lineSem + "SP" + String.format("%02d", year)
							+ " ";
					break;
				case 1:
					lineSem = lineSem + "SU" + String.format("%02d", year)
							+ " ";
					break;
				}
			}
			for (int i = 0; i < numberStudents; i++) {
				bw.write("Student " + (i + 1) + ":");
				bw.newLine();
				bw.write(lineSem);
				bw.newLine();

				String line1 = "";
				String line2 = "";
				for (int k = 0; k < numberSemesters; k++) {
					coursesThisSemester = 0;
					for (int j = 0; j < numberCourses; j++) {
						if (schedule[i][j][k] == 1) {
							if (coursesThisSemester == 0) {
								line1 = line1 + String.format("%02d", j + 1)
										+ "   ";
								coursesThisSemester++;
							} else if (coursesThisSemester == 1) {
								line2 = line2 + String.format("%02d", j + 1)
										+ "   ";
								coursesThisSemester++;
							}
						}
					}
					if (coursesThisSemester == 0) {
						line1 = line1 + "     ";
						line2 = line2 + "     ";
					} else if (coursesThisSemester == 1) {
						line2 = line2 + "     ";
					}
				}

				bw.write(line1);
				bw.newLine();
				bw.write(line2);
				bw.newLine();
				bw.newLine();
			}

			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
