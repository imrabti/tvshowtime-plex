tvshowtime-plex
===============

**tvshowtime-plex** is a background java service compatible with all platforms (Windows, MacOS, Linux, NAS), that will 
allow you to mark automatically episodes in your TVShowTime account as soon as you did watch them in your Plex system.

Some details : 

  - tvshowtime-plex is a separate background service and is not a Plex Media Server plug-in, you are not forced to run it
  on the same server where PMS is installed, because it uses REST services offered by PMS.
  - It is built upon Java 8 and was tested on all platforms including Synology NAS. Java 8 is the only requirement to run
  this application.
  - It uses a REST API offered by PMS to check the watched episodes and mark them as watched in TVShowTime, no need to add
  a special log configuration to your PMS.

Installation
===============

**Linux, OSX, Windows**

First make sure that you have already installed Java 8 JRE in your computer, and that it is configured on the PATH.
You can download Java 8 right from Oracle website, or if you are using Linux you can also choose to install and use the OpenJRE from the packages.

After this you are done with the requirements to run **tvshowtime-plex**, the next step is to download the already compiled package **tvshowtime-plex** from : [tvshowtime-plex 1.0.1](https://github.com/imrabti/tvshowtime-plex/releases/download/1.0.1/tvshowtime-plex-1.0.1.zip)

Configuration
-------------

After downloading the application and extracting the archive in the folder you want to put it in, you need to edit the **application.properties** file inside the config folder first, below is what need to be done : 

There are two importants properties that needs to be configured correctly are : 

1. **nuvola.pms.path** this is the HTTP URL of your Plex Media Server, if you are going to run this application on the same server as PMS then the default provided value is good (no need to change it), if it is not the case then you need to put the correct URL for _example nuvola.pms.path = http://192.168.1.5:32400_
2. **nuvola.tvshowtime.tokenFile** this is the complete file path where you want your OAuth authorisation token to be stored, if you want the token to be stored in the folder where the application is then you are good with the default value. It is used so that you dont always have to go through all frustrating steps of configuring you TVShowTime account with this application.

```
# REQUIRED: nuvola.pms.path is the location of the http service exposed by Plex Media Server
# the default value should be 'ok', assuming you're running the tvshowtime-plex on the same machine
# where the PMS is installed
nuvola.pms.path = http://localhost:32400

# REQUIRED: Where do you wish to write the token used for authorizing access to
# you TVShow Time account, the default value should be 'ok'
nuvola.tvshowtime.tokenFile = session_token

# REQUIRED: Where do you wish to write the tvshowtime-plex log file.
logging.path = /tmp/tvshowtimeplex
logging.pattern.console=%d{HH:mm} %-5level - %msg%n
logging.level.root = ERROR
logging.level.org.nuvola.tvshowtime = INFO
```

After configuring the **application.properties** file you can now launch the service using the command :

```
java -jar tvshowtimeplex.jar
```

