/**
 * Created by Prateek on 5/12/2017.
 */
jobportal.controller("profilePageController", function($scope,$http,$state,Upload,loginService,toastr) {

    console.log("Reached profilePageController controller");

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
        $scope.email=result.email;
    });

    //Getting Profile details
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




    $scope.updateProfile = function () {
        console.log("Updating profile");

        var data = {
            "firstName":   $scope.userprofile.firstName,
            "lastName":  $scope.userprofile.lastName,
            "introduction":  $scope.userprofile.introduction,
            "experience":  $scope.userprofile.experience,
            "education":  $scope.userprofile.education,
            "skills":  $scope.userprofile.skills
        };

        if($scope.userprofile.firstName != "" && $scope.userprofile.firstName != undefined && $scope.userprofile.lastName != "" && $scope.userprofile.lastName != undefined){
            loginService.login("/applicant/update",data,function (result) {
                if(result.statusCode=200){
                    alert("Profile Updated");
                    $state.reload();
                }
                else{
                    alert("Profile can't Updated")
                }
            })
        }else{
            toastr.error("You cannot leave FirstName or LastName as blank!")
        }

    }



    $scope.uploadImage = function(file){

        console.log("uploading pic");
        Upload.upload({
            url: '/api/aws/s3/upload', //webAPI exposed to upload the file
            data:{file:file } //pass file as data, should be user ng-model
        }).then(function (resp) { //upload function returns a promise
            console.log("Response of the server  after upload",resp);
            if(resp.status == 200){ //validate success
                alert("Image uploaded");
                var imageUrlS3="https://s3-us-west-2.amazonaws.com/jp275/"+file.name;
                console.log("iMage Url:"+imageUrlS3);
                var imageUrl = {
                    "imageUrl": imageUrlS3
                };

                loginService.login("/applicant/update",imageUrl,function (result) {
                    if(result.statusCode=200){
                        alert("Image Updated");
                        $state.reload();
                    }
                    else{
                        alert("Image can't Updated")
                    }
                })
                $state.reload();

            } else {
                alert('an error occured');
            }
        }, function (resp) { //catch error
            console.log('Error status: ' + resp.status);
            alert('Error status: ' + resp.status);
        }, function (evt) {
            console.log(evt);
            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
            $scope.progress = 'progress: ' + progressPercentage + '% '; // capture upload progress
        });

    }







});