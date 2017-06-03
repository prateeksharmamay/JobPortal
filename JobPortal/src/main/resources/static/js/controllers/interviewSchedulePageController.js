/**
 * Created by Prateek on 5/12/2017.
 */
jobportal.controller("interviewSchedulePageController", function($scope,$http,$state,loginService,data,toastr) {

    console.log("Reached interviewSchedulePageController controller");


    $scope.openingsInitial = JSON.parse(JSON.stringify(data.data));

    $scope.openingsListInitial = $scope.openingsInitial.openings;
    $scope.logoListInitial = $scope.openingsInitial.imageURLs;

    // console.log($scope.openings);
    $http({
        url: "/applicant/interview",
        method: "GET",
        /*data: {
         "opening_id": openingId,
         "add": "true"
         }*/
    }).success(function(res) {
        if(res.statusCode == 200){
            $scope.openingsWithInterviews = res.openings;
            $scope.openingsInterviewDetails = res.scheduledInterviews;
            filterOpeningsList();
        }
        else{
            toastr.error("Error while Getting your Interview Details.");
        }
    });

    var filterOpeningsList = function () {
        var openingWithInterviewSet = new Set();
        for(var i = 0 ; i < $scope.openingsWithInterviews.length ; i ++){
            openingWithInterviewSet.add($scope.openingsWithInterviews[i].opening_id);
        }

        $scope.openingsList = [];
        $scope.logoList = [];
        for(var i = 0 ; i < $scope.openingsListInitial.length ; i ++){
            if(openingWithInterviewSet.has($scope.openingsListInitial[i].opening_id)){
                $scope.openingsList.push($scope.openingsListInitial[i]);
                $scope.logoList.push($scope.logoListInitial[i]);
            }
        }
    };

    $scope.acceptInterviewRequest = function(application_id){
        $http({
            url: "/applicant/interview",
            method: "POST",
            data: {
                "application_id": application_id,
                "accept": "true"
            }
        }).success(function(res) {
            if(res.statusCode == 200){
                toastr.success("Successfully Accepted Interview Invitation");
                $state.reload();
            }
            else{
                toastr.error("Error while scheduling interview");
            }
        });
    };

    $scope.rejectInterviewRequest = function(application_id){
        $http({
            url: "/applicant/interview",
            method: "POST",
            data: {
                "application_id": application_id,
                "accept": "false"
            }
        }).success(function(res) {
            if(res.statusCode == 200){
                toastr.success("Successfully Rejected Interview Invitation");
                $state.reload();
            }
            else{
                toastr.error("Error while scheduling interview");
            }
        });
    };
});