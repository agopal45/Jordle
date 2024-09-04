# Jordle
Jordle is a full-stack project of a wordle-based game with words correlated to java and object-oriented programming. 
The backend of the project is coded fully in Java and the front-end is coded with JavaFX.
When a user starts the game or plays a new round, a pseudo-random 5-letter word is picked from a prewritten list. 
With each word guess a user attempts to guess, they are provided feedback on the letters that are present in the word and the letters that are in the correct position.
Additional scenes and panes are added for introductions and instructions.

The backend file contains code applying object-oriented principles to select a word from the words.txt file and to check the accuracy of user guesses.
User may have to run javafx through terminal by compiling and running which can be done by typing in: - javac —module-path=p:\javafx-sdk-11.0.2\lib —add-modules=javafx.controls *.java
and:  - java —module-path=p:\javafx-sdk-11.0.2\lib —add-modules=javafx.controls *
or follow the appropriate compiling and running based on user's IDE.
