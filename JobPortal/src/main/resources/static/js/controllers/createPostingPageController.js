/**
 * Created by Prateek on 5/14/2017.
 */


jobportal.controller("createPostingPageController", function($scope,$http,$state,loginService,toastr) {

    $scope.submitOpening = function (title,description,responsibilities,location,minSalary,maxSalary) {

        $scope.posting = {
            "title":title,
            "description":description,
            "responsibilities": responsibilities,
            "location": location,
            "minSalary": minSalary,
            "maxSalary":maxSalary
        };

        if( $scope.posting.title!="" && $scope.posting.title!=undefined && $scope.minSalary!="" && $scope.maxSalary!="" && $scope.minSalary!=undefined && $scope.maxSalary!=undefined  ){


            if( isNaN($scope.minSalary) || isNaN($scope.maxSalary)){
                toastr.error("Please enter numeric value for Salary");
            }
            else if($scope.minSalary > $scope.maxSalary){
                toastr.error("Minimum Salary should be less than Maximum Salary");
            }
            else{
                loginService.login('/company/postOpening',$scope.posting, function(res){
                    if(res.statusCode == "200"){
                        toastr.success("Job is successfully Posted !");
                    }
                    else{
                        toastr.error(res.message);
                    }
                });
            }
        }else{
            toastr.error("Title or Salary  is missing");
        }
    }
});