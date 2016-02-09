#Regex Scanner
This program has been created to quickly parse text data for a list of regex pattern based objects. 

**Note:** No object will be repeated in the resulting list.

##Flags
###Argument Flags
| Symbol | Purpose                                                                                                           |
|--------|-------------------------------------------------------------------------------------------------------------------|
|   -v   | Indicates that the program should print more information at runtime. Typically for debugging purposes.            |
|   -i   | Indicates that the next provided argument is the Relative Address of Input File.                                  |
|   -o   | Indicates that the next provided argument is the Relative Address of Output File.                                 |
| -regex | Indicates that the next provided argument is the Regex Pattern used to parse data.                                |
| -pre   | Indicates that the next provided argument is the name of the type of Standard Regex Pattern to be used (See Table)|
| -html  | Indicates that the output file should be in an HTML List format.                                                  |
| -help  | Display help options.                                                                                             |

###Standard Regex Pattern Arguments
|   Symbol    | Usage                                                                                               |
|-------------|-----------------------------------------------------------------------------------------------------|
|    phone    | Used to detect simple phone numbers .                                                               |
|   phone_us  | Used to detect US Phone numbers in various formats.                                                 |
|   phone_all | Used to detect phone numbers with various country codes and formats. May not work for all countries.|
|    email    | Used to detect email addresses.                                                                     |
|     url     | Used to detect url addresses.                                                                       |
|     ip      | Used to detect ip addresses.                                                                        |
