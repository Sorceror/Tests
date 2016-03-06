module.exports = {
  listDirAndFilter : function (dirName, extFilter, callback) {
    var fs = require('fs')
    var path = require('path');

    fs.readdir(dirName, (err, list) => {
      if (!err) {
        list.forEach(filePath => {
          if (path.extname(filePath) == "." + extFilter) {
            callback(null, filePath)
          }
        })
      } else {
        callback(null)
      }
    })
  }
}
