/**
 * Created by Prateek on 5/12/2017.
 */

jobportal.controller("myApplicationsPageController", function($scope,$http,$state,loginService,toastr) {

    console.log("Reached myApplicationsPage  1 Controller controller");

    loginService.httpget("/applicant/getApplications", function(result){
        if(result){
            console.log("Fetched Profile Infomration " + result);

            $scope.applications= result.applications;

            $scope.applications.length = $scope.applications == undefined? 0 : $scope.applications.length;
            var zeroApps = true;
            for(var i = 0 ; i < $scope.applications.length ; i++){
                if($scope.applications[i].applicationStatus == "Offered"){
                    zeroApps = false;
                    break;
                }
            }

            if(zeroApps){
                $scope.zeroApp = true;
            }else{
                $scope.zeroApp = false;
            }

        }else{
            console.log("Cannot fetch the details");
            $scope.message = result.message;
        }

    });

    //custom filter to display on completed apps page
    $scope.terminalStatus = function (application) {
        return application.applicationStatus === 'OfferRejected' || application.applicationStatus === 'OfferAccepted'|| application.applicationStatus === 'Rejected'|| application.applicationStatus === 'Cancelled';
    }

    $scope.withdraw = function(){

        $scope.selectedApps = [];
        angular.forEach($scope.applications, function(application){

            if (!!application.selected){
                $scope.selectedApps.push(application.application_id);
                console.log("Selected App:"+application.application_id);
            }

        })
        console.log("seelcted apps size:"+$scope.selectedApps.length);

       if($scope.selectedApps.length>0){
           $http({

               url: "/applicant/cancelApplications",
               method: "POST",
               data: $scope.selectedApps
           }).success(function(data){
               if(data.statusCode==200){
                   toastr.success("You have successfully cancelled the selected Applications");
                   $state.reload();
               }else
                   toastr.error("One or more applications cannot be cancelled");


           });
       }else{
           toastr.error("Please select atleast one application");
       }

    }

    $scope.acceptJob = function () {

        $scope.selectedApps = [];
        angular.forEach($scope.applications, function(application){

            if (!!application.selected){
                $scope.selectedApps.push(application.application_id);
                console.log("Selected App:"+application.application_id);
            }
        })
        console.log("seelcted apps size:"+$scope.selectedApps.length);


        if($scope.selectedApps.length>0){
            $http({

                url: "/applicant/acceptOffer",
                method: "POST",
                data: $scope.selectedApps
            }).success(function(data){
                if(data.statusCode==200){
                    toastr.success("You have successfully accepted the selected offers");
                    $state.reload();
                }else
                    toastr.error("One or more offers cannot be accpeted");
            });
        }else{
            toastr.error("Please select atleast one application");
        }

    }



    $scope.rejectJob = function () {

        $scope.selectedApps = [];
        angular.forEach($scope.applications, function(application){

            if (!!application.selected){
                $scope.selectedApps.push(application.application_id);
                console.log("Selected App:"+application.application_id);
            }
        })
        console.log("seelcted apps size:"+$scope.selectedApps.length);


        if($scope.selectedApps.length>0){
            $http({

                url: "/applicant/rejectApplications",
                method: "POST",
                data: $scope.selectedApps
            }).success(function(data){
                if(data.statusCode==200){
                    toastr.success("You have successfully rejected the selected offers");
                    $state.reload();
                }else
                    toastr.error("One or more offers cannot be rejected");
            });
        }else{
            toastr.error("Please select atleast one application");
        }

    }

});
