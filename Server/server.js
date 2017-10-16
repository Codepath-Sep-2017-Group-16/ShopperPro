var express = require('express'),
    app = express(),    
    bodyParser = require('body-parser'),
    messaging = require('./messagingHelper'),
    router = express.Router();

app.use(bodyParser.json());

// Middleware
router.use(function(req, res, next) {
    if (req.method === 'OPTIONS') {
        var headers = {};
        headers["Access-Control-Allow-Origin"] = "*";
        headers["Access-Control-Allow-Methods"] = "POST, GET, PUT, DELETE, OPTIONS";
        headers["Access-Control-Allow-Credentials"] = false;
        headers["Access-Control-Max-Age"] = '86400'; // 24 hours
        headers["Access-Control-Allow-Headers"] = "X-Requested-With, X-HTTP-Method-Override, Content-Type, Accept";
        res.writeHead(200, headers);
        res.end();
    } else {
        res.setHeader("Access-Control-Allow-Origin", "*");
        next();
    }
});


// Post current location of the user
router.route('/users/:user_id')
    .post(function(req, res) {
        messaging.SendMessage("f8nT5Nahca4:APA91bFWGBiyE0fbppBroOP_ze9434Dvjuf3fYFMZ5UWR7ylEmEqFE4it_1EBL_fJdhUX3n5g6tPMAeRGtsTKy2oQRWFlyNshAKt1xQFPuN2XcLNJ0Dsndz8eyFrBuVE0HfJLKG8IIdc")        
        var status = 200        
        res.status(status);        
    });


app.use('/api', router);

var server = app.listen(8081, function() {
    var host = server.address().address
    var port = server.address().port

    console.log("SocialShopper - Listening at http://%s:%s", host, port)
})
