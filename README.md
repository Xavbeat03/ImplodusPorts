# ImplodusPorts

A UI based port system for Minecraft.

![IportsImage](https://github.com/Xavbeat03/ImplodusPorts/assets/58835993/d82ed2ca-bd55-4606-ac2f-b6986c15917a)


## Commands

* ```/iports destroy <port_id>``` : Attempts to destroy the associated port
  * Permission Node: ```implodusports.admin.destroy``` 
* ```/iports changesize <1,2,3,4>``` : Changes the size of the port being looked at between 1 and 4
  * Permission Node: ```implodusports.admin.changesize``` 
* ```/iports reload``` : Reloads the config file
  * Permission Node: ```implodusports.admin.reload```
* ```/iports next``` : Starts the next travel of a multi-part journey 
  * Permission Node: N/A
* ```/iports``` : Gives a status on the plugin
  * Permission Node: N/A

## Creating a port

Admins can create ports by doing the following:
* Place a sign
* Label the top line ```[Port]```
* Label the second line the port name

Afterwards, the sign should become auto-formatted, and the plugin will inform you that the port was created.

## Using a port

To use a port, do the following:
* Right-click it
* Select the port you wish to travel to
* Pay the needed funds and travel.

## Other notes
Ports will delete themselves if they're in a claimed towny townblock and that townblock is either deleted or unclaimed.

## Dependencies

* Towny 0.98.6.0 and up
* VaultAPI
* Some Economy Plugin
* (Soft) Dynmap

**Note:** these commands can only be run by individuals with the appropriate admin permissions.
