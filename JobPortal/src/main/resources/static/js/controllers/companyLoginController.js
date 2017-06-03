/**
 * Created by kunal on 5/14/17.
 */
jobportal.controller("companyLoginController", function($scope,$http,$state,toastr,Upload,loginService) {



    loginService.httpget("/company/activeSession",function (result) {
        if(result.statusCode==200){
            console.log("Session Active");
            $state.go("companyDashboard");

        }else{
            console.log("No Session");
            $state.go("loginCompany");
        }

    })


    $scope.company = {
        "address":"",
        "description": "",
        "email": "",
        "password":"",
        "name" : "",
        "website": "",
        "imageUrl": ""
    };

    $scope.loginCred = {
        "email": "",
        "password":""
    }

    $scope.register = function(){
        console.log($scope.company);

        if($scope.company.email == '' || $scope.company.password == ''){
            toastr.error("Provide all the details !!!");
            return;
        }
        else{

            if($scope.company.imageUrl == ""){
                loginService.register("/company/register",$scope.company,function (result) {
                    if(result.statusCode == "200"){
                        toastr.success("You have successfully registered. Verify your account and Login to continue !");
                    }else{
                        toastr.error(result.message);
                    }
                });
            }
            else{
                var file = $scope.company.imageUrl;
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
                        $scope.company.imageUrl = imageUrlS3;
                        var imageUrl = {
                            "imageUrl": imageUrlS3
                        };

                        loginService.register("/company/register",$scope.company,function (result) {
                            if(result.statusCode == "200"){
                                toastr.success("You have successfully registered. Verify your account and Login to continue !");
                            }else{
                                toastr.error(result.message);
                            }
                        });
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

        }

    }

    $scope.login = function()
    {
        if($scope.loginCred.email == '' || $scope.loginCred.password == ''){
            toastr.error("Provide all the details !!!");
            return;
        }
        else{

            loginService.login("/company/signin",$scope.loginCred,function (result) {
                if(result.statusCode == "200"){
                    // toastr.success("You have successfully registered. Verify your account and Login to continue !");
                    $state.go("companyDashboard");
                }else{
                    toastr.error(result.message);
                }
            });
        }
    }
})