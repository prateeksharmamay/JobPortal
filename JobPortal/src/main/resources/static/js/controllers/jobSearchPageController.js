/**
 * Created by Prateek on 5/12/2017.
 */
jobportal.controller("jobSearchPageController", function($scope,$http,$state,$stateParams,$location,loginService,toastr) {

    console.log("Reached jobSearchPageController controller");
    $scope.openingsList = [];
    $scope.logoList = [];
    $scope.searchJobText = $stateParams.searchText;

    if($stateParams.openings != null){
        $scope.openingsList = $stateParams.openings.openings;
        $scope.logoList = $stateParams.openings.imageURLs;
    }

    $scope.applyForJob = function (jobObj) {
        //console.log("Id of opening "+jobObj);
        $state.go("jobApplicationPage", {"jobObj":jobObj});
    }


    $scope.filteredOpenings = $scope.openingsList;
    $scope.page_number = 1;
    $scope.total_count = 0;
    $scope.itemsPerPage = 5;

        $scope.filterPageNumber = function (page_number) {
            page_number=page_number-1;
            $http({
                url: "/applicant/searchWithFilter?query="+$scope.searchJobText+"&keywords="+$scope.textSearch+"&companies="+ $scope.companySearchText+"&locations="+$scope.locationSearchText+"&minSalary="+$scope.minSalary+"&maxSalary="+ $scope.maxSalary+"&page="+page_number+"&size="+1000,
                method: "GET",
            }).success(function(openings){
                if(openings.statusCode==200){
                    if(openings.openings.length>0) {
                        console.log("next button true");
                        // $scope.nextButton = true;
                        $scope.filteredOpenings = openings.openings;
                        $scope.total_count = openings.openings.length;
                    }else{
                       $scope.filteredOpenings=[];
                        toastr.error("No Results found");
                    }
                    // console.log("Session Active");
                    //  $state.go("jobSearchPage", {"openings":openings,"searchText":$scope.searchJobText});
                }else if(openings.statusCode==405){
                    toastr.error("No Results found");
                }
            });
        }


    $scope.filterResults = function () {
        $http({
            url: "/applicant/searchWithFilter?query="+$scope.searchJobText+"&keywords="+$scope.textSearch+"&companies="+ $scope.companySearchText+"&locations="+$scope.locationSearchText+"&minSalary="+$scope.minSalary+"&maxSalary="+ $scope.maxSalary+"&page="+0+"&size="+1000,
            method: "GET",
        }).success(function(openings){
            if(openings.statusCode==200){
                if(openings.openings.length>0) {
                    console.log("next button true");
                    // $scope.nextButton = true;
                    $scope.filteredOpenings = openings.openings;
                    $scope.total_count = openings.openings.length;
                }else{
                    $scope.filteredOpenings=[];
                    toastr.error("No Results found");
                }
                // console.log("Session Active");
                //  $state.go("jobSearchPage", {"openings":openings,"searchText":$scope.searchJobText});
            }else if(openings.statusCode==405){
                toastr.error("No Results found");
            }
        });
    }



});