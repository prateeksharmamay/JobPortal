/**
 * Created by Prateek on 5/12/2017.
 */
jobportal.controller("interestedJobPageController", function($scope,$http,$state,loginService,data,toastr) {
    $scope.openingsInitial = JSON.parse(JSON.stringify(data.data));

    $scope.openingsListInitial = $scope.openingsInitial.openings;
    $scope.logoListInitial = $scope.openingsInitial.imageURLs;

    // console.log($scope.openings);
    $http({
        url: "/applicant/profile",
        method: "GET",
        /*data: {
         "opening_id": openingId,
         "add": "true"
         }*/
    }).success(function(res) {
        if(res.statusCode == 200){
            $scope.interestedOpenings = res.profile.interestedOpenings;
            filterOpeningsList();
        }
        else{
            toastr.error("Error while adding Job to your Interested Jobs List");
        }
    });

    var filterOpeningsList = function () {
        var interestedJobsSet = new Set();
        for(var i = 0 ; i < $scope.interestedOpenings.length ; i ++){
            interestedJobsSet.add($scope.interestedOpenings[i].opening_id);
        }

        $scope.openingsList = [];
        $scope.logoList = [];
        for(var i = 0 ; i < $scope.openingsListInitial.length ; i ++){
            if(interestedJobsSet.has($scope.openingsListInitial[i].opening_id)){
                $scope.openingsList.push($scope.openingsListInitial[i]);
                $scope.logoList.push($scope.logoListInitial[i]);
            }
        }
    };

    $scope.validateInterest = function (opening_id) {
        var userInterestedOpening = "false";

        if($scope.interestedOpenings == undefined){
            return userInterestedOpening;
        }

        for(var i = 0 ; i < $scope.interestedOpenings.length ; i ++){
            if(opening_id == $scope.interestedOpenings[i].opening_id){
                userInterestedOpening = "true";
                break;
            }
        }
        return userInterestedOpening;
    };

    /*loginService.httpget("/applicant/activeSession",function (result) {
        if(result.statusCode==200){
            console.log("Session Active");
            $state.go("applicantDashboard");

        }else{
            console.log("No Session");
            $state.go("/");
        }

    });*/

    $scope.applyForJob = function (jobObj) {
        //console.log("Id of opening "+jobObj);
        $state.go("jobApplicationPage", {"jobObj":jobObj});
    };

    $scope.addToInterestedJobs = function(openingId){
        $http({
            url: "/applicant/interested",
            method: "POST",
            data: {
                "opening_id": openingId,
                "add": "true"
            }
        }).success(function(res) {
            if(res.statusCode == 200){
                toastr.success("Added To your Interested Jobs List");
                $state.reload();
            }
            else{
                toastr.error("Error while adding Job to your Interested Jobs List");
            }
        });
    };

    $scope.deleteFromInterestedJobs = function(openingId){
        $http({
            url: "/applicant/interested",
            method: "POST",
            data: {
                "opening_id": openingId,
                "add": "false"
            }
        }).success(function(res) {
            if(res.statusCode == 200){
                toastr.success("Removed from your Interested Jobs List");
                $state.reload();
            }
            else{
                toastr.error("Error while adding Job to your Interested Jobs List");
            }
        });
    };
});
