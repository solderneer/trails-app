var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var TrailSchema = new Schema({
    _id: Number,
    name: String,
    longitude: Number,
    latitude: Number,
    distance: Number,
    time: Number,
    Likes: Number,
    markers: Number,
    picture: String
});

module.exports = mongoose.model('Trails', TrailSchema);