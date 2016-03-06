// # 1
// console.log("HELLO WORLD")

// #2 - sum of arguments
/*
var sum = 0;
for (var i = 2; i < process.argv.length; i++) {
  sum += Number(process.argv[i]);
}

console.log(sum);
*/

// #3 - count lines in file
/*
var fs = require('fs')

var buff = fs.readFileSync(process.argv[2])
var content = buff.toString()
var linesCount = content.split('\n').length

console.log(linesCount - 1)
*/

// #4 - async count of lines
/*
var fs = require('fs')

fs.readFile(process.argv[2], (err, data) => {
  if (!err)
    console.log(data.toString().split('\n').length - 1);
})
*/

// #5 - list of files in given directory
/*
var fs = require('fs')
var path = require('path');

fs.readdir(process.argv[2], (err, list) => {
  if (!err) {
    list.forEach(filePath => {
      if (path.extname(filePath) == "." + process.argv[3]) {
        console.log(filePath)
      }
    })
  }
})
*/

// #6 - list of files in giver directory - with modules
/*
var myModule = require('./module.js')

myModule.listDirAndFilter(process.argv[2], process.argv[3], (err, file) => {
  if (!err)
    console.log(file);
})
*/

// #7 - do HTTP get request to provided url
/*
var http = require('http')

http.get(process.argv[2], response => {
  response.setEncoding("utf8")
  response.on("data", data => {
    console.log(data)
  })
})
*/

// #8 - do get HTTP request and collect all data from provided url
/*
var http = require('http')
var bl = require('bl')

http.get(process.argv[2], response => {
  response.setEncoding("utf8")
  response.pipe(bl((err, data) => {
    console.log(data.toString().length)
    console.log(data.toString())
  }))
})
*/

// #9 - collect from multiple URLs in order
/*
var http = require('http')
var bl = require('bl')

var urls = [process.argv[2], process.argv[3], process.argv[4]]
var res = []

http.get(urls[0], response => {
  response.setEncoding("utf8")
  response.pipe(bl((err, data) => {
    res[0] = data.toString();
    done()
  }))
})

http.get(urls[1], response => {
  response.setEncoding("utf8")
  response.pipe(bl((err, data) => {
    res[1] = data.toString();
    done()
  }))
})

http.get(urls[2], response => {
  response.setEncoding("utf8")
  response.pipe(bl((err, data) => {
    res[2] = data.toString();
    done()
  }))
})

function done() {
  if (res.length == urls.length) {
    res.forEach(str => console.log(str))
  }
}
*/

// #10 - TCP server returning time
/*
var time = require('strftime')
var net = require('net')
var server = net.createServer(socket => {
  socket.end(time("%Y-%m-%d %H:%M") + "\n")
})
server.listen(process.argv[2])
*/

// #11 - HTTP server returning file to each request
/*
var http = require('http')
var fs = require('fs')

var server = http.createServer((request, response) => {
  response.writeHead(200, { 'content-type' : 'text/plain' })
  fs.createReadStream(process.argv[3]).pipe(response)
})
server.listen(process.argv[2])
*/

// #12 - convert POST request string data to UPPERCASE
/*
var http = require('http')
var map = require('through2-map')
var server = http.createServer((request, response) => {
  if (request.method != 'POST')
    return res.end('send me POST\n');

  response.writeHead(200, { 'content-type' : 'text/plain' })
  request.pipe(map(chunk => {
    return chunk.toString().toUpperCase()
  })).pipe(response)
})
server.listen(process.argv[2])
*/

// #13 - multiple endpoints -> destructurize date
var http = require('http')
var url = require('url')
var server = http.createServer((request, response) => {
  response.writeHead(200, { 'Content-Type': 'application/json' })
  var parsedURL = url.parse(request.url, true);
  var result = { error: 'Invalid request'};
  if (parsedURL.pathname == '/api/parsetime') {
    result = parseTime(parsedURL.query)
  } else if (parsedURL.pathname == '/api/unixtime') {
    result = unixTime(parsedURL.query)
  }
  response.end(JSON.stringify(result))
})
server.listen(process.argv[2])

function parseTime(time) {
  var result = { error: 'invalid parsetime request'}
  if (time.iso) {
    var date = new Date(time.iso)
    result = {
      "hour" : date.getHours(),
      "minute" : date.getMinutes(),
      "second" : date.getSeconds()
    }
  }

  return result;
}

function unixTime(time) {
  var result = { error: 'invalid unixtime request'}
  if (time.iso) {
    var date = new Date(time.iso)
    result = { "unixtime" : date.getTime() }
  }

  return result
}
