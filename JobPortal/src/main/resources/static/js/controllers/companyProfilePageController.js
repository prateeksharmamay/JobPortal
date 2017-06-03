/**
 * Created by Prateek on 5/14/2017.
 */
jobportal.controller("companyProfilePageController", function(toastr,$scope,$http,$state,data,Upload,loginService) {
    $scope.companyInfo = data.profile;

    $scope.updateCompanyProfile = function(){
        loginService.login("/company/update",$scope.companyInfo,function (result) {
            if(result.statusCode=200){
                toastr.success("Company Profile Updated !");
                $state.reload();
            }
            else{
                toastr.error("Error in Company Profile Update !");
            }
        })
    };

    $scope.uploadLogo = function(x){
console.log("File name:"+x);
        var file = x;
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
                 $scope.imageUrl1 = {
                    "imageUrl": imageUrlS3
                };
               $scope.showLogoButton = true;

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


    $scope.savePic = function () {

        loginService.login("/company/update",$scope.imageUrl1,function (result) {
            if(result.statusCode=200){
                toastr.success("Company Logo Updated !");

                $state.reload();
            }
            else{
                toastr.error("Error in Company Logo Update !");
            }
        });
    }











});