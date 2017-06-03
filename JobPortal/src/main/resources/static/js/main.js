
var jobportal =  angular.module('jobportal',['ui.router','rzModule', 'ngFileUpload','toastr','ui.bootstrap','ui.filters','angularUtils.directives.dirPagination']);

/*
jobportal.controller('jobPortalController',function($scope,$http,$state,$location) {


});
*/

jobportal.config(function($stateProvider,$urlRouterProvider){

    $urlRouterProvider.otherwise('/');

    $stateProvider

        .state('/',{
            url:'/',
            templateUrl: '../templates_view/login.html',
            controller:'loginController'

        })

        .state('loginCompany',{

            url:'/companylogin',
            templateUrl: '../templates_view/companylogin.html',
            controller:'companyLoginController'

        })

        .state('LoginRegister', {
            url:'/LoginRegister',
            templateUrl: '/login/LoginRegister.html',
            controller: 'loginController'

        })

        .state('applicantDashboard', {
            url:'/applicantDashboard',
            templateUrl: '../templates_view/applicantDashboard.html',
            controller: 'applicantDashboardController',
            resolve: {
            	data: function($http){
            		return $http({
            			url:"/applicant/allJobs",
            			method:"GET"
            		}).success(function(data){
            			return data;
            		})
            	}
            }
        })

        .state('companyDashboard', {
            url:'/companyDashboard',
            templateUrl: '../templates_view/companyDashboard.html',
            controller: 'companyDashboardController',
            resolve: {
            	data: function($http){
            		return $http({
            			url:"/company/allOpenings",
            			method:"GET"
            		}).success(function(data){
            			return data;
            		})
            	}
            }
        })

        .state('jobSearchPage', {
            url:'/jobSearchPage',
            templateUrl: '../templates_view/jobSearchPage.html',
            controller: 'jobSearchPageController',
            params:{"openings":null,"searchText":null}

        })

        .state('profilePage', {
            url:'/profilePage',
            templateUrl: '../templates_view/profilePage.html',
            controller: 'profilePageController'
        })

        .state('myApplicationsPage', {
            url:'/myApplicationsPage',
            templateUrl: '../templates_view/myApplicationsPage.html',
            controller: 'myApplicationsPageController'
        })

        .state('interestedJobPage', {
            url:'/interestedJobPage',
            templateUrl: '../templates_view/interestedJobPage.html',
            controller: 'interestedJobPageController',
            resolve: {
                data: function($http){
                    return $http({
                        url:"/applicant/allJobs",
                        method:"GET"
                    }).success(function(data){
                        return data;
                    })
                }
            }
        })

        .state('interviewSchedulePage', {
            url:'/interviewSchedulePage',
            templateUrl: '../templates_view/interviewSchedulePage.html',
            controller: 'interviewSchedulePageController',
            resolve: {
                data: function($http){
                    return $http({
                        url:"/applicant/allJobs",
                        method:"GET"
                    }).success(function(data){
                        return data;
                    })
                }
            }
        })

        .state('jobApplicationPage', {
            url:'/jobApplicationPage',
            templateUrl: '../templates_view/jobApplicationPage.html',
            controller: 'jobApplicationPageController',
            params: {"jobObj":null}
        })

        .state('companyPostingsPage', {
            url:'/companyPostingsPage',
            templateUrl: '../templates_view/companyDashboard.html',
            controller: 'companyDashboardController',
            resolve: {
            	data: function($http){
            		return $http({
            			url:"/company/allOpenings",
            			method:"GET"
            		}).success(function(data){
            			return data;
            		})
            	}
            }
        })

        //create posting page
        .state('createPostingPage', {
            url:'/createPostingPage',
            templateUrl: '../templates_view/createPostingPage.html',
            controller: 'createPostingPageController'
        })

        .state('companyProfilePage', {
            url:'/companyProfilePage',
            templateUrl: '../templates_view/companyProfilePage.html',
            controller: 'companyProfilePageController',
            resolve: {
                data: function ($http) {
                    return $http.get("/company/profile")
                        .then(function (response) {
                            return response.data;
                        });
                }
            }

        })

        .state('editPostingPage', {
            url:'/editPostingPage',
            templateUrl: '../templates_view/editPostingPage.html',
            controller: 'editPostingPageController',
            params:{"openingObj":null},
        })


        .state('myApplicationsPageCompleted', {
            url:'/myApplicationsPage/completed',
            templateUrl: '../templates_view/myApplicationsPageCompleted.html',
            controller: 'myApplicationsPageController'
        })

        .state('myApplicationsPageDecisionAvailable', {
            url:'/myApplicationsPage/decisionavailable',
            templateUrl: '../templates_view/myApplicationsPageDecisionAvailable.html',
            controller: 'myApplicationsPageController'
        })

        .state('viewApplicantsPage', {
            url:'/viewApplicantsPage',
            templateUrl: '../templates_view/viewApplicantsPage.html',
            controller: 'viewApplicantsPageController',
            params:{"openingObj":null}
        })

        .state('showInterviews', {
            url:'/showInterviews',
            templateUrl: '../templates_view/showInterviews.html',
            controller: 'showInterviewsController',
            params:{"openingObj":null}
        })





})//End of file

