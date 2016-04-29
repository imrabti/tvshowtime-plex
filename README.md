tvshowtime-plex
===============

**tvshowtime-plex** is a background java service compatible with all plateforms (Windows, MacOS, Linux, NAS), that will 
allow you to mark automatically episodes in your TVShowTime account as soon as you did watch them in you Plex system.

Some details : 

  - tvshowtime-plex is a separate background service and is not a Plex Media Server plug-in, you are not forced to run it
  on the same server where PMS is installed, because it uses REST services offered by PMS.
  - It is built upon Java 8 and was tested on all plateforms including Synology NAS. Java 8 is the only requirement to run
  this application.
  - It uses a REST API offered by PMS to check the watched episodes and mark them as watched in TVShowTime, no need to add
  a special log configuration to your PMS.
