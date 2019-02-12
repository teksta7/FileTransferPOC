import json
import boto3
import paramiko

def lambda_handler(event, context):
    # TODO implement

    s3_client = boto3.client('s3')
    #Download private key file from secure S3 bucket
    s3_client.download_file('file-transfer-backup-storage-poc','Windows.pem', '/tmp/Windows.pem')

    k = paramiko.RSAKey.from_private_key_file("/tmp/Windows.pem")
    c = paramiko.SSHClient()
    c.set_missing_host_key_policy(paramiko.AutoAddPolicy())

    host=event['ec2-3-8-174-199.eu-west-2.compute.amazonaws.com']
    print ("Connecting to " + host)
    c.connect( hostname = host, username = "ec2-user", pkey = k )
    print ("Connected to " + host)

    commands = [
        "pwd",
        "sudo rm-rf efsDEMO",
        "sudo mkdir efsDEMO",
        "sudo mount -t efs fs-8252bf73:/ efsDEMO"
        "aws s3 cp s3://file-transfer-storage-poc/clamav_defs/main.cvd /home/ec2-user/efsDEMO/main.cvd"
        ]
    for command in commands:
        print ("Executing {}".format(command))
        stdin , stdout, stderr = c.exec_command(command)
        print (stdout.read())
        print (stderr.read())

    return {
        'statusCode': 200,
        'body': json.dumps('Successfully Transfered file from S3 to EFS')
    }
