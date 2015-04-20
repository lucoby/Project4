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

import org.springframework.stereotype.Service;

import project4.login.model.StudentModel;

class Student {

}

@Service
public class SuggestionService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String file = "OMS-CS-CourseGrades_Final.csv";
		lpSolve(file);

	}

	public static void lpSolve(String studentInputs) {
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

		// for (ArrayList<Integer> s : students) {
		// for (Integer c : s) {
		// System.out.print(c + " ");
		// }
		// System.out.println();
		// }

		// Setup courses
		int numberCourses = 18;

		// Fall Start
		boolean[][] courseOfferings = { { true, false, false },
				{ true, true, true }, { true, true, true },
				{ true, true, true }, { false, false, true },
				{ true, true, true }, { true, false, false },
				{ true, true, true }, { true, true, true },
				{ false, false, true }, { true, false, false },
				{ true, true, true }, { true, true, true },
				{ false, false, true }, { true, false, false },
				{ false, false, true }, { true, false, false },
				{ false, false, true } };

		String[][] preRequisites = { { "7641", "7646" }, { "6300", "6440" } };

		// Setup semester
		int numberSemesters = 12;

		// Write .lp file
		// writeLpFile("student_schedule.lp", students, courseOfferings,
		// preRequisites,
		// numberStudents, numberCourses, numberSemesters);
		//
		// // Run Gurobi
		// runGurobi("student_schedule.lp", "student_schedule.sol");
		//
		// // Read in .sol file
		// int[][][] schedule = new
		// int[numberStudents][numberCourses][numberSemesters];
		// readSolFile("student_schedule.sol", schedule);
		//
		// // Create schedule from .sol file
		// writeOptimizedSchedule("optimized_schedule.txt", schedule,
		// numberStudents,
		// numberCourses, numberSemesters);

		// Using old lp file...
		// runGurobi("student_schedule_old.lp", "student_schedule_old.sol");
		//
		// int[][][] schedule = new
		// int[numberStudents][numberCourses][numberSemesters];
		// readOldSolFile("student_schedule_old.sol", schedule);
		//
		// writeOptimizedSchedule("optimized_schedule_old.txt", schedule,
		// numberStudents,
		// numberCourses, numberSemesters);
	}

	private static void writeLpFile(String lpFile,
			ArrayList<ArrayList<Integer>> students,
			boolean[][] courseOfferings, int[][] preRequisites,
			int numberStudents, int numberCourses, int numberSemesters) {
		try {
			File file = new File(lpFile);
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			// Objective Statement
			bw.write("Min X");
			bw.newLine();

			// Constraints
			bw.write("Subject To");
			bw.newLine();

			// Enrollment Constraint
			for (int i = 1; i <= numberStudents; i++) {
				for (int k = 1; k <= numberSemesters; k++) {
					for (int j = 1; j < numberCourses; j++) {
						bw.write(" y_" + i + "_" + j + "_" + k + " +");
					}
					bw.write(" y_" + i + "_" + numberCourses + "_" + k
							+ " <= 2");
					bw.newLine();
				}
			}

			// Course Availability and Course Capacity Constraint
			for (int j = 1; j <= numberCourses; j++) {
				for (int k = 1; k <= numberSemesters; k++) {
					for (int i = 1; i < numberStudents; i++) {
						bw.write(" y_" + i + "_" + j + "_" + k + " +");
					}
					if (courseOfferings[j - 1][(k - 1) % 3]) {
						bw.write(" y_" + numberStudents + "_" + j + "_" + k
								+ " - X <= 0");
						bw.newLine();
					} else {
						bw.write(" y_" + numberStudents + "_" + j + "_" + k
								+ " = 0");
						bw.newLine();
					}
				}
			}

			// PreReq Constraint
			int m = numberSemesters;
			for (int i = 1; i <= numberStudents; i++) {
				for (int j = 0; j < preRequisites.length; j++) {
					for (int k = 1; k < numberSemesters; k++) {
						int pre = m - k;
						int post = pre + 1;
						bw.write(" " + post + " y_" + i + "_"
								+ preRequisites[j][1] + "_" + k + " - " + pre
								+ " y_" + i + "_" + preRequisites[j][0] + "_"
								+ k + " +");
					}

					bw.write(" " + 1 + " y_" + i + "_" + preRequisites[j][1]
							+ "_" + m + " - " + 0 + " y_" + i + "_"
							+ preRequisites[j][0] + "_" + m + " <= 0");
					bw.newLine();
				}
			}

			// Student Schedule Constraint
			int c;
			for (int i = 1; i <= numberStudents; i++) {
				c = 0;
				for (int j = 1; j <= numberCourses; j++) {
					for (int k = 1; k < numberSemesters; k++) {
						bw.write(" y_" + i + "_" + j + "_" + k + " +");
					}
					while (students.get(i - 1).get(c) < j
							&& c < students.get(i - 1).size() - 1) {
						c++;
					}
					if (students.get(i - 1).get(c) == j) {
						bw.write(" y_" + i + "_" + j + "_" + numberSemesters
								+ " = 1");
						bw.newLine();
					} else {
						bw.write(" y_" + i + "_" + j + "_" + numberSemesters
								+ " = 0");
						bw.newLine();
					}
				}
			}

			// Variables
			bw.write("Binary");
			bw.newLine();
			for (int i = 1; i <= numberStudents; i++) {
				for (int j = 1; j <= numberCourses; j++) {
					for (int k = 1; k <= numberSemesters; k++) {
						bw.write("y_" + i + "_" + j + "_" + k);
						bw.newLine();
					}
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
