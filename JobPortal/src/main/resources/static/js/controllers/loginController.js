/**
 * Created by kunal on 5/12/17.
 */
jobportal.controller("loginController", ['$scope', '$http', '$state', 'loginService', 'toastr', function($scope,$http,$state,loginService,toastr) {

console.log("Reached login controller");

//console.log("User Type: "+$scope.userType);

	loginService.httpget("/applicant/activeSession",function (result) {
		if(result.statusCode==200){
			console.log("Session Active");
			$state.go("applicantDashboard");

		}else{
			console.log("No Session");
            $state.go("/");
		}

    })





    $scope.login = function(inputUsername, inputPassword)
    {
        console.log("Username: "+inputUsername);
        console.log("Password: "+inputPassword);
        var data = {
    			"email": inputUsername,
				"password": inputPassword
    	};
		if(inputUsername!="" && inputUsername != undefined && inputPassword != "" && inputPassword != undefined){
            loginService.login("/applicant/signin", data, function(result){
                if(result.statusCode == "200"){
                    console.log("Inside successful registration");
                    $state.go("applicantDashboard");
                }else{
                    //$scope.message = result.message;
                    toastr.error(result.message);
            }
            });
		}
        else{
			toastr.error("Please provide both username and password");
		}
    }
    
    $scope.register = function(){
    	var data = {
    			"firstName": $scope.firstName,
				"lastName": $scope.lastName,
				"password": $scope.password,
				"email": $scope.email
    	};
    	if($scope.firstName != "" && $scope.firstName != undefined && $scope.lastName != "" && $scope.lastName != undefined && $scope.email != "" && $scope.email != undefined && $scope.password != "" && $scope.password != undefined){
            loginService.register("/applicant/register", data, function(result){
                if(result.statusCode == "200"){
                    console.log("Inside successful registration " + result);
                    //$scope.message = "We have sent you an email. Please verify your account before login!";
                    toastr.success("We have sent you an email. Please verify your account before login!");
                }else{
                    //$scope.message = result.message;
                    toastr.error(result.message);
                }
            });
		}else{
            toastr.error("Please provide all the details");
		}

    };


///if company then go to company controller and transition to company state and else wise









}]);

