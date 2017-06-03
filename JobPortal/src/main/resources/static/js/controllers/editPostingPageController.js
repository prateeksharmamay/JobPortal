/**
 * Created by Prateek on 5/14/2017.
 */

jobportal.controller("editPostingPageController", function(loginService,$scope,$http,$state,$stateParams,toastr) {
    var postingObj = $stateParams.openingObj;

    $scope.posting = {
        "opening_id": postingObj.id,
        "title":postingObj.title,
        "description":postingObj.description,
        "responsibilities": postingObj.responsibilites,
        "location": postingObj.location,
        "minSalary": postingObj.minSalary,
        "maxSalary":postingObj.maxSalary
    };
    console.log("Reached editPostingPageController controller");
    console.log("SalryL:"+ $scope.maxSalary);
    console.log("SalryL:"+ $scope.minSalary);
    $scope.editPosting = function () {

        console.log("SalryL:"+ $scope.maxSalary);
        console.log("SalryL:"+ $scope.minSalary);

        if( $scope.posting.title!="" && $scope.posting.title!=undefined && $scope.posting.minSalary!="" && $scope.posting.maxSalary!="" && $scope.posting.minSalary!=undefined && $scope.posting.maxSalary!=undefined  ){


            if( isNaN($scope.posting.minSalary) || isNaN($scope.posting.maxSalary)){
                toastr.error("Please enter numeric value for Salary");
            }else{
            loginService.login("/company/updateOpening",$scope.posting,function (result) {
                if(result.statusCode=200){
                    toastr.success("Job Posting Updated !");
                    $state.go("companyDashboard");
                }
                else{
                    toastr.error("Error in Job Posting Update !");
                }
            })}
        }
        else{
            toastr.error("You can not leave Title or Salary as blank!");
        }
    }

})