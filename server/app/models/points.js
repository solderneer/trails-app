var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var pointSchema = new Schema({
    name: String,
    picture: String,
    description: String,
    image1: String,
    image2: String,
    image3: String,
    image4: String,
    caption1: String,
    caption2: String,
    caption3: String,
    caption4: String
})

module.exports = mongoose.model('Points', pointSchema);