/**
 * Created by kunal on 5/12/17.
 */
jobportal.controller("companyDashboardController", function(data,$scope,$http,$state,loginService,toastr) {

    console.log("Reached companyDashboardController controller");
    $scope.selectedStatus = "";

    $scope.filterJobs = function(openingStatus){
       $scope.filterStatus = openingStatus;
    };

    loginService.httpget("/company/activeSession",function (result) {
        if(result.statusCode==200){
            console.log("Session Active");
            $state.go("companyDashboard");

        }else{
            console.log("No Session");
            $state.go("loginCompany");
        }

    })



    $scope.postings = data.data.modelMapList;
    
    console.log($scope.postings);
    
    $scope.editPosting = function (openingId) {

        $http({
            url: "/company/opening?opening_id="+openingId,
            method: "GET",
        }).success(function(data, status) {
            // console.log("generic get service data " + data + " \n status code " + status);
            var opening = data;
            opening.id = openingId;
            $state.go("editPostingPage", {"openingObj":opening});
        });
    };

    $scope.viewApplicants = function(openingId){
        $http({
            url: "/company/opening?opening_id="+openingId,
            method: "GET",
        }).success(function(data, status) {
            // console.log("generic get service data " + data + " \n status code " + status);
            var opening = data;
            opening.id = openingId;
            $state.go('viewApplicantsPage', {"openingObj":opening});
        });
    }

    $scope.showInterviews = function(openingId){
        $http({
            url: "/company/interview?opening_id="+openingId,
            method: "GET",
        }).success(function(data, status) {
            // console.log("generic get service data " + data + " \n status code " + status);
            var opening = data;
            opening.id = openingId;
            $state.go('showInterviews', {"openingObj":opening});
        });
    }


    $scope.cancelPosting = function(openingId){
        loginService.register('/company/cancelOpening',{'openingId': openingId},function(res){
            if(res.statusCode == "200"){
                toastr.success(res.message);
                $state.reload();
            }
            else{
                toastr.error(res.message);
            }
        });
    };

});


