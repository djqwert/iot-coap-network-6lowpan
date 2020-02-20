#ifndef PROJECT_CONF_H_
#define PROJECT_CONF_H_

#undef NETSTACK_CONF_RDC
#define NETSTACK_CONF_RDC 	nullrdc_driver

// F0 /**********************************/
#undef RPL_OF_OCP 
#define RPL_OF_OCP  RPL_OCP_OF0
/****************************************/

// Configuro Trickle /*******************/

#undef RPL_CONF_DIO_REDUNDANCY 
#define RPL_CONF_DIO_REDUNDANCY 	2

#undef RPL_CONF_DIO_INTERVAL_MIN 
#define RPL_CONF_DIO_INTERVAL_MIN 	4	// I_MIN 16 ms (2^4)

#undef RPL_CONF_DIO_INTERVAL_DOUBLINGS 
#define RPL_CONF_DIO_INTERVAL_DOUBLINGS 14	// I_MAX 4,37 min (2^(18))
/***************************************/

// TABLES /*****************************/

#undef NBR_TABLE_CONF_MAX_NEIGHBORS 
#define NBR_TABLE_CONF_MAX_NEIGHBORS   	10	// Default: 15

#undef UIP_CONF_MAX_ROUTES 
#define UIP_CONF_MAX_ROUTES	      		20	// Default: 30
/**************************************/

#undef UIP_CONF_TCP 
#define UIP_CONF_TCP	0	// Disable TCP

#endif