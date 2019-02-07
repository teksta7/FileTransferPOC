import json
import boto3
import os
import sys
from ftplib import FTP

def lambda_handler(event, context):
    #---------------------------------------------------------------------------------------------------------
    # Reading request and fetch file from S3
    #---------------------------------------------------------------------------------------------------------
    os.chdir("/tmp/")
    filename = event['pathParameters']['filename'] + event['headers']['extension']
    print("Attempting to download " + filename + " From S3 bucket")

    s3 = boto3.resource('s3')
    s3bucket = "file-transfer-storage-poc"
    s3.Bucket(s3bucket).download_file(filename, filename)
    localfileread = open(filename, 'r')
    localread = open(filename, 'rb')
    #---------------------------------------------------------------------------------------------------------
    # Initiating file transfer
    #---------------------------------------------------------------------------------------------------------
    ftp = FTP('3.8.237.60') #New FTP instance with connection setting for EC2 instance
    ftp.login('AWS',"") #establish connection to server with login creds
    ftp.storbinary('STOR ' + filename, localread)
    ftp.quit() #close connection
    print ("Logging out file contents from temp lambda storage \n" + localfileread.read()) #read out file contents
    return {
        'statusCode': 200,
        'body': "File successfully transferred to external server" #Return response
    }
