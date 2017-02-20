#!/bin/bash
#How to use this script?
# 1. Download this script to where your 3 .csv files are saved
# 2. run this script with the following command
# 	sh analyser.sh "Your name" "Your id" "News site" "site address"
#
#    for example, my name is Weilun Chen, and my Id is 1111111111, and I need to crawl CNN, I type 
#	sh analyser.sh "Weilun Chen" "1111111111" "CNN"
# 3. All analysed results are in the file CrawlReport_yoursite.txt now
#
# P.S.
# a.  For the file site, your csv file should record the file size in bytes with integer.
# b.  Naming conventions should strictly follow the statement inside the assignment!!
#
# Write by Weilun Chen.
# CSCI 572 

name=$1
uscid=$2
site=$3

outputFile="CrawlReport_"$site".txt"
fetchFile="fetch_"$site".csv"
urlsFile="urls_"$site".csv"
visitFile="visit_"$site".csv"

rm $outputFile

echo "Name: "$name >> $outputFile
echo "USC ID: "$uscid >> $outputFile
echo "News site crawled: "$site".com\n" >> $outputFile

echo "Fetch Statistics\n================" >> $outputFile 
cat $fetchFile | wc -l | awk '{print "# fetches attempted: "$1}' >> $outputFile
cat $fetchFile | cut -f 2 -d ',' | egrep -c '200' | awk '{print "# fetches succeeded: "$1}' >> $outputFile
cat $fetchFile | cut -f 2 -d ',' | egrep -c '3..' | awk '{print "# fetches aborted: "$1}' >> $outputFile
cat $fetchFile | cut -f 2 -d ',' | egrep -c '[4-5]..' | awk '{print "# fetches failed: "$1}' >> $outputFile

echo "\nOutgoing Urls:\n================" >> $outputFile
cat $urlsFile | wc -l | awk '{print "Total URLs extracted: "$1}' >> $outputFile
cat $urlsFile | sort | uniq | wc -l | awk '{print "# unique URLs extracted: "$1}' >> $outputFile
cat $urlsFile | sort | uniq | cut -f 2 -d ',' | egrep -v 'N_OK' | wc -l | awk '{print "# unique URLs within News Site: "$1}' >> $outputFile
cat $urlsFile | sort | uniq | cut -f 2 -d ',' | egrep 'N_OK' | wc -l | awk '{print "# unique URLs outside News Site: "$1}' >> $outputFile

echo "\nStatus Codes:\n================" >> $outputFile
cat $fetchFile | cut -f 2 -d ',' | egrep -c '200' | awk '{print "200 OK: "$1}' >> $outputFile
cat $fetchFile | cut -f 2 -d ',' | egrep -c '301' | awk '{print "301 Moved Permanently: "$1}' >> $outputFile
cat $fetchFile | cut -f 2 -d ',' | egrep -c '302' | awk '{print "302 Unauthorized: "$1}' >> $outputFile
cat $fetchFile | cut -f 2 -d ',' | egrep -c '404' | awk '{print "404 Not Found: "$1}' >> $outputFile
cat $fetchFile | cut -f 2 -d ',' | egrep -c '502' | awk '{print "502 Bad gateway: "$1}' >> $outputFile

echo "\nFile Sizes:\n================" >> $outputFile
cat $visitFile | cut -f 2 -d ',' | sed -e 's#\(^\"\)\(.*\)\(\"$\)#\2#g' | awk '$1 < 1024 && $1 >= 0 {print $1;}' | wc -l | awk '{print "< 1KB: "$1}' >> $outputFile 
cat $visitFile | cut -f 2 -d ',' | sed -e 's#\(^\"\)\(.*\)\(\"$\)#\2#g' | awk '$1 < 10240 && $1 >= 1024 {print $1;}' | wc -l | awk '{print "1KB ~ <10KB: "$1}' >> $outputFile 
cat $visitFile | cut -f 2 -d ',' | sed -e 's#\(^\"\)\(.*\)\(\"$\)#\2#g' | awk '$1 < 102400 && $1 >= 10240 {print $1;}' | wc -l | awk '{print "10KB ~ <100KB: "$1}' >> $outputFile 
cat $visitFile | cut -f 2 -d ',' | sed -e 's#\(^\"\)\(.*\)\(\"$\)#\2#g' | awk '$1 < 1048576 && $1 >= 102400 {print $1;}' | wc -l | awk '{print "100KB ~ <1MB: "$1}' >> $outputFile 
cat $visitFile | cut -f 2 -d ',' | sed -e 's#\(^\"\)\(.*\)\(\"$\)#\2#g' | awk '$1 >= 1048576 {print $1;}' | wc -l | awk '{print ">= 1MB: "$1}' >> $outputFile 

echo "\nContent Types:\n================" >> $outputFile
cat $visitFile | cut -f 4 -d ',' | egrep ".*text/html.*" | wc -l | awk '{print "text/html: "$1}' >> $outputFile
cat $visitFile | cut -f 4 -d ',' | egrep ".*image/gif.*" | wc -l | awk '{print "image/gif: "$1}' >> $outputFile
cat $visitFile | cut -f 4 -d ',' | egrep ".*image/jpeg.*" | wc -l | awk '{print "image/jpeg: "$1}' >> $outputFile
cat $visitFile | cut -f 4 -d ',' | egrep ".*image/png.*" | wc -l | awk '{print "image/png: "$1}' >> $outputFile
cat $visitFile | cut -f 4 -d ',' | egrep ".*application/pdf.*" | wc -l | awk '{print "application/pdf: "$1}' >> $outputFile
cat $visitFile | cut -f 4 -d ',' | egrep ".*image/vnd.microsoft.icon.*" | wc -l | awk '{print "application/pdf: "$1}' >> $outputFile



