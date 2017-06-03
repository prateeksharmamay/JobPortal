/**
 * Created by Prateek on 5/13/2017.
 */

jobportal.controller("jobApplicationPageController", function($scope,$http,$state, $stateParams,loginService,Upload, toastr) {

    console.log("Reached jobApplicationPageController controller");

    // $scope.applyprofilebutton = false;
    // $scope.applyresumebutton = false;
    // $scope.alreadyApplied = false;

    $scope.changeStatus = true;

    $scope.userprofile = {
        "firstName":"",
        "lastName": "",
        "introduction": "",
        "experience":"",
        "education" : "",
        "skills": "",
    };

    loginService.isLoggedIn(function(result){
        console.log("Session in profile"+result.statusCode);
        console.log("Email in profile"+result.email);
        $scope.email=result.email;

    });


    $scope.error = true;
    console.log("Param passed is "+$stateParams.jobObj);


    $http({
        url: "/company/opening?opening_id="+$stateParams.jobObj,
        method: "GET"
    }).success(function(data, status){
        console.log("generic get service data "+data+" \n status code "+status);
        $scope.opening = data;
        if(data.applicationscustom.hasOwnProperty($scope.email)) {

            if(data.applicationscustom[$scope.email]!=="Cancelled"){
                $scope.applyprofilebutton = true;
                $scope.applyresumebutton=true;
                $scope.alreadyApplied=true;
            }
        }

    });

    loginService.httpget("/applicant/profile", function(result){
        if(result){
            console.log("Fetched Profile Infomration " + result);
            $scope.userprofile.firstName = result.profile.firstName;
            $scope.userprofile.lastName = result.profile.lastName;
            $scope.userprofile.introduction =result.profile.introduction;
            $scope.userprofile.experience=result.profile.experience;
            $scope.userprofile.education=result.profile.education;
            $scope.userprofile.skills=result.profile.skills;
            console.log("Image in fetch profile:"+result.profile.imageUrl);
            $scope.userprofile.imageUrl = result.profile.imageUrl;
        }else{
            console.log("Cannot fetch the details");
            $scope.message = result.message;
        }
    });

    console.log("Values from the profile "+$scope.userprofile);

    /*if($scope.userprofile.experience != "" && $scope.userprofile.experience !=undefined && $scope.userprofile.education!= "" && $scope.userprofile.education != undefined && $scope.userprofile.skills != "" && $scope.userprofile.skills != undefined){

    }else{
        console.log("Inside not able to apply wih profile");
        toastr.error("Please update your profile before applying");
    }*/

    $scope.applyWithProfile = function () {
        if($scope.userprofile.experience != "" && $scope.userprofile.experience !=undefined && $scope.userprofile.education!= "" && $scope.userprofile.education != undefined && $scope.userprofile.skills != "" && $scope.userprofile.skills != undefined){
            console.log("Applying to job id:"+$stateParams.jobObj);
            loginService.login("/applicant/apply",$stateParams.jobObj,function (data) {
                console.log("APplyw ith profile:"+data);
                if(data.statusCode==200){
                    console.log("Applied")
                    $state.reload();

                }else if(data.statusCode==402){
                    alert("You cannot have more than 5 Pending Applictions");
                    console.log("Cannot Apply");
                }else{
                    alert("Cannot Apply");
                }
            })
        }else{
            console.log("Inside not able to apply wih profile");
            toastr.error("Please update your profile before applying");
        }

    }


    $scope.applywith = function () {

        $scope.showResumeTemplate = true;

    }




    $scope.uploadResume  = function (resumeObj) {

        if(resumeObj !== null && resumeObj !== undefined && resumeObj !== "") {

            console.log("Uploading Resume to S3");
            Upload.upload({url: '/api/aws/s3/upload', data:{file:resumeObj}})
                .then(function (resp) { //upload function returns a promise
                    console.log("Response of the server  after upload",resp);
                    if(resp.status == 200) { //validate success
                        console.log("Resume uploaded");
                        var resumeUrlS3 = "https://s3-us-west-2.amazonaws.com/jp275/" + resumeObj.name;
                        console.log("resumeUrlS3 :" + resumeUrlS3);

                        $scope.resumeUrl = {
                            "resumeUrl": resumeUrlS3,
                            "opening_id":$stateParams.jobObj
                        };
                        alert("Resume uploaded")
                        console.log("Applying with uploaded resume to the job");
                        $scope.changeStatus = false;

                    }else{
                        alert("Cannot upload image");
                        console.log("Image cannot be uploaded to S3");
                    }

                })}else{
            alert("Please upload the file");
        }
    }//end of resumeupload

    $scope.applyWithResume = function(){

        loginService.login("/applicant/applyWithResume",$scope.resumeUrl,function (result) {
            console.log("$scope.resumeUrl: in applywihtresu: "+$scope.resumeUrl);
            if(result.statusCode==200){
                alert("Applied with Resume");
                $state.reload();
                }
            else if(result.statusCode==402){
                alert("You cannot have more than 5 Pending Applications");
            }else{
                alert("Cannot apply");
            }

        })
    }



});