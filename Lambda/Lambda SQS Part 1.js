var QUEUE_URL = 'https://sqs.eu-west-2.amazonaws.com/356930079321/file-transfer-poc';
var AWS = require('aws-sdk');
var sqs = new AWS.SQS({ region: 'eu-west-2' });
var error = {
  code: "SERVICE_UNAVAILABLE",
  message: "Dependent systems are not currently responding"
};

exports.handler = function(event, context) {
    var params = {
        MessageBody: JSON.stringify(event),
        QueueUrl: QUEUE_URL
    };
    sqs.sendMessage(params, function(err, data) {
        if (err) {
            console.log('error:', "Failed to Send Message" + err);
            context.done(null, error); // ERROR with message
        }
        else {
            console.log('Pushing data with correlationID:', data.MessageId);
            context.succeed('Successfully pushed transfer request into SQS queue'); // SUCCESS
        };
    });
}
