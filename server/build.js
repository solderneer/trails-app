var config = require("./config");

var request = require("request");
var shortid = require("shortid");
var ora = require("ora");

var db_check = ora("Checking for db presence...");
db_check.start();

var create_db = function(db_name, cb){
  var nano = require("nano")(config.db_location);
  var spinner_check = ora("Checking " + db_name + "...");
  spinner_check.start();
  request(config.db_location + "/" + db_name, function(err, res, body){
    spinner_check.stop();
    console.log("Checking " + db_name + "...Done");
    if(err){
      console.error(err);
      throw err;
    }
    if(res.statusCode == 200){
      console.log(db_name + " already exists.");
      if(cb) cb();
    }else{
      var spinner_create = ora(db_name + " does not exist. Creating...");
      spinner_create.start();
      nano.db.create(db_name, function(err, body){
        spinner_create.stop();
        if(err){
          console.error(err);
          throw err;
        }
        console.log(db_name + " does not exist. Creating...Done");
        if(cb) cb();
      });
    }
  });
}

request(config.db_location, function(err, res, body){
  db_check.stop();
  if(err){
    console.error(err);
    throw err;
  }
  if(res.statusCode == 200){
    console.log("Checking for db presence...Done");


    create_db("trails", function(){
      var view_spinner = ora("Creating view for trails...");
      view_spinner.start();
      var nano = require("nano")(config.db_location);
      var trails = nano.db.use("trails");
      //TODO: Document already exists
      trails.get('/_design/trails_design', function(err, body){
        if(!body){
          trails.insert({ "views":
            { "by_code":
              { "map": function(doc) {
                  emit(doc.code, doc._id);
                }
              }
            }
          }, '/_design/trails_design', function(err, body){
            if(err){
              console.log(err);
              throw err;
            }
            view_spinner.stop();
            console.log("Creating view for trails...Done");
          });
        }else{
          if(body.views.by_code != undefined){
            view_spinner.stop();
            console.log("View already exists");
          }else{
            trails.insert({ "views":
              { "by_code":
                { "map": function(doc) {
                    emit(doc.code, doc._id);
                  }
                }
              },
              "_rev" : shortid.generate()
            }, '/_design/trails_design', function(err, body){
              if(err){
                console.log(err);
                throw err;
              }
              view_spinner.stop();
              console.log("Creating view for trails...Done");
            });
          }
        }
      });
    });
    create_db("points");
    create_db("images");
    create_db("users");
    create_db("codes");


  }else{
    console.error("CouchDB does not exist. Please install it.");
  }
});
