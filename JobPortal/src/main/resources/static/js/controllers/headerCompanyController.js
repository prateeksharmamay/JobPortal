jobportal.controller("headerComapnyController", function($scope,$http,$state,loginService) {

    console.log("Reached headerCompanyController controller");

    loginService.isLoggedIn(function(result){
        console.log("Session in profile"+result.statusCode);
        console.log("Email in profile"+result.email);
        $scope.email=result.email;
    });

    $scope.logout = function(){

        console.log("Loggin out");
        loginService.logoutCompany(function(result){
            if(result.statusCode == "200"){
                console.log("Logout successfull");
                $state.go("/");
            }
            else{
                console.log("Cannot logout");
                $scope.message = result.message;
            }


        });
    }



    $scope.searchJob = function () {
        $state.go("jobSearchPage", {jobText:$scope.searchJobText});
    }
});