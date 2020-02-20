#include "contiki.h"
#include "dev/button-sensor.h"
#include "lib/random.h"
#include "net/ip/uip.h" 
#include "net/ipv6/uip-ds6.h" 
#include "net/ip/uip-debug.h"
#include "contiki-net.h"
#include "rest-engine.h"
#include "stdio.h"
#include "stdlib.h"

#define TIME	30

PROCESS(coap, "CoAP Server");
AUTOSTART_PROCESSES(&coap);

static void res_get_handler(void *request, void *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);
static void res_periodic_handler(void);
static void res_event_handler(void);

static unsigned int value = 0;

void res_get_handler(void* request, void* response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset) {
	
	unsigned int accept = -1;
  	REST.get_header_accept(request, &accept);
  	

	if(accept == REST.type.APPLICATION_JSON || accept == -1){

		sprintf((char *) buffer, "[{\"n\":\"value\",\"v\":%d}]", value);
		REST.set_header_content_type(response, REST.type.APPLICATION_JSON);
		REST.set_response_payload(response, buffer, strlen((char *) buffer));

	}else{

		const char *msg = "[{\"n\":\"error\",\"sv\":\"Supporting content-types application/json\"}]";
		REST.set_response_status(response, REST.status.NOT_ACCEPTABLE);
		REST.set_response_payload(response, msg, strlen(msg));

	}
	
}

PERIODIC_RESOURCE(periodic_resource, "title=\"Mote periodic resourse\";rt=\"value\";obs", res_get_handler, NULL, NULL, NULL, CLOCK_SECOND*TIME, res_periodic_handler);
EVENT_RESOURCE(event_resource, "title=\"Mote event resourse\";rt=\"value\";obs", res_get_handler, NULL, NULL, NULL, res_event_handler);

// Simula sensore di temperatura
void res_periodic_handler(){
	
	// E' normale che il server risponda con un header non-confirmable:
	// probabilmente dipende dal client che gliel'ha inviata per accedere alla WSN
	
	value = 25 + random_rand() % 10;
	
	REST.notify_subscribers(&periodic_resource);
	
}

// Simula sensore di umidità
void res_event_handler(){
		
	value = 25+random_rand() % 10;
	
	REST.notify_subscribers(&event_resource);
	
}

PROCESS_THREAD(coap, ev, data){
	
	PROCESS_BEGIN();
	
	printf("Start CoAP server\n");
	
	SENSORS_ACTIVATE(button_sensor);
	
	rest_init_engine();
	
	rest_activate_resource(&periodic_resource, "mote/value");
	rest_activate_resource(&event_resource, "mote/value");
	
	while(1){
		PROCESS_WAIT_EVENT();
		if(ev == sensors_event && data == &button_sensor){
			printf("Event triggered\n");
			event_resource.trigger();
		}
	}	

	PROCESS_END();

}
