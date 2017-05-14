var config = require("./config");

var express = require("express");
var nano = require("nano");

var app = express();
var api = require("./api")(nano);

app.use("/api", api);
app.use("/uploads", express.static(__dirname + config.uploads));

app.listen(config.port, function(err){
  err ? console.log(err) : console.log("Listening at " + config.port);
});
