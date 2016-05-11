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
*
*

###Generating Graphs
To be able to generate graphs summarizing the results of our program, make sure to have JFreeChart installed. Directions on installation can be found at http://www.tutorialspoint.com/jfreechart/jfreechart_installation.htm. If you are using Eclipse to compile and run code, make sure to add the jars to the buildpath of the project as well.

