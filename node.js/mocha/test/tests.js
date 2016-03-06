var expect = require('chai').expect;
var request = require('request');

describe("Mocha test suite", function() {
  describe("Time decomposition", function() {
    var url = 'http://localhost:10000/api/parsetime?iso=2013-08-10T12:10:15.474Z';

    it("returns 200", function() {
      request(url, function (err, response, body) {
        expect(response.statusCode).to.equal(200);
        done();
      });
    });

    it("expect correct time decomposition", function() {
      request(url, function(err, response, body) {
        expect(body).to.equal("{\"hour\":20,\"minute\":56,\"second\":44}");
        done();
      });
    });
  });

  describe("Unix timestamp", function() {
    var url = 'http://localhost:10000/api/unixtime?iso=2013-08-10T12:10:15.474Z';

    it("returns 200", function() {
      request(url, function(err, response, body) {
        expect(response.statusCode).to.equal(200);
        done();
      });
    });

    it("expect correct time decomposition", function() {
      request(url, function(err, response, body) {
        expect(body).to.equal("{\"unixtime\":1457294204957}");
        done();
      });
    });
  });
});
