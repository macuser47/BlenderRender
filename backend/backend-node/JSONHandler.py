import sys;
import json;
import time;
import startNode;
import random;

#Note that this file is written (mostly) functionally, because I'm new to functional programming and it seems cool.

min_timeout = 0.1;
max_timeout = 2.0;


#Writes files serialized in lists
#names of files, data to write, and original data in equal length lists
def writeFiles(fileNames, dataToWrite, original, maxDesync = 5):
    #check if any changes to original file, if there are, return the data to re-merge
    currentData = loadFiles(fileNames);
    if currentData != original:
        return currentData, False;

    #everything checks out, write the files
    map(writeFile, fileNames, dataToWrite);
    return (1..len(dataToWrite)) + [True];


#load set of files
#last element is desynced bool.
def loadFiles(files, maxDesync = 5): #max desync of 5 seconds
    currentTime = time.time();

    loadedFiles = loadFilesRec(files, maxDesync, currentTime);

    return loadFiles;


#recursively load files and put into array
def loadFilesRec(files, maxDesync, startTime):
    if time.time() - startTime >= maxDesync: #empty
        raise TimeoutError(); #last element is check
    else:
        try:
            loadedJSON = loadJSON(files[0])
            if len(files) == 1:
                return [loadedJSON];
            else:
                return [loadedJSON] + loadFilesRec(files[1:], maxDesync, startTime); #append json, get next element
        except IOError:
            time.sleep( getTimeout(maxDesync) );
            return loadFilesRec(files, maxDesync, startTime); #sleep and retry if error

def getTimeout(upperBound = sys.maxint):
    random.seed(time.time())
    return random.uniform(min_timeout, min(max_timeout, upperBound))

def flatten(deepList):
    return [val for sublist in deepList for val in sublist];

#JSON read/write methods
# read string from file
def readFile(file):
    file = open(file, 'r');
    text = file.read();
    file.close();
    return text;

# write string to file
def writeFile(file, string):
    file = open(file, 'w');
    file.write(string);
    file.close();

# read JSON object from file
def loadJSON(file):
    return json.loads( readFile(file) );

# wrtite JSON object to file
def writeJSON(file, obj):
    writeFile(file, json.dumps(obj));


##############ERRORS###############
class TimeoutError(Exception):
    def __init__(self, value):
        self.value = value
    def __str__(self):
        return repr(self.value)