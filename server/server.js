// server.js

// BASE SETUP
// =============================================================================

// call the packages we need
var express    = require('express');        // call express
var app        = express();                 // define our app using express
var bodyParser = require('body-parser');
var Trails     = require('./app/models/trails');

//Set up mongoose connection
var mongoose = require('mongoose');
var options = { server: { socketOptions: { keepAlive: 300000, connectTimeoutMS: 30000 } }, 
                replset: { socketOptions: { keepAlive: 300000, connectTimeoutMS : 30000 } } };       
 
var mongodbUri = 'mongodb://user:21092000@ds141950.mlab.com:41950/trails';
 
mongoose.connect(mongodbUri, options);
var conn = mongoose.connection;             
 
conn.on('error', console.error.bind(console, 'connection error:'));  
 
conn.once('open', function() {
    
    // configure app to use bodyParser()
    // this will let us get the data from a POST
    app.use(bodyParser.urlencoded({ extended: true }));
    app.use(bodyParser.json());

    var port = process.env.PORT || 8000;        // set our port

    // ROUTES FOR OUR API
    // =============================================================================
    var router = express.Router();              // get an instance of the express Router

    // middleware to use for all requests
    router.use(function(req, res, next) {
        // do logging
        console.log('Something is happening.');
        next(); // make sure we go to the next routes and don't stop here
    });


    // test route to make sure everything is working (accessed at GET http://localhost:8080/api)
    router.get('/', function(req, res) {
        res.json({ message: 'hooray! welcome to our api!' });   
    });

    router.route('/trails')

        .post(function(req, res){ 
            console.log("I got here");
            var trails = new Trails();
            trails.name = req.body.name;
            trails.longitude = 0;
            trails.latitude = 0;
            trails.distance = 0;
            trails.time = 0;
            trails.time = 0;
            trails.Likes = 0;
            trails.markers = 0;
            trails.picture = "";
            console.log(trails);
            console.log(trails.name);

            trails.save(function(err){
                if (err){
                    res.send(err);
                    console.log("I got here");
                }
                    res.json({message: 'Trail created!'});
            });

        });

        // REGISTER OUR ROUTES -------------------------------
        // all of our routes will be prefixed with /api
        app.use('/api', router);

        // START THE SERVER
        // =============================================================================
        app.listen(port);
        console.log('Magic happens on port ' + port);
         
});