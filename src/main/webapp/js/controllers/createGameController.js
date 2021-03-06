/**
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
var spaceApp = angular.module('spaceApp');
spaceApp.controller("createGameController", function ($scope, $translate, Game, FindPlayer, Profile,$firebase,MailService, Spinner,$rootScope) {


    //haal ingelogde gebruiker op en initialiseer firebase connectie
    Profile.get(function (data, headers) {
        console.log("get api/auth/user");
        $scope.loggedInProfileId = data.profile.profileId;
        $scope.loggedInUserId = data.userId;
        $scope.loggedInUsername = data.username.replace(/ /g, '-');
    }, function (data, headers) {
        $scope.loggedInProfileId = null;
    });


    $scope.gameData = {
        gameName: "", opponentProfileId: ""
    };
    $scope.gameId = "";

    $scope.createGame = function (opponentId, opponentUsername) {
        $scope.gameData.opponentProfileId = opponentId;
        Game.save($scope.gameData, function (data) {
            $scope.gameId = data[0];
            $scope.pushInvite($scope.gameId, opponentUsername, opponentId);

        })
    };

    //push invite
    $scope.pushInvite = function (gameId, opponentUsername, opponentId) {
        opponentUsername = opponentUsername.replace(/ /g, '');
        var firebaseUrl = 'https://vivid-fire-9476.firebaseio.com/invites/' + opponentUsername + opponentId;
        var ref = new Firebase(firebaseUrl);
        $scope.allInvites = $firebase(ref);
        $scope.allInvites.$add({inviter: $scope.loggedInUsername, inviterId: $scope.loggedInProfileId,invited: opponentUsername,invitedId:opponentId, read: false, gameId: gameId, accepted: false});
        $rootScope.loadInvites();
    };

    $scope.selectRandomPlayer = function () {
        FindPlayer.findUserByUserId().get({userId: $scope.loggedInProfileId}, function (data) {
            $scope.gameData.opponentProfileId = data.profile.profileId;
            Game.save($scope.gameData, function (data) {
                $scope.gameId = data[0];
            })
        })
    };

    $scope.foundPlayers = [];
    $scope.receiverMail = {
        emailAddress: ""
    };
    $scope.searchCriteria = 'email';
    $scope.findHasCalled = false;

    $scope.findPlayers = function (searchString) {
        if ($scope.searchCriteria == "email") {
            $scope.findHasCalled = true;
            FindPlayer.findUsersByEmail().get({email: searchString}, function (data) {
                $scope.foundPlayers = data;
            });
        } else {
            FindPlayer.findUsersByUsername().get({username: searchString}, function (data) {
                $scope.foundPlayers = data;
            });
        }
    };
    $scope.mailSent = false;
    $scope.sendMail = function(receiverMailAddress){
        Spinner.spinner.spin(Spinner.target);
        $.blockUI({ message: null });
        $scope.receiverMail = {
            emailAddress: receiverMailAddress
        };
        MailService.save($scope.receiverMail, function(){
            Spinner.spinner.stop();
            $.unblockUI();
            $scope.mailSent = true;
        }, function(){
            $.unblockUI();
            Spinner.spinner.stop();
            $scope.mailSent = false;
        });
    };

    $scope.hideFoundPlayer = function (profileId) {
        return (profileId == $scope.loggedInProfileId);
    };

    $scope.getUserImage = function (image) {
        if (image == null) {
            return "../assets/userimage.png"
        } else {
            return image;
        }
    };

    $scope.findFbfriends = function () {
        FB.ui({app_id: '649165391811913',
            method: 'apprequests',
            message: 'Do you want to play Spacecrack ?'
        });
    };

});