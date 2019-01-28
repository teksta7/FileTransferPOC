var AWS = require('aws-sdk');

exports.handler = function(event, context) {
    console.log('Lambda SFTP invoked');
    console.log('Attempting to get ' + event.filename);
    console.log('Connecting to  ' + event.location);
    var s3 = new AWS.S3();
    var params = {
        Bucket: 'file-transfer-storage-poc',
        Key: event.filename,
        Body: JSON.stringify(event)
    }
    s3.putObject(params, function(err, data) {
        if (err) console.log(err, err.stack); // an error occurred
        else console.log(data); // successful response
    });
}
