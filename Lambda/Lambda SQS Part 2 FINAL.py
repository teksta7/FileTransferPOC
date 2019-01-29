import json
import boto3
from ftplib import FTP

def lambda_handler(event, context):
    #---------------------------------------------------------------------------------------------------------
    # Reading payload and prep
    #---------------------------------------------------------------------------------------------------------
    body = json.loads(json.dumps(event)) #convert JSON object to python object
    #---------------------------------------------------------------------------------------------------------
    # Initiating file transfer
    #---------------------------------------------------------------------------------------------------------
    ftp = FTP('3.8.173.47') #New FTP instance with connection setting for EC2 instance
    ftp.login('AWS',"") #establish connection to server with login creds
    files = ftp.dir() #variable of local directory
    print (files) #print local directory
    ftp.cwd(body['location']) #navigate to directory on server where file is located
    print (ftp.pwd())
    print (ftp.dir())
    responseBody = open('/tmp/' + body['filename'],'wb') #Create local file on temp lambda storage
    ftp.retrbinary('RETR ' + body['filename'], responseBody.write, 1024) #retrieve file on server
    ftp.quit() #close connection
    responseBody.close() #close file IO stream
    text = open("/tmp/" + body['filename']) #Open file for logging
    print ("Logging out file contents from temp lambda storage \n" + text.read()) #read out file contents
    #---------------------------------------------------------------------------------------------------------
    # Pushing file to s3 bucket
    #---------------------------------------------------------------------------------------------------------

    s3 = boto3.resource('s3')
    s3bucket = "file-transfer-storage-poc"

    s3.Bucket(s3bucket).upload_file("/tmp/" + body['filename'], body['filename'])
    print ("S3 Accepted file with no error")
    return {
        'statusCode': 200,
        'body': "File successfully retreived and put into S3 Bucket" #Return response
    }
