/**
 *  Set Multiple Thermostats
 *
 *  Copyright 2017 NATARAJASHEKER JALUVANCHA
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Set Multiple Thermostats",
    namespace: "Raj",
    author: "NATARAJASHEKER JALUVANCHA",
    description: "Set Multiple Thermostats",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
    section("Choose first thermostat") {
		input "thermostats", "capability.thermostat", required: true, multiple: false
	}
    
    section("Set mode temperatures") {
        input "opHeatSet", "decimal", title: "When Heating", description: "Heating temperature for mode"
        input "opCoolSet", "decimal", title: "When Cooling", description: "Cooling temperature for mode"
        input "opTurnOff", "bool", title: "Turn Off", description: "Turn off"
    }


	section("Choose second thermostat") {
		input "thermostat2", "capability.thermostat", required: true, multiple: false
	}

    section("Set mode temperatures") {
        input "opHeatSet2", "decimal", title: "When Heating", description: "Heating temperature for mode"
        input "opCoolSet2", "decimal", title: "When Cooling", description: "Cooling temperature for mode"
        input "opTurnOff2", "bool", title: "Turn Off", description: "Turnoff"
    }
    
    section("Resume to normal schedule after this many minutes (default 60)") {
    input "openThreshold", "number", description: "Number of minutes", required: false
  }

    
    
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	
	log.debug "Initialize with settings: ${settings}"
   subscribe(location, modeChangedHandler)
}

def modeChangedHandler(evn){


	def delay = (openThreshold != null && openThreshold != "") ? openThreshold * 60 : 3600
	
	if(opTurnOff){
    thermostats.auto()
    thermostats.setHeatingSetpoint(opHeatSet)
    thermostats.setCoolingSetpoint(opCoolSet)
    
    runIn(delay, thermResumeProgram1, [overwrite: true])   
    
    }else{
    thermostats.off()
    }
    if(opTurnOff2){
    thermostat2.auto()
    thermostat2.setHeatingSetpoint(opHeatSet2)
    thermostat2.setCoolingSetpoint(opCoolSet2)
    
    runIn(delay, thermResumeProgram2, [overwrite: true])       
    
    }else{
    thermostat2.off()
    }


}

def thermResumeProgram1(){
thermostats.resumeProgram()
}


def thermResumeProgram2(){
thermostat2.resumeProgram()
}
