## Overview

The repository contains a code for creating decision tree based chat flows. The ```Main``` class contains example usage of the ```ChatFlow``` class in the terminal, using the decision tree found in ```./src/main/resources/instructions_example.json```.

## Program structure

The ```ChatFlow``` object takes a ```List``` of instructions of ```ChatInstruction``` type.
The ```ChatInstruction``` object can have the following member variables:
- ```id``` - an integer containing a unique value for each instruction in the list;
- ```type``` which currently takes one of the values contained in the ```InstructionType``` enum:
  - ```MESSAGE``` - indicates that a message is to be sent;
  - ```INPUT``` - indicates that the program awaits a text input;
  - ```CHOICE``` - indicates that the user must choose from a set of predetermined choices;
  - ```END``` - indicates that the discussion has come to the end;
- ```message``` - a string containing the message to be sent to the chat, required for ```MESSAGE``` instructions. The ```message``` variable can contain placeholders formatted as e.g. ```${placeholder}``` for user input variables, corresponding to ```inputVariable``` values.
-  ```inputVariable``` - name of the variable to contain user input, required for ```INPUT``` instructions;
- ```inputRegex``` - regular expression describing the expected format for user input, required for ```INPUT``` instructions;
- ```choices``` - a map containing user choices and the corresponding ```id``` values for next instructions, required for ```CHOICE``` instructions;
- ```errorMessage``` - a string containing a message to be displayed if the user input does not adhere to the requirements, required for ```INPUT``` and ```CHOICE``` instructions;
- ```nextId``` - an integer indicating the next instruction to be read, required for ```MESSAGE``` and ```INPUT``` instructions;

The list of instructions can be read from an external source. The program currently supports reading from JSON files - this can be done by passing the path to the JSON file as a string
in the ```ChatFlow``` constructor. An example file can be found in ```./src/main/resources/instructions_example.json```

The list of instructions is validated to ensure that each instruction contains the required member variables, that all placeholders have corresponding inputs and that the list contains an ```END``` instruction;

After executing the ```readMessage```, ```enterInput``` or ```enterChoice``` method, the ```ChatFlow``` object reads the next instruction in the tree.

The ```ChatFlowTerminalRunner``` class takes a ```ChatFlow``` object as an argument, and the ```run``` method loops through all the instructions in the list of the ```ChatFlow``` object until an ```END``` instruction is reached.

## Example decision tree

The following picture illustrates the example decision tree found in ```./src/main/resources/instructions_example.json```:
<p align="center">
<img src="https://github.com/dzinulis/chat-flow/blob/master/instructions_example.png" width=50% height=50%>
</p>
