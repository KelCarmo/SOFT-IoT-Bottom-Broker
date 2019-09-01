package br.ufba.dcc.wiser.soft_iot.mapping_devices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.ufba.dcc.wiser.soft_iot.entities.*;

public class ControllerImpl implements Controller{
	
	private List<Device> listDevices; 
	private String strJsonDevices;
	private boolean debugModeValue;
	private  ClientMQTT clienteMQTT;
	
	public void start(){
		//printlnDebug("Starting mapping of connected devices...");
		//loadConnectedDevices(this.strJsonDevices);
		// TODO Auto-generated method stub
		 	clienteMQTT = new ClientMQTT("tcp://localhost:1883", null, null);
	        clienteMQTT.iniciar();

	        new Listener(clienteMQTT, "#", 0);
	}
	
	public void stop(){
		
	        this.clienteMQTT.finalizar();
	    
	}
	
	
	private void loadConnectedDevices(String strDevices){
		List<Device> listDevices = new ArrayList<Device>();
		try {
			printlnDebug("JSON load:");
			printlnDebug(strDevices);
			JSONArray jsonArrayDevices = new JSONArray(strDevices);
			for (int i = 0; i < jsonArrayDevices.length(); i++){
				JSONObject jsonDevice = jsonArrayDevices.getJSONObject(i);
				ObjectMapper mapper = new ObjectMapper();
				Device device = mapper.readValue(jsonDevice.toString(), Device.class);
				listDevices.add(device);
				
				List<Sensor> listSensors = new ArrayList<Sensor>();
				JSONArray jsonArraySensors = jsonDevice.getJSONArray("sensors");
				for (int j = 0; j < jsonArraySensors.length(); j++){
					JSONObject jsonSensor = jsonArraySensors.getJSONObject(j);
					Sensor sensor = mapper.readValue(jsonSensor.toString(), Sensor.class);
					listSensors.add(sensor);
				}
				device.setSensors(listSensors);
			}
		} catch (JsonParseException e) {
			System.out.println("Verify the correct format of 'DevicesConnected' property in configuration file."); 
		} catch (JsonMappingException e) {
			System.out.println("Verify the correct format of 'DevicesConnected' property in configuration file.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.listDevices = listDevices;
	}
	
	public Device getDeviceById(String deviceId){
		for (Device device : listDevices ){
			if(device.getId().contentEquals(deviceId))
				return device;
		}	
		return null;		
	}
	
	private void printlnDebug(String str){
		if (debugModeValue)
			System.out.println(str);
	}

	public void setStrJsonDevices(String strJsonDevices) {
		this.strJsonDevices = strJsonDevices;
	}

	public List<Device> getListDevices() {
		return listDevices;
	}

	public void setDebugModeValue(boolean debugModeValue) {
		this.debugModeValue = debugModeValue;
	}

}
