/**
 * Created by Prateek on 5/19/2017.
 */
jobportal.controller("viewApplicantsPageController", function($uibModal,$scope,$http,$state,$stateParams,loginService,toastr) {
    $scope.opening = $stateParams.openingObj;
    $scope.showApplicants = false;
    $scope.cancelPosting = function(openingId){
        // loginService.httpget().then().catch();

    };

    $scope.location="";

    $scope.applicantButtonText = "View Applicants";
    $scope.viewApplicantsBtn = false;
    $scope.viewApplicantsHide = false;

    $scope.applicants = $scope.opening != null ? $scope.opening.applicantsInfo : null;

    $scope.showApplicants = function () {
        if($scope.viewApplicantsBtn === false){
            $scope.viewApplicantsBtn = true;
            $scope.applicantButtonText = "Hide Applicants";
        }

        else{
            $scope.viewApplicantsBtn = false;
            $scope.applicantButtonText = "View Applicants";
        }
    };

    $scope.modalShown = false;
    $scope.openApplicantProfile = function(applicantObj){
        $scope.modalShown = !$scope.modalShown;
        // Traverse Applicants array of objects and assign the match with below object
        /*$scope.applicant = {
            firstName: "Amit",
            lastName: "Mathur",
            introduction: "Full Stack Dev",
            experience: "2 years experience as SDE",
            education: "MS SE SJSU, BTech IT",
            skills: "Java, Spring, UI",
            status: "Pending"
        }*/

        $scope.applicant = applicantObj.applicantInfo;
        $scope.applicant.status = applicantObj.applicationStatuscustom;
        $scope.applicant.resume = applicantObj.applicantResume;
        $scope.applicant.applicationId = applicantObj.applicationId;

        /*loginService.httpget("/company/"+applicantId,function (result) {
            if(result.statusCode=200){
                $scope.applicant = result;
            }
            else{
                toastr.error("Error in Getting Applicant !");
            }
        });*/

    };

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

    $scope.giveOffer = function(applicationId){
        loginService.register('/company/acceptApplicant',{'applicationId': applicationId},function(res){
            if(res.statusCode == "200"){
                toastr.success(res.msg);
            }
            else{
                toastr.error(res.msg);
            }
        });
    };

    $scope.rejectApplicant = function(applicationId){
        loginService.register('/company/rejectApplicant',{
            'applicationId': applicationId
        },function(res){
            if(res.statusCode == "200"){
                toastr.success(res.msg);
            }
            else{
                toastr.error(res.msg);
            }
        });
    };


    $scope.scheduleInterview = function () {

        $scope.applicantinfo = true;
        $scope.interviewInfor = true;
        $scope.hideScheduleButton = true;

    }

    $scope.schedule = function(location,interviewDate,startTime,endTime,application_id){

        console.log("Location:"+location);
        console.log("date:"+interviewDate);
        console.log("Start Time:"+startTime);
        console.log("End Time:"+endTime);
        console.log("Application id;"+application_id);

        var d12 = new Date(interviewDate);
        //
        // console.log("Year:"+d12.getFullYear()) ;
        // console.log("Month:"+(d12.getMonth()*1+1)) ;
        // console.log("Day:"+d12.getDate()) ;

        var time_startTime = new Date(startTime);
        // console.log("startime hh:"+time_startTime.getHours());
        // console.log("startime mm:"+time_startTime.getMinutes());
        // console.log("startime sec:"+time_startTime.getSeconds());
        // console.log("startime msec:"+time_startTime.getMilliseconds());

        var time_endTime = new Date(endTime);
        // console.log("endtime hh:"+time_endTime.getHours());
        // console.log("endtime mm:"+time_endTime.getMinutes());
        // console.log("endtime sec:"+time_endTime.getSeconds());
        // console.log("endtime msec:"+time_endTime.getMilliseconds());
        //

        var newStartTime = new Date(d12.getFullYear(), (d12.getMonth()),d12.getDate(), time_startTime.getHours(), time_startTime.getMinutes(), time_startTime.getSeconds(), time_startTime.getMilliseconds());
        console.log("New Start time:"+newStartTime);
        var newEndTime = new Date(d12.getFullYear(), (d12.getMonth()),d12.getDate(), time_endTime.getHours(), time_endTime.getMinutes(), time_endTime.getSeconds(), time_endTime.getMilliseconds());
        console.log("New End  time:"+newEndTime.toISOString());

        var data = {
            "location":location,
            "startTime":   newStartTime,
            "endTime":newEndTime,
            "application_id":application_id
        }

        $http({
            url: "/company/interview",
            method: "POST",
            data: data
        }).success(function(data, status){
            if(data.statusCode == "200"){
                toastr.success(data.message);
            }
            else{
                toastr.error(data.message);
            }
        });



    }

});

jobportal.directive('modalDialog', function() {
    return {
        restrict: 'E',
        scope: {
            show: '='
        },
        replace: true, // Replace with the template below
        transclude: true, // we want to insert custom content inside the directive
        link: function(scope, element, attrs) {
            scope.dialogStyle = {};
            if (attrs.width)
                scope.dialogStyle.width = attrs.width;
            if (attrs.height)
                scope.dialogStyle.height = attrs.height;
            scope.hideModal = function() {
                scope.show = false;
            };
        },
        template: "<div class='ng-modal' ng-show='show'>" +
                        "<div class='ng-modal-overlay' ng-click='hideModal()'></div>" +
                        "<div class='ng-modal-dialog' ng-style='dialogStyle'>" +
                            "<button class='btn ng-modal-close' ng-click='hideModal()'>X</button>" +
                            "<div class='ng-modal-dialog-content' ng-transclude></div>" +
                        "</div>" +
                   "</div>"
    };
});