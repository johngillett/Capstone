##Scheduling Freshmen Courses through Simulated Annealing
**Anna Dovzhik & John Gillett**

**CSCI 440 - Capstone in Computer Science**

**University of Puget Sound**

**Spring 2016**

###Overview
Welcome to our Spring 2016 Capstone Project! This repository contains all the code and relevant files used for our project of scheduling freshmen at the University of Puget Sound into their first-semester schedules. This code is custom-fit from Fall 2015 data, but the overall structure and concepts can be applied in the future for a possible new way of enrolling freshmen into courses.

Our code is properly formatted to run in an Eclipse project. All java files are located in the **src** folder. 

###Structure of Project
We break the problem up into classes for basic objects, data parsing/generation, scheduling algorithms, and displaying results. These different pieces all come together in the Driver class, which runs the entire scheduling process and outputs a variety of results that summarize our program's success.

###Generating Graphs
To be able to generate graphs summarizing the results of our program, make sure to have JFreeChart installed. Directions on installation can be found 
[here](http://www.tutorialspoint.com/jfreechart/jfreechart_installation.htm). If you are using Eclipse to compile and run code, make sure to add the jars to the buildpath of the project as well.

###Running the program
The main method is located in Driver, so most aspects of the project can be manipulated from there. There are many helper methods that output data that can be commented/uncommented as necessary.

###Data Files & Format
We were given Fall 2015 course and student data through Excel spreadsheets. In the process of data cleansing, we created csv text files that contained the pertinent information for our program. Some custom modifications were necessary to deal with data discrepancies, which must be kept in mind if our program is to be applied to future semesters. 

The following files are the input data files our data reads. To run our project with an updated set of courses and students, you would need to replace them with their respective updated information:
 
* courses.txt
* freshman_counts.txt (A list of all courses and the number of freshmen in each, for preference generation)
* course_conflicts.txt
* freshmen.txt (list of students by ID with each enrolled course)
* freshmen_advising.txt (List of students by ID with each of their assigned advising courses)
* seminar_prefs (List of Seminars by Course ID)

The following files are the output data files made by our program:

* resultingSchedule.txt
* resulingCourses.txt

###Generating preferences

We also used the input data to generate student preference lists for regular courses, since these did not exist already. Anyone attempting to implement this as a real solution in the future would need to modify/write code to support reading in student's regular course preferences. This would be a similar process to how seminar preferences are read in.

Regular preferences can be generated in a number of ways. By default, they are simply parsed from *standardStudentRegularPrefs.txt* inside the method *getStandardPrefs*. 

By commenting out *getStandardPrefs* and uncommenting *generatePopPrefs*, a new set of preferences can be generated and used if desired. These results are outputted as *currentStudentRegularPrefs.txt*. By renaming this file to *standardStudentRegularPrefs.txt*, it can be used as the default set of regular preferences.

###Constants

The Constants class is where many of the settings and values for the scheduling process are stored. Through modification of these values you can change the way the scheduling process works.

**General Constants**:

* STUD_COURSE_LIMIT is the number of courses each student should ideally be placed in. No student will be enrolled in more than this number of courses.
* SAT_SCALE can be set to Linear or Geometric. This simply changes the way student's satisfaction scores are calculated. Our implementation defaults to linear.
* TRACK_FIDELITY this is how often we record the value of the total satisfaction score via AlgTracker. So when the value is 1, every single iteration is recorded. When the value is instead 20, only every 20 iterations are recorded in the graph. 

**Simulated Annealing Constants**:

* INIT_TEMP_VAL: This is the starting value of the temperature. Most implementations start at 1.
* ITERS_BEFORE_TEMP_SCALE: The number of iterations the SimAnnealingScheduler goes through before reducing the temperature. The higher the value, the longer the scheduler takes to run. 
* TEMP_SCALE_FACTOR: The amount the temperature is scaled every ITERS_BEFORE_TEMP_SCALE times. For instance, at .9 the temperature is reduced by 10% every ITERS_BEFORE_TEMP_SCALE times. 
* MIN_TEMP: This is the threshold the SimAnnealingScheduler checks against before stopping. Once the temperature is at or below this value, the schedule stops and returns the result.   