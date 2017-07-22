import json;
import time;


def main(updateTimeout):
    print "Setting up Node:"
    nodeSetup();
    while True:
        update();
        time.sleep(updateTimeout)
    
def update():
    print "TODO: Implement update"

def nodeSetup():
    #load config file and update NodeList.json
    #get config
    config = loadJSON("config.json");

    names = [];
    
    #user output
    print "Instantiating " + str(len(config["GPUList"])) + " GPUS: ";
    for gpu in config["GPUList"]:
        print "GPU: " + gpu["name"] + " Index: " + str(gpu["miner-index"]);
        names.add(gpu["name"]);

    #get NodeList
    nodeList = loadJSON(config["manager root"] + "NodeList.json")

    #check for gpus, if not present, add
    ''''for pc in nodeList["NodeList"]:
        for gpu in pc["gpus"]:
            if gpu["name"] in config["GPUList"]'''
                

def detachNode():
    print "TODO: Implement Node Detaching";
    #exit();


def loadJSON(file):
    file = open('config.json', 'r');
    rawJSON = file.read();
    file.close();

    return json.loads(rawJSON);

if __name__ == "__main__":
    timeout = 4;
    try:
        main(timeout);
    except KeyboardInterrupt:
        detachNode();
