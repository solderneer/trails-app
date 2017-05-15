module.exports = function(nano){
  var config = require("./config");

  var trails = nano.db.use("trails");
  var points = nano.db.use("points");
  var images = nano.db.use("images");
  var users = nano.db.use("users");
  var codes = nano.db.use("codes");

  var express = require("express");
  var bodyParser = require("body-parser");

  var jwt = require("jsonwebtoken");
  var multer = require("multer");
  var shortid = require("shortid");
  var async = require("async");
  var nodemailer = require("nodemailer");

  var storage = multer.diskStorage({
    destination : function(req, file, cb){
      cb(null, config.upload);
    },
    filename : function(req, file, cb){
      cb(null, shortid.generate() + "." + file.filename.split(".")[file.filename.split(".").length - 1]);
    }
  });
  var upload = multer({storage : storage});

  var transporter = nodemailer.createTransport(config.smtp);

  var api = express.Router();

  api.use(bodyParser.json());
  api.use(bodyParser.urlencoded({extended : true}));

  api.get("/", function(req, res){
    res.status(200).json({
      message : "Received"
    });
  });

  api.get("/trails", function(req, res){
    trails.list(function(err, body){
      if(err){
        res.status(500).json({
          message : "Database error",
          data : err
        });
      }else{
        async.each(body, function(trail, trail_cb){
          var points_data = [];
          async.each(body.points, function(point, point_cb){
            points.get(point, function(err, body){
              if(err){
                point_cb(err);
              }else{
                points_data.push(body);
              }
            });
          }, function(err){
            if(err){
              trail_cb(err);
            }else{
              body.points = points_data;
              trail_cb(null);
            }
          });
        }, function(err){
          if(err){
            res.status(500).json({
              message : "Database error",
              data : err
            });
          }else{
            res.status(200).json({
              message : "Success",
              trails : body
            });
          }
        });
      }
    });
  });

  api.get('/trail/:id', function(req, res){
    trails.get(req.params.id, function(err, body){
      if(err){
        if(err.error == "not_found"){
          res.status(404).json({
            message : "Trail not found",
            data : err
          });
        }else{
          res.status(500).json({
            message : "Database error",
            data : err
          });
        }
      }else{
        res.status(200).json({
          message : "Success",
          trail : body
        });
      }
    });
  });

  api.get("/trail/code/:code", function(req, res){
    trails.view('trails_design', "by_code", {keys : [req.params.code]}, function(err, body){
      if(err){
        res.status(500).json({
          message : "Database err",
          data : err
        });
      }else if(body.length < 1){
        res.status(400).json({
          message : "Code not found",
        })
      }else{
        trails.get(body[0].value, function(trail_err, trail_body){
          if(trail_err){
            res.status(500).json({
              message : "Database err",
              data : err
            });
          }else{
            res.status(200).json({
              message : "Success!",
              trail : trail_body
            });
          }
        });
      }
    });
  });

  api.post("/trail/new", upload.single("picture"), function(req, res){
    jwt.verify(req.body.token, config.secret, function(err, decoded){
      if(err){
        if(err.message == "invalid signature"){
          res.status(400).json({
            message : "Invalid authentication token",
            data : err
          });
        }else{
          res.status(400).json({
            message : "JWT Error",
            data : err
          });
        }
      }else{
        if(req.body.name == ""){
          res.status(400).json({
            message : "No trail name!"
          });
        }else if(req.body.description == ""){
          res.status(400).json({
            message : "No trail description!"
          });
        }else{
          var trail = {
            name : req.body.name,
            description : req.body.description,
            code : req.body.code,
            likes : 0,
            picture : req.file.filename,
            uploader : decoded.email,
            points : req.body.points
          };
          trails.insert(trail, encodeURI(req.body.name), function(err, body){
            if(err){
              //TODO: trail already exists
              res.status(500).json({
                message : "Database error",
                data : "err"
              });
            }else{
              res.status(200).json({
                message : "Success",
                trail : body
              });
            }
          });
        }
      }
    });
  });

  api.get("/point/:id", function(req, res){
    points.get(req.params.id, function(err, body){
      if(err){
        if(err.error == "not_found"){
          res.status(404).json({
            message : "Trail not found",
            data : err
          });
        }else{
          res.status(500).json({
            message : "Database error",
            data : err
          });
        }
      }else{
        res.status(200).json({
          message : "Success",
          point : body
        });
      }
    });
  });

  api.post("/point/new", function(req, res){
    jwt.verify(req.body.token, config.secret, function(err, decoded){
      if(err){
        if(err.message == "invalid signature"){
          res.status(400).json({
            message : "Invalid authentication token",
            data : err
          });
        }else{
          res.status(400).json({
            message : "JWT Error",
            data : err
          });
        }
      }else{
        if(req.body.name == ""){
          res.status(400).json({
            message : "No point name!"
          });
        }else if(req.body.description == ""){
          res.status(400).json({
            message : "No point description!"
          });
        }else if(!(req.body.lat < 90 && req.body.lat > -90 && req.body.lng < 180 && req.body.lng > -180)){
          res.status(400).json({
            message : "Invalid point latlng!"
          });
        }else{
          var point = {
            name : req.body.name,
            picture : req.file.filename,
            description : req.body.description,
            lat : req.body.lat,
            lng : req.body.lng,
            images : []
          };
          points.insert(point, shortid.generate(), function(err, body){
            if(err){
              res.status(500).json({
                message : "Database error",
                data : "err"
              });
            }else{
              res.status(200).json({
                message : "Success",
                point : body
              });
            }
          });
        }
      }
    });
  });

  api.post("/point/image", upload.single("image"), function(req, res){
    images.insert({caption : req.body.caption}, req.file.filename, function(err, body){
      if(err){
        res.status(500).json({
          message : "Database error",
          data : err
        });
      }else{
        res.status(200).json({
          message : "Success",
          image : body
        });
      }
    });
  });

  api.post("/user/new", function(req, res){
    if(req.body.email == ""){
      res.status(400).json({
        message : "No email",
        data : err
      });
    }else if(req.body.password == ""){
      res.status(400).json({
        message : "No password",
        data : err
      });
    }else if(req.body.name == ""){
      res.status(400).json({
        message : "No name",
        data : err
      });
    }else{
      users.get(req.body.email, function(err, body){
        if(err && err.error == "not_found"){
          var user_data = {
            email : req.body.email,
            name : req.body.name,
            password : req.body.password
          };
          codes.insert(user_data, shortid.generate, function(c_err, c_body){
            if(c_err){
              res.status(500).json({
                message : "Database error",
                data : c_err
              });
            }else{
              var mail_data = {
                from : config.smtp.auth.user,
                to : req.body.email,
                subject : 'Verify signup',
                html : '<a>http://' + config.host + '/api/user/new/' + c_body._id'</a>'
              }
              transporter.sendMail(mailOptions, function(error, info){
                  if (error) {
                    res.status(500).json({
                      message : "Error sending email",
                      data : error
                    });
                  }else{
                    res.status(200).json({
                      message : "Success!"
                    });
                  }
              });
            }
          });
        }else{
          res.status(400).json({
            message : "User with email already exists",
            data : err
          });
        }
      });
    }
  });

  //Email veriication
  api.get("/user/new/:code", function(req, res){
    codes.get(req.params.code, function(err, body){
      if(err){
        if(err.error == "not_found"){
          res.status(404).json({
            message : "Invalid code",
            data : err
          });
        }else{
          res.status(500).json({
            message : "Database error",
            data : err
          });
        }
      }else{
        var user_data = {
          name : body.name,
          password : body.password
        };
        users.insert(user_data, body.email, function(u_err, user){
          if(u_err){
            res.status(500).json({
              message : "Database error",
              data : u_err
            });
          }else{
            codes.destroy(req.params.code, body._rev, function(d_err, success_body){
              if(d_err){
                res.status(500).json({
                  message : "Database error",
                  data : d_err
                });
              }else{
                res.status(200).json({
                  message : "Success",
                  token : jwt.sign(body, config.secret),
                  expiry : config.expiry
                });
              }
            });
          }
        });
      }
    });
  });

  api.post("/login", function(req, res){
    users.get(req.body.email, function(err, body){
      if(err){
        if(err.error == "not_found"){
          res.status(404).json({
            message : "User not found",
            data : err
          });
        }else{
          res.status(500).json({
            message : "Database error",
            data : err
          });
        }
      }else{
        if(body.password === req.body.passord){
          res.status(200).json({
            message : "Success!",
            token : jwt.sign(body, config.secret),
            expiry : config.expiry
          });
        }else{
          res.status(400).json({
            message : "Incorrect password"
          });
        }
      }
    });
  });

  return api;
};