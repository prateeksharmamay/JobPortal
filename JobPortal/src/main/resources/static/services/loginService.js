jobportal.service('loginService', ['$http', function($http){
		
	this.register = function(url, dataObject, callback){
		$http({
			url: url,
			method: "POST",
			data: JSON.stringify(dataObject)
		}).success(function(data, status){
			var resp_obj = JSON.parse(JSON.stringify(data));
			callback(resp_obj);
		});
	};
	
	this.login = function(url, data, callback){
		$http({
			url: url,
			method: "POST",
			data: JSON.stringify(data)
		}).success(function(data, status){
			var resp_obj = JSON.parse(JSON.stringify(data));
			callback(resp_obj);
		});
	};
	
	this.isLoggedIn = function(callback){
		$http({
			url: "/applicant/isLoggedIn",
			method: "GET"
		}).success(function(data, status){
            console.log("Return from isLoggedIn " + data + " \n status code  "+ status);
			var resp_obj = JSON.parse(JSON.stringify(data));
			console.log("Is logged i ui:"+resp_obj);
			callback(resp_obj);
		});
	}

	//Generic Service for Get

	this.httpget = function(url,callback){
		console.log('url '+url);
        $http({
            url: url,
            method: "GET",
        }).success(function(data, status){
        	console.log("generic get service data "+data+" \n status code "+status);
            var resp_obj = JSON.parse(JSON.stringify(data));
            callback(resp_obj);
        });
    }


    this.logout = function(callback){
        $http({
            url:"/applicant/logout",
            method:"POST"
        }).success(function (response) {
            console.log(response);
            var resp_obj = JSON.parse(JSON.stringify(response));
            callback(resp_obj);
        })
    }

    this.logoutCompany = function(callback){
        $http({
            url:"/company/logout",
            method:"POST"
        }).success(function (response) {
            console.log(response);
            var resp_obj = JSON.parse(JSON.stringify(response));
            callback(resp_obj);
        })
    }

}]);