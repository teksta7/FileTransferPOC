import json
import boto3
from ftplib import FTP

def lambda_handler(event, context):
    ftp = FTP('3.8.173.47') #New FTP instance with connection setting for EC2 instance
    ftp.login('AWS',"") #establish connection to server with login creds
    files = ftp.dir() #variable of local directory
    print (files) #print local directory
    ftp.cwd('/External/') #navigate to directory on server where file is located
    print (ftp.pwd())
    print (ftp.dir())
    responseBody = open('/tmp/sample.json','wb') #Create local file on temp lambda storage
    ftp.retrbinary('RETR sample.json', responseBody.write, 1024) #retrieve file on server
    ftp.quit() #close connection
    responseBody.close() #close file IO stream
    text = open("/tmp/sample.json") #Open file for logging
    print ("Logging out file contents from temp lambda storage \n" + text.read()) #read out file contents
    return {
        'statusCode': 200,
        'body': "File successfully retreived and put into S3 Bucket" #Return response
    }
