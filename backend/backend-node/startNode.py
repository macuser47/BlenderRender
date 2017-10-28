import json;
import time;
import socket;
import os;
import ctypes;
import sys;
import JSONHandler;
from pathlib2 import path;

######TODOS########
#-Make config loaded into struct for file-wide use
#-Make JSON Loading handler
# -Synchronous Reads
# -Safe Writes w/o data loss

#

class Config:
    def __init__(self, configJSON):
        self.GPUList = configJSON["GPUList"];
        self.managerRoot = configJSON["manager root"];
        self.minerLauncher = configJSON["miner launcher"];
        self.output = configJSON["output"];


global config;

global updateState;


#\\\\Macpro-pc\\f\\Render Server\\

def main(updateTimeout):
    print "Setting up Node:"
    nodeSetup();
    while True:
        update();
        time.sleep(updateTimeout)
    
def update():
    print "TODO: Implement update"
    checkTasks(False);


#check if new tasks have been allocated in GPUTasks, and perform corresponding actions
def checkTasks(forceUpdate):
    global updateState;
    #get GPUTasks.json
    GPUTasks = loadJSON("GPUTasks.json");

    #check if task reallocation is needed (return if not)
    if (not forceUpdate) & (GPUTasks == updateState):
        return;

    #load config file
    config = loadJSON("config.json");

    #kill existing instances
    print "TODO: Kill existing instances"

    #run new tasks
    #modify file

    #get mining gpus and concatenate mining indices for miner args, add blenders to array for later task allocation
    miners = "";
    blenders = []
    for gpu in config["GPUList"]:
        if GPUTasks[gpu["name"]]["task"] == "mining":
            miners += str(gpu["miner-index"]);
        elif GPUTasks[gpu["name"]]["task"] == "rendering":
            blenders.append(gpu["name"]);
            print "TODO: Figure out how to render shit";

    #create new start script for this gpu set
    loc = config["miner launcher"].split(".bat")[0] + "_tmp.bat";
    writeFile(loc, readFile(config["miner launcher"]) + " -di " + miners);
    print "Wrote new miner start script, executing..."

    #run miner
    os.system("start " + loc);

    #run blender shit

    for gpu in blenders:
        #some fluff to make sure the script runs as admin
        #https://stackoverflow.com/a/41930586
        if is_admin():
            os.system("blender -b " + gpu["target"] + " -P blender.py")
        else:
            # Re-run the program with admin rights
            ctypes.windll.shell32.ShellExecuteW(None, "runas", sys.executable, "", None, 1)


    #set updateState
    updateState = GPUTasks;

#perform initial node setup:
#load config file and update NodeList.json
#load JSON files and create template files if not found
#generates GPUTasks
def nodeSetup():

    #load config file
    loadConfig();

    #check if nodelist found, create
    nodeList = createNodeList();

    #TODO: Summary printout of config for user


    print "Writing Node data to NodeList.json..."
    #add node to node list, write
    writeCompleted = False;
    while not writeCompleted:
        modifiedNodeList = addNodeToNodeList(nodeList);
        (nodeList, writeCompleted) = JSONHandler.writeFiles([config.managerRoot + "NodeList.json"], modifiedNodeList, nodeList);




    #generate GPUTasks.json
    print "Generating GPUTasks.json..."
    GPUTasks = {};
    for gpu in config["GPUList"]:
        GPUTasks[gpu["name"]] = {
            "task": "mining",
            "target": "",
            "args": ""
        }

    writeJSON("GPUTasks.json", GPUTasks);

    #force update to run for the first time
    global updateState;
    updateState = GPUTasks;
    checkTasks(True);
                
############################################################################################
###########################NODE HELPER FUNCTIONS############################################
############################################################################################
def generateGPUTasks():


def addNodeToNodeList(nodeList):
    #copy obj for safety
    moddedList = list(nodeList);

    #check for gpus, remove old copies, re-add
    for pc in moddedList["NodeList"]:
        if pc["ip"] == getIP():
            print "Removing old node logs..."
            moddedList["NodeList"].remove(pc);

    #create object for node data to add to NodeList
    nodeObj = {
        "ip": getIP(),
        "node root": getDirectory(),
        "gpus": config["GPUList"]
    }

    #add node object
    moddedList["NodeList"].append(nodeObj);

    return moddedList;


def loadConfig():
    global config;
    # no exception handling needed, if config.json not found, you definitely fucked up
    config = Config(readJSONForce(["config.json"]))

def createNodeList():
    #load NodeList, generate if not found
    try:
       return readJSONForce(config.managerRoot + "NodeList.json");
    except IOError:
        print "NodeList.json not found, generating template file at " + config.managerRoot + "NodeList.json"
        JSONHandler.writeJSON(config.managerRoot + "NodeList.json", getNodeListTemplate());
        return readJSONForce(config.managerRoot + "NodeList.json");

    print "Loaded config.json and NodeList.json from " + getDirectory() + "\\config.json and " + nodeListLoc;

def detachNode():
    #get NodeList.json
    nodeListLoc = config.managerRoot + "NodeList.json";
    nodeList = readJSONForce([nodeListLoc]);
    #remove node data from NodeList
    removeNodeData(nodeList, nodeListLoc);

    print "TODO: Stop miners/renders"

    #exit();

def getNodeListTemplate():
    return {
        "NodeList": []
    }

def getRenderQueueTemplate():
    return {
        "Queue": []
    }

###############################################################################################
###########################SYSTEM FUNCTIONS####################################################
#############################################################################################

#force retrieval, timeout not an option
def readJSONForce(loc):
    try:
        nodeList = JSONHandler.loadFiles(loc)
        return nodeList;
    except JSONHandler.TimeoutError:
        return readJSONForce();                   #yes, definitely a leak potential here, but necessary
    except IOError as e:
        raise e; #pass exception

#
def removeNodeData(nodeJSON, nodeListLoc):
    for pc in nodeJSON["NodeList"]:
        if pc["ip"] == getIP():
            print "Removing old node logs..."
            nodeJSON["NodeList"].remove(pc);
    JSONHandler.writeJSON(nodeListLoc, nodeJSON); #TODO: fix


#get local ip
def getIP():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM);
    s.connect(("8.8.8.8", 80));
    ip = (s.getsockname()[0]);
    s.close();

    return ip;

#get working directory
def getDirectory():
    return os.path.dirname(os.path.realpath(__file__));


#get if program run as admin
#https://stackoverflow.com/a/41930586
def is_admin():
    #TODO: make this just return the bool and test
    try:
        return ctypes.windll.shell32.IsUserAnAdmin()
    except:
        return False


#start script
if __name__ == "__main__":
    timeout = 4;
    updateState = {};
    try:
        main(timeout);
    except KeyboardInterrupt: #TODO: Fix this so node is detached at any depth
        detachNode();
