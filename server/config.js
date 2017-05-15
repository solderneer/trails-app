module.exports = {
  port : 10201,
  db_location : "http://localhost:5984",
  secret : 'secret',
  expiry : 60 * 60 * 24, //Seconds
  uploads : "/uploads",
  host : 'locahost',
  smtp : {
    host: '',
    port: undefined,
    secure: false, // upgrade later with STARTTLS
    auth: {
        user: '',
        pass: ''
    }
  }
};
