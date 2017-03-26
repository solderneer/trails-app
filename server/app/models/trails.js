var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var TrailSchema = new Schema({
    name: String,
    route: Array,
    distance: Number,
    time: Number,
    likes: Number,
    markers: Number,
    picture: String,
    description: String,
    latitude: Number,
    longitude: Number
});

module.exports = mongoose.model('Trails', TrailSchema);

