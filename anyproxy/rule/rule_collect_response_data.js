var mysql      = require('mysql');
var _host      = 'localhost';
var _port      = '3306';
var _user      = 'root';
var _password  = 'BeeCloud2016@';
var _database  = 'mock';

module.exports = {
	summary:function(){
        return "Collect interface information";
    },
	
    replaceServerResDataAsync: function(req,res,serverResData,callback){
        var col;
        var connection = mysql.createConnection({
            host     : _host,
            port     : _port,
            user     : _user,
            password : _password,
            database : _database
        });
        connection.connect();
        try {
            if(null != serverResData ) {
                serverResData = serverResData.toString();
                var tempJson = serverResData;
                tempJson = JSON.parse(tempJson);
                col = null != tempJson ? tempJson.type : "";
            } else {
                col = "";
            }
            var insertSQL = 'insert into response(host,url,col,method,contentType,userAgent,response) values("' + req.headers.host + '","' + req.url + '","' + col + '","' + req.method + '","' + req.headers['content-type'] + '","' + req.headers['user-agent'] + '",' + JSON.stringify(serverResData) + ')';
           	console.log("insertSQL -> " + insertSQL);
            connection.query(insertSQL, function (err, res) {
                if (err) console.log(err);
                console.log("INSERT Return ==> ");
                console.log(res);
            });
        } catch(err) {
            console.log(err);
        } finally {
            callback(serverResData);
            connection.end();
        }
    }
};
