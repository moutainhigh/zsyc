!(function(win){
	'use strict';

	var module = {
		image: {
			path: ''
		},
		file: {
			path: '/file'
		}
	};

	var upload = function(file, m){
		var reader = new FileReader;
		var p = new Promise(function(ok,error){
			reader.addEventListener('load',function(){
				var code = sha1(reader.result);
				fetch( win.uploadHost + m.path + '/exist?code='+code.toUpperCase())
					.then( function(res){ return  res.json(); })
					.then( function(json) {
						if(json.bizContent && json.bizContent.id ){
							return ok(json);
						}
						var data = new FormData();
						data.append('file',file);
						return fetch( win.uploadHost + m.path + '/upload',{
							method:'POST',
							body:data
						}).then(function( res ){
							return res.json()
						}).then(function(json){
							return ok(json);
						});
					});
			});
		});
		reader.readAsArrayBuffer(file);
		return p;
	};

	win.fileUpload = function(file, m){
		return upload(file, module[m || 'image']).then( function(data){
			data.origin = win.uploadHost +   '/origin/' + data.fingerprint;
			data.small  = win.uploadHost + '/small/' + data.fingerprint;
			data.middle = win.uploadHost + '/middle/' + data.fingerprint;
			return data;
		});
	};
})(window);