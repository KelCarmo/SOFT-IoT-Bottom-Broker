package br.ufba.dcc.wiser.soft_iot.broker_bottom;
import java.util.List;

import javax.naming.ServiceUnavailableException;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import br.ufba.dcc.wiser.soft_iot.tatu.TATUWrapper;

import br.ufba.dcc.wiser.soft_iot.entities.Device;

public class Listener implements IMqttMessageListener {
	
	private boolean debugModeValue;
	private ControllerImpl impl;
	

    public Listener(ControllerImpl impl, ClientMQTT clienteMQTT, String topico, int qos) {
        clienteMQTT.subscribe(qos, this, topico);
        this.impl = impl;
    }

    @Override
    public synchronized void messageArrived( final String topic, final MqttMessage message) throws Exception {
//        System.out.println("Mensagem recebida:");
//        System.out.println("\tTópico: " + topico);
//        System.out.println("\tMensagem: " + new String(mm.getPayload()));
//        System.out.println("");
        new Thread(new Runnable() {
			public void run() {
				
				String messageContent = new String(message.getPayload());
				printlnDebug("topic: " + topic + "message: " + messageContent);
				if(true){ // TATUWrapper.isValidTATUAnswer(messageContent)
//					try{
//						JSONObject json = new JSONObject(messageContent);
						System.out.println("Mensagem Recebida: " + messageContent);	

//					}
//					catch (ServiceUnavailableException e) {
//						e.printStackTrace();
//					}
				}
//				else if(topic.contentEquals("CONNECTED")){
//					printlnDebug("Resending FLOW request for device: " + messageContent);
//					try {
//						Thread.sleep(2000);
//						Device device = fotDevices.getDeviceById(messageContent);
//						if(device != null)
//							sendFlowRequest(device);s.getDeviceById(messageContent);
//						if(device != null)
//							sendFlowRequest(device);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (ServiceUnavailableException e) {
//						e.printStackTrace();
//					}
//					
//				}
			}
		}).start();
    }
    
    public void calcScores() {
    	List<Device> listDevices = this.impl.getListDevices();
    	for (int i = 0; i < listDevices.size(); i++) {
			System.out.println(listDevices.get(i));
		}
    }
    
    public void saveMsg() {
    	
    	
    }
    
    private void printlnDebug(String str){
		if (debugModeValue)
			System.out.println(str);
	}

}