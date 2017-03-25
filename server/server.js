// server.js

// BASE SETUP
// =============================================================================

// call the packages we need
var express    = require('express');        // call express
var app        = express();                 // define our app using express
var bodyParser = require('body-parser');
var Trails     = require('./app/models/trails');
var boundaries = require('./app/modules/boundaries')

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
            trails.longitude = req.body.longitude;
            trails.latitude = req.body.latitude;
            trails.distance = req.body.distance;
            trails.time = req.body.time;
            trails.likes = req.body.likes;
            trails.markers = req.body.markers;
            console.log(trails);
            console.log(trails.name);

            trails.save(function(err){
                if (err){
                    res.send(err);
                    console.log("I got here");
                }
                    res.json({message: 'Trail created!'});
            });

        })

        .get(function(req, res) {
            Trails.find({}, {name:1, longitude:1, latitude:1}, function(err, trails) {
                if(err)
                    res.send(err);

                console.log(boundaries);
                res.json(trails);
            });
        });

    router.route('/trails/:trail_id')

        .get(function(req, res){
            Trails.findById(req.params.trail_id, function(err, trails){
                if (err)
                    res.send(err);
                res.json(trails);
            });
        })

        .put(function(req, res) {
            Trails.findById(req.params.trail_id, function(err, trails) {
                if(err)
                    res.send(err)

                trails.name = req.body.name;
                trails.longitude = req.body.longitude;
                trails.latitude = req.body.latitude;
                trails.distance = req.body.distance;
                trails.time = req.body.time;
                trails.likes = req.body.likes;
                trails.markers = req.body.markers;
                
                trails.save(function(err){
                    if(err) 
                        res.send(err);

                    res.json({message: 'Trail updated!'});
                });
            });
        });

    router.route('/boundaries')

        .post(function(req, res){
            boundaries.longbot = req.body.longbot;
            boundaries.longtop = req.body.longtop;
            boundaries.latbot = req.body.latbot;
            boundaries.lattop = req.body.lattop;
            res.json({message: 'Boudaries set!'});
        });

        // REGISTER OUR ROUTES -------------------------------
        // all of our routes will be prefixed with /api
        app.use('/api', router);

        // START THE SERVER
        // =============================================================================
        app.listen(port);
        console.log('Magic happens on port ' + port);
         
});