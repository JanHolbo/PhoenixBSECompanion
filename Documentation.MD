#Documentation

XML Interface	 [Recruit]	
XML interface is accessed by going to Personal -> XML Access and generating a full access code.
This then creates a <ID> and <Code> that allows you to access the site data via your user details.

The general request to get the XML pages is: 

http://www.phoenixbse.com/index.php?a=xml&uid=<ID>&code=<Code>&sa=<request>

The <request> is either:

info_data - gets current info that you know about
order_data - gets current order data
pos_list - gets a list of your positions and locations
send_orders - sends a set of orders posted as XML
game_status - gets teh game status
turn_data&tid=<position number>- gets a self contained HTML turn
pending_orders&pid=<position number>- gets the pending orders for a turn 
notes - gets the notifications for your account
items - gets all the items and their data that you know about
systems - gets all the systems and cbodies that you know about
base_data - gets all items in bases + extra info


Notes:
[1] turn_data can have a &type=file which makes it return the filename of the html fragment on the site
[2] when turn_data fails it now returns all the possible turns with file names / last update stamps
[3] Pending orders returns the pending orders (as seen on the turns) if there is no current queued orders. Otherwise it returns the orders in the current queued turn. If you add type=turn you always get the orders that the off-line game has stacked for you and if you add type=current you only get the current orders that are queued.
Notifcations defines	 [Recruit]	
NOTE_TURN_DATA => 1			- position_id,system_id,type,squad
NOTE_NEW_POSITION => 2				- position_id,system_id,type,squad
NOTE_ACTIVE_MISSION => 3			- mission_id,pos_number
NOTE_NEW_MISSION => 4				- mission_id
NOTE_BATTLE => 5				- location_name,number ships,system_id
NOTE_DELIVERY => 6				- position_id,system_id,quant,item_id,deliver_to_id
NOTE_PICKUP => 7				- position_id,system_id,quant,item_id,picked_up_from_id
NOTE_BUY => 8					- position_id,system_id,quant,item_id,bought_from_id,stellars
NOTE_SELL => 9					- position_id,system_id,quant,item_id,sold_to_id,stellars
NOTE_TRANSFER_IN => 10				- position_id,stellars
NOTE_POSITION_TRANSFER => 11			- position_id,system_id,type,transfer_id
NOTE_RELATIONS_CHANGE => 12			- aff_id,status
NOTE_SYSTEM_CHARTER => 13			- aff_id,status,charter_id - disabled
NOTE_RESTRICTED_STARBASE => 14			- position_id
NOTE_RESTRICTED_CBODY => 15			- system_id,cbody_id
NOTE_RESTRICTED_SYSTEM_LINK => 16	        - system_id
NOTE_RESTRICTED_ITEM => 17			- item_id
NOTE_RESTRICTED_ORDER => 18			- order_id
NOTE_RESTRICTED_MISSION => 19		        - mission_id
NOTE_RESTRICTED_SYSTEM_LOC => 20		- system_id
NOTE_DELIVERED_TO => 21				- position_id,system_id,quant,item_id,by_id
NOTE_PICKED_UP_FROM => 22			- position_id,system_id,quant,item_id,by_id
NOTE_BOUGHT_FROM => 23				- position_id,system_id,quant,item_id,by_id,stellars
NOTE_SOLD_TO => 24				- position_id,system_id,quant,item_id,by_id,stellars
NOTE_BOARDED => 25				- loc,system_id,position_id
NOTE_RAIDED => 26				- loc,system_id,position_id
NOTE_COMBAT_TRANSACTION => 27			- loc,system_id,position_id
NOTE_SEL_POSITION => 28				- position_id,amount


//coming soon
NOTE_RESTRICTED_DATA => 29			- restricted_type,data0,data1
NOTE_PLANETARY_SALES => 30			- position_id,system_id,cbody_id,trade_type,stellars
NOTE_NEXUS_CASH => 31				- position_id,stellars
NOTE_GM_NOTE => 32				- position_id,group_id,system_id,cbody_id,subject + msg  
NOTE_RUMOUR => 33				- data
NOTE_SPOTTED => 34				- position_id,system_id,location_name - not done yet
NOTE_TURN_ERROR => 35				- position_id,system_id,cbody_id,error_code,txt
NOTE_WARNING => 36				- position_id,system_id,cbody_id,warning_code,txt
NOTE_RESEARCH_FINISHED => 37			- position_id,item_id
NOTE_COMPLEX_VISIT => 38			- position_id,base_id,complex_type (txt)
NOTE_TRANSFER_OUT => 39				- to_position_id,stellars
NOTE_ORBITAL_DROP => 40				- position_id,system_id,quant,item_id,by_id - like deliver
NOTE_ORBITAL_RESUPPLY => 41			- position_id,system_id,quant,item_id,by_id - like deliver to
NOTE_SPECIAL_ACTION => 42			- position_id,header + text
NOTE_MESSAGE => 43				- src_pos_id,to_pos_id + message
NOTE_AGENT_ACTION => 44				- position_id,system_id,action_text + text
NOTE_REMINDER => 45				- position_id,note (80 char) + order at add
NOTE_COMPLEX_CHANGE => 46			- position_id,complex_type,built/scrapped/activated/deactivated,number
NOTE_ACCOUNT_LOW => 47				- cash, account below £10 - not done yet
NOTE_REGISTERED_BASE_ACTIVITY => 48		- position_id,system_id,cbody_id,action_type  (attacked/bought/registed/deregistered)		
NOTE_OPPORTUNITY_FIRE => 49			- loc,system_id,position_id + txt
NOTE_ESCAPING_COMBAT => 50			- loc,system_id,position_id + txt

      
WARNING_DEEPCORE_STRUCT => 0 			'Insufficient structural modules to deep core'                   
WARNING_COMPLEXES_NOT_PRODUCING => 1 		'Complexes are not producing'
WARNING_SB_EFFICIENCY_LOW => 2 			'Base Efficiency has dropped below 100%'
WARNING_SB_SECURITY_LOW => 3 			'Base security has dropped below 100%'
WARNING_SHIP_INTEGRITY_LOW => 4 		'Ship integrity is low (<25%)'
WARNING_SCOUTED => 5 				'Scouted'
WARNING_BASE_SUBVERTED => 6 			'Base subverted'
WARNING_BASE_REGISTRATION_REVOKED => 7 		'Registration Revoked'
		
	
TURN_ERROR_NO_STARGATE_KEY => 0 		'has no stargate key'			
TURN_ERROR_NO_KNOW_BASE => 1 			'has no knowledge of base'
TURN_ERROR_NO_KNOW_SYSTEM => 2 			'has no knowledge of system'
TURN_ERROR_NO_KNOW_CBODY => 3 			'has no knowledge of celestial body'
TURN_ERROR_INCORRECT_SEC => 4 			'has incorrect security clearance'
TURN_ERROR_NO_THRUST => 5 			'does not have enough thrust to take off/land'
TURN_ERROR_MAINTENANCE_FAILED => 6 		'failed to maintain'
		

COMPLEX_ACTION_BUILT => 0 			'built'
COMPLEX_ACTION_SCRAPPED => 1 			'scrapped'
COMPLEX_ACTION_ACTIVATED => 2 			'activated'
COMPLEX_ACTION_DEACTIVATED => 3 		'deactivated'
                

REGISTERED_BASE_REGISTERED => 0 		'registed'
REGISTERED_BASE_DEREGISTERED => 1 		'de-registered'
REGISTERED_BASE_ATTACKED => 2 			'been attacked'
REGISTERED_BASE_BOUGHT => 3 			'been bought'
             

COMPLEX_VISIT_RECREATION => 0 			'used recreation complexes'
COMPLEX_VISIT_REPAIR => 1 			'repaired'
COMPLEX_VISIT_MAINTENANCE => 2 			'maintained'
COMPLEX_VISIT_UPGRADE => 3 			'upgraded'
COMPLEX_VISIT_REFIT => 4 			'refitted'
                 
            
TRADETYPE_TRADE => 0 				'Trade'
TRADETYPE_LIFE => 1 				'Life'
TRADETYPE_DRUGS => 2 				'Drugs'

Flags	 [Recruit]	
Order Catagory Flags
====================
RDERFLAG_ANY                     0x0           
ORDERFLAG_THRUSTMOVE              0x1           
ORDERFLAG_MOVEMENT                0x2           
ORDERFLAG_TRANSACTION             0x4           
ORDERFLAG_STANDINGORDER           0x8           
ORDERFLAG_BASIC                   0x10           
ORDERFLAG_SCAN                    0x20           
ORDERFLAG_OTHER                   0x40           
ORDERFLAG_ISSUE                   0x80           
ORDERFLAG_CREATE                  0x100           
ORDERFLAG_SQUADRONORDER           0x200           
ORDERFLAG_SQUADRONSTANDINGORDER   0x400           
ORDERFLAG_TURNTYPE                0x800           
ORDERFLAG_BOARDING                0x1000           
ORDERFLAG_PLANETARYINTERACTION    0x2000           
ORDERFLAG_MACRO                   0x4000           
ORDERFLAG_COPY                    0x8000           
ORDERFLAG_FULL                    0xfffff           

Position Type
=============
POSITIONTYPE_NONE                 0x00           
POSITIONTYPE_GP                   0x01           
POSITIONTYPE_SHIP                 0x02           
POSITIONTYPE_STARBASE             0x03           
POSITIONTYPE_DEBRIS               0x04           
POSITIONTYPE_POLITICAL            0x05           
POSITIONTYPE_PLATFORM             0x06           
POSITIONTYPE_AGENT                0x07           

Position Flags
==============
POSFLAG_NONE                      0x00           
POSFLAG_GP                        0x01           
POSFLAG_SHIP                      0x02           
POSFLAG_STARBASE                  0x04           
POSFLAG_POLITICAL                 0x08           
POSFLAG_PLATFORM                  0x10           
POSFLAG_AGENT                     0x20           
POSFLAG_DEBRIS                    0x40    

Periphery Names / IDs
=====================
Cluster                           3
Caliphate                         9
Coreward Arm                      14
Darkfold                          2
Dewiek Home                       4
Dewiek Pocket                     5
Detinus Republic                  6
Flagritz Empire                   10
Felini Empire                     11
Halo                              13
Inner Capellan                    1
Inner Empire                      8
None                              0
Orion Spur                        16
Outer Capellan                    12
Perfidion Reach                   17
Transpiral                        15
Twilight                          7
