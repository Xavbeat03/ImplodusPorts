# ImplodusPorts

A UI based port system for Minecraft.

## Commands

* ```/iports destroy <port_id>``` : Attempts to destroy the associated port
  * Permission Node: ```implodusports.admin.destroy``` 
* ```/iports changesize <1,2,3,4>``` : Changes the size of the port being looked at between 1 and 4
  * Permission Node: ```implodusports.admin.changesize``` 
* ```/iports reload``` : Reloads the config file
  * Permission Node: ```implodusports.admin.reload``` 

## Creating a port

To create a port, place a sign and do the following:
* Label the top line ```[Ports]```
* Label the second line the port name

**Note:** these commands can only be run by individuals with the appropriate admin permissions.
