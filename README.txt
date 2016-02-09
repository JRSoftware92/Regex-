Author: John Riley
Application: Regex List Parser
Purpose: This program has been created to quickly parse text data for a list of regex pattern based objects. No object will be repeated in the list.

Flags:
-v - Indicates that the program should print more information at runtime. Typically for debugging purposes.
-i - Indicates that the next provided argument is the Relative Address of Input File
-o - Indicates that the next provided argument is the Relative Address of Output File
-regex - Indicates that the next provided argument is the Regex Pattern used to parse data.
-html - Indicates that the output file should be in an HTML List format.
-pre - Indicates that the next provided argument is the name of the type of Standard Regex Pattern to be used (see below)
	- phone - Used to detect simple phone numbers.
	- phone_us - Used to detect US phone numbers in various formats
	- phone_all - Used to detect phone numbers with various country codes and formats. May not work for all countries.
	- email - Used to detect emails
	- url - Used to detect urls
	- ip - Used to detect ip addresses
-help - Display help options.
