DS SUBSCRIBER
===============

This application contains a J2SE web server to capture events & Angular UI to display them.

Build
----------------
mvn package

Installation
----------------
Unzip `target/subscriber-2.0.zip` in your destination directory

```	
/subscriber-2.0
   /angularUI
   /lib
   /postman
   startServer.sh
```
						
Start Server
---------------------------

`$ ./startServer.sh <port>  with default port=9000]`

Requests are persisted in `tmp`

API
-------
* **POST** - `/event` : Event creation
* **GET** - `/history` : List all events
* **DELETE** - `/history` : Delete all events
* **GET** - `/current` : Get the last event

Postman
----------
import postman/subscriber.json.postman_collection to play with subsciber api
		
Angular UI
----------
Open `ui/index.html`
	



