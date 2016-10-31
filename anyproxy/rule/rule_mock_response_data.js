var mysql      = require('mysql');
var request    = require('sync-request');
var _host      = 'localhost';
var _port      = '3306';
var _user      = 'root';
var _password  = 'BeeCloud2016@';
var _database  = 'mock';
var response_id = "";
module.exports = {
	summary:function(){
        return "replace response data by local response";
    },
	
    shouldUseLocalResponse : function(req,reqBody){
		var res = request('GET', 'http://127.0.0.1:8888/beecloud/rule/get/enable', {
			'headers': {
				'accept':'text/html,application/json,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
			}
		});
		if(res==null||res==""){
			return false;
		}
		var rules = JSON.parse(res.getBody('utf8'));
		var len = rules.length;
		var url = req.url;
		console.log(url);
		console.log(rules[0]);
		for(var i=0;i<len;i++){
			var pattern = rules[i].path.toString();
			if(url.match(pattern)){
				console.log("match");
				response_id = rules[i].response_id;
				return true;
			}
		}
		return false;
	},
	
    dealLocalResponse : function(req,reqBody,callback){
		var selectSQL = 'SELECT * FROM response WHERE id='+"'"+response_id+"'";
		console.log(selectSQL);
		var connection = mysql.createConnection({
		host     : _host,
		port     : _port,
		user     : _user,
		password : _password,
		database : _database
        });
        connection.connect();
		var query = connection.query(selectSQL,function (err, res) {
                if (err) console.log(err);
				if(res!=null&&""!=res){
					var response = res[0];
					console.log(response);
					callback(response.statuscode,null,response.response);
				}
                
            });
		}
	
};
