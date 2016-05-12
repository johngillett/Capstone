##Scheduling Freshmen Courses through Simulated Annealing
Anna Dovzhik & John Gillett
University of Puget Sound
CSCI 440 - Capstone in Computer Science
Spring 2016

###Overview
Welcome to our Spring 2016 Capstone Project! This repository contains all the code and relevant files used for our project of scheduling freshmen at the University of Puget Sound into their first-semester schedules. This code is custom-fit from Fall 2015 data, but the overall structure and concepts can be applied in the future for a possible new way of enrolling freshmen into courses.

Our code is properly formatted to run in an Eclipse project. All java files are located in the **src** folder. 

###Structure of Project
We break the problem up into classes for basic objects, data parsing/generation, scheduling algorithms, and displaying results. These different pieces all come together in the Driver class, which runs the entire scheduling process and outputs a variety of results that summarize our program's success.

###Data Files & Format
We were given Fall 2015 course and student data through Excel spreadsheets. In the process of data cleansing, we created csv text files that contained the pertinent information for our program. Some custom modifications were necessary to deal with data discrepancies. 

The following files are the input data files our data reads: 
*courses_with_advising.txt
*freshman_counts.txt
*course_conflicts.txt
*freshmen.txt
*freshmen_advising.txt

The following files are the output data files made by our program:
*resultingSchedule.txt
*resulingCourses.txt

###Generating Graphs
To be able to generate graphs summarizing the results of our program, make sure to have JFreeChart installed. Directions on installation can be found at http://www.tutorialspoint.com/jfreechart/jfreechart_installation.htm. If you are using Eclipse to compile and run code, make sure to add the jars to the buildpath of the project as well.

###Using Our Project
The main method is located in Driver, most aspects of the project can be manipulated from there. 
The "PrintOutDataInfo" method has multiple useful output methods called inside, commenting/uncommenting these methods is an easy way to modify what results are outputted. 

Our project currently uses data from Fall 2015, and generates regular course preference lists for students. To run our project with an updated set of courses and students you would need to replace the following files with their respective updated information:
*courses 
*courses_with_advising
*freshman_counts (A list of all courses and the number of freshmen in each, for preference generation)
*freshmen (list of students by ID with each enrolled course)
*freshmen_advising (List of students by ID with each of their assigned advising courses)
*seminar_prefs (List of Seminars by Course ID)

Since we worked with incomplete data anyone attempting to implement this as a real solution would need to modify/write code to support reading in student's regular course preferences. 

Student Generation can be modified in numerous ways. By default they are simply parsed from "standardStudentRegularPrefs.txt" inside the method "getStandardPrefs". By commenting out this and uncommenting "generatePopPrefs" a new set of prefernces can be generated and used if desired. These results are outputted as "currentStudentRegularPrefs.txt" and through renaming can be used as a standard set like by default. 