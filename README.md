# Log Analytics

## Getting Started

This Project returns the top-n most frequent visitors and urls for each day of the trace for the following dataSet ftp://ita.ee.lbl.gov/traces/NASA_access_log_Jul95.gz

### Prerequisites

Install Docker on your Machine

https://www.docker.com/products/docker-desktop

### The Docker Image for this project is in the below path

To run the image on your machine

Pull the image from Docker Hub

```
docker pull gr8rajas/webloganalytics:latest
```

Run the image

```
docker run -it webloganalytics:latest
```


The console accepts an input from user. Press Enter for default functionality else Press any number to retrieve top visitors and urls for each day

Example Input

```
 Number of top visitors are you looking for?
 Just press enter to give top 3 visitors else enter number

1

 Retrieving results for top 1
```


Once the input is read from the user, the output is displayed as below

```
+------------+-----------------------+---------------------------------------------------+-------------------+---+
|receive_date|host                   |url                                                |no_of_times_visited|top|
+------------+-----------------------+---------------------------------------------------+-------------------+---+
|01/Jul/1995 |burger.letters.com     |/images/NASA-logosmall.gif                         |118                |1  |
|02/Jul/1995 |piweba3y.prodigy.com   |/shuttle/missions/sts-71/sts-71-patch-small.gif    |187                |1  |
|03/Jul/1995 |134.83.184.18          |/shuttle/countdown/video/livevideo.gif             |413                |1  |
|04/Jul/1995 |piweba3y.prodigy.com   |/shuttle/missions/sts-71/sts-71-patch-small.gif    |163                |1  |
|05/Jul/1995 |piweba3y.prodigy.com   |/shuttle/missions/sts-71/sts-71-patch-small.gif    |106                |1  |
|06/Jul/1995 |spidey.cor.epa.gov     |/icons/menu.xbm                                    |99                 |1  |
|07/Jul/1995 |morphine.iris.com      |/icons/menu.xbm                                    |82                 |1  |
|08/Jul/1995 |piweba3y.prodigy.com   |/history/apollo/apollo-13/apollo-13-patch-small.gif|85                 |1  |
|09/Jul/1995 |piweba4y.prodigy.com   |/history/apollo/apollo-13/apollo-13-patch-small.gif|77                 |1  |
|10/Jul/1995 |163.185.36.150         |/htbin/cdt_clock.pl                                |277                |1  |
|11/Jul/1995 |bill.ksc.nasa.gov      |/htbin/cdt_main.pl                                 |465                |1  |
|12/Jul/1995 |indy.gradient.com      |/htbin/cdt_main.pl                                 |452                |1  |
|13/Jul/1995 |rush.internic.net      |/htbin/cdt_clock.pl                                |547                |1  |
|14/Jul/1995 |siltb10.orl.mmc.com    |/images/launch-logo.gif                            |296                |1  |
|15/Jul/1995 |siltb10.orl.mmc.com    |/images/launch-logo.gif                            |297                |1  |
|16/Jul/1995 |siltb10.orl.mmc.com    |/images/launch-logo.gif                            |294                |1  |
|17/Jul/1995 |siltb10.orl.mmc.com    |/images/KSC-logosmall.gif                          |289                |1  |
|18/Jul/1995 |siltb10.orl.mmc.com    |/images/KSC-logosmall.gif                          |180                |1  |
|19/Jul/1995 |siltb10.orl.mmc.com    |/images/KSC-logosmall.gif                          |287                |1  |
|20/Jul/1995 |siltb10.orl.mmc.com    |/images/launch-logo.gif                            |298                |1  |
|21/Jul/1995 |siltb10.orl.mmc.com    |/images/launch-logo.gif                            |459                |1  |
|22/Jul/1995 |currypc.fpl.msstate.edu|/htbin/cdt_main.pl                                 |700                |1  |
|23/Jul/1995 |currypc.fpl.msstate.edu|/htbin/cdt_main.pl                                 |709                |1  |
|24/Jul/1995 |currypc.fpl.msstate.edu|/htbin/cdt_main.pl                                 |303                |1  |
|25/Jul/1995 |edams.ksc.nasa.gov     |/ksc.html                                          |37                 |1  |
|26/Jul/1995 |arctic.nad.northrop.com|/history/apollo/images/apollo-logo1.gif            |37                 |1  |
|27/Jul/1995 |edams.ksc.nasa.gov     |/ksc.html                                          |37                 |1  |
|28/Jul/1995 |pcmas.it.bton.ac.uk    |/htbin/cdt_clock.pl                                |328                |1  |
+------------+-----------------------+---------------------------------------------------+-------------------+---+
```
