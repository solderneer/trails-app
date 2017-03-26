// server.js

// BASE SETUP
// =============================================================================

// call the packages we need
var express    = require('express');        // call express
var app        = express();                 // define our app using express
var bodyParser = require('body-parser');
var Trails     = require('./app/models/trails');
var Points     = require('./app/models/points')

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
            trails.route = req.body.route;
            trails.distance = req.body.distance;
            trails.time = req.body.time;
            trails.likes = req.body.likes;
            trails.markers = req.body.markers;
            trails.picture = req.body.picture;
            trails.description = req.body.description;
            trails.latitude = req.body.latitude;
            trails.longitude = req.body.longitude;
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
            Trails.find(function(err, trails) {
                if(err)
                    res.send(err);

                res.json(trails);
            });
        });

    router.route('/trails/restricted')

        .get(function(req, res){
           longbot = req.query.longbot;
           longtop = req.query.longtop;
           latbot = req.query.latbot;
           lattop = req.query.lattop;

           Trails.find({}, 'latitude longitude', function(err, trails) {
                var response = [];
                if(err)
                    res.send(err);
                
                for(i = 0; i < trails.length; i++){
                    console.log(i);
                    var bool = trails[i].longitude > longbot && trails[i].latitude > latbot && trails[i].longitude < longtop && trails[i].latitude < lattop;
                    console.log(bool);
                    if(trails[i].longitude > longbot && trails[i].latitude > latbot && trails[i].longitude < longtop && trails[i].latitude < lattop){
                        console.log("Hey");
                        response.push(trails[i]);
                    }
                }

                res.json(response);
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
                trails.route = req.body.route;
                trails.distance = req.body.distance;
                trails.time = req.body.time;
                trails.likes = req.body.likes;
                trails.markers = req.body.markers;
                trails.picture = req.body.picture;
                trails.description = req.body.description;
                trails.latitude = req.body.latitude;
                trails.longitude = req.body.longitude;
                
                trails.save(function(err){
                    if(err) 
                        res.send(err);

                    res.json({message: 'Trail updated!'});
                });
            });
        });

    router.route('/points')
        
        .post(function(req, res){
            var points = new Points();
            points.name = req.body.name;
            points.picture = req.body.picture;
            points.description = req.body.description;
            points.image1 = req.body.image1;
            points.image2 = req.body.image2;
            points.image3 = req.body.image3;
            points.image4 = req.body.image4;
            points.caption1 = req.body.caption1;
            points.caption2 = req.body.caption2;
            points.caption3 = req.body.caption3;
            points.caption4 = req.body.caption4;

            points.save(function(err){
                if (err){
                    res.send(err);
                    console.log("I got here");
                }
                    res.json({message: 'Point created!'});
            });

        })


        .get(function(req, res){
            Points.find(function(err, points) {
                if(err)
                    res.send(err);

                res.json(points);
            });
        });

    router.route('/points/:point_id')

        .put(function(req, res) {
            Points.findById(req.params.point_id, function(err, points) {
                if(err)
                    res.send(err)

                points.name = req.body.name;
                points.picture = req.body.picture;
                points.description = req.body.description;
                points.image1 = req.body.image1;
                points.image2 = req.body.image2;
                points.image3 = req.body.image3;
                points.image4 = req.body.image4;
                points.caption1 = req.body.caption1;
                points.caption2 = req.body.caption2;
                points.caption3 = req.body.caption3;
                points.caption4 = req.body.caption4;
                
                points.save(function(err){
                    if(err) 
                        res.send(err);

                    res.json({message: 'Trail updated!'});
                });
            });
        })

        .get(function(req, res){
            Points.findById(req.params.point_id, function(err, points){
                if (err)
                    res.send(err);
                res.json(points);
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