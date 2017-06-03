/**
 * Created by kunal on 5/25/17.
 */


jobportal.controller("showInterviewsController", function($scope,$http,$state,Upload,loginService,toastr,$stateParams) {


$scope.interviews = $stateParams.openingObj;

if($scope.interviews.email.length<1){

    alert("No Interviews Scheduled for this position");
    $state.go('companyDashboard');

}else{


}








})