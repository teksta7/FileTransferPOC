#!/bin/bash
echo '"Welcome to batch processing"'
#========================================
latestdate="0"
randomFile="Windows"
FILES=`ls /tmp/storage/*` #ECS TESTING
#FILES=`ls /home/ec2-user/storage/*` #EC2 TESTING
#FILES=`ls /home/jake/Downloads/test/*`#LOCAL TESTING
fileToCopy=""
echo $FILES
#========================================
# Find latest file in given directory
#========================================
for f in $FILES; do
    #echo "File found"
    localdate=$(date -d @$( stat -c %Y $f ) +%Y%m%d%H%M%S)
    echo "Was last modified on" $localdate
    if [ "$localdate" -gt "$latestdate" ];then
        latestdate=$localdate
        echo "Latest file found is" $f "with a timestamp of" $latestdate
        fileToCopy=$f
    fi
done
#========================================
# check if file already exists in S3 bucket
##(REQUIRES AWS CLI IN ENV)
#========================================
#fileToCopy=$(echo $fileToCopy| cut -d'/' -f 6) #LOCAL TESTING
#fileToCopy=$(echo $fileToCopy| cut -d'/' -f 5) #EC2 TESTING
fileToCopy=$(echo $fileToCopy| cut -d'/' -f 4) #ECS TESTING
echo "Checking S3 bucket if $fileToCopy is present" 
CHECK=$(aws s3 ls s3://file-transfer-storage-poc/ --recursive | grep $fileToCopy)
if [ -n "$CHECK" ]; then
  echo "it does exist, exiting"
    exit 1
else
  echo "File doesn't exist, continuing..."
fi

#========================================
# Validate if valid json (Optional)
##(REQUIRES JQ IN ENV)
#========================================

#========================================
# Move file to S3
#========================================
echo "Pushing file to S3..."
aws s3 cp $fileToCopy s3://file-transfer-storage-poc
echo "Batch Complete"
