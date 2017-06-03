/**
 * Created by kunal on 5/12/17.
 */
jobportal.controller("headerApplicantController", function($scope,$http,$state,loginService,toastr) {

    console.log("Reached headerApplicantController controller");


    loginService.isLoggedIn(function(result){
        console.log("Session in profile"+result.statusCode);
        console.log("Email in profile"+result.email);
        $scope.email=result.email;
    });




    $scope.logout = function(){

        console.log("Loggin out");
        loginService.logout(function(result){
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

        $scope.searchJob = function () {

          //  loginService.httpget("/applicant/search?query="+ $scope.searchJobText +"&page=" +0+"&size="+10,function (openings) {
            //loginService.httpget("/applicant/search?query="+ $scope.searchJobText,function (openings) {
            loginService.httpget("/applicant/searchWithFilter?query="+$scope.searchJobText+"&keywords=undefined&companies=undefined&locations=undefined&minSalary=0&maxSalary=10000000&page=0&size=1000",function (openings) {
                if(openings.statusCode==200){
                    // console.log("Session Active");
                    $state.go("jobSearchPage", {"openings":openings,"searchText":$scope.searchJobText});

                }else{
                    toastr.error("Error while searching");
                }

            });
        }
    }
});