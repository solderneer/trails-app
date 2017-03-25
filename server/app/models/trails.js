var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var TrailSchema = new Schema({
    name: String,
    longitude: Number,
    latitude: Number,
    distance: Number,
    time: Number,
    likes: Number,
    markers: Number,
    picture: String
});

module.exports = mongoose.model('Trails', TrailSchema);

