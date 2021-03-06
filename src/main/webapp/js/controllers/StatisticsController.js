/**
 * Created by Tim on 11/03/14.
 */
var spaceApp = angular.module('spaceApp');
spaceApp.controller("StatisticsController", function ($scope, $translate, StatisticsService) {

    $scope.statistics = {
        winRatio: 5,
        amountOfGames: 10,
        averageAmountOfColoniesPerWin:25,
        averageAmountOfShipsPerWin: 30
    };

    StatisticsService.get(function (data) {
        $scope.statistics = data;
        var DataStats = {
            labels: [$translate('WINRATIO') +": "+ $scope.statistics.winRatio , $translate('NUMBER_OF_GAMES_PLAYED') +": "+$scope.statistics.amountOfGames, $translate('AVERAGE_AMOUNT_OF_COLONIES_PER_WIN')+": " + $scope.statistics.averageAmountOfColoniesPerWin, $translate('AVERAGE_AMOUNT_OF_SHIPS_PER_WIN') +": "+ $scope.statistics.averageAmountOfShipsPerWin],
            datasets: [
                {
                    fillColor : "rgba(255,0,0,0.5)",
                    strokeColor : "rgba(255,0,0,1)",
                    pointColor : "rgba(255,0,0,1)",
                    pointStrokeColor : "#E1E81A",
                    data: [$scope.statistics.winRatio*100, $scope.statistics.amountOfGames, $scope.statistics.averageAmountOfColoniesPerWin, $scope.statistics.averageAmountOfShipsPerWin]
                }
            ]
        };
        var options = {
            scaleOverlay : true,
            scaleShowLabels : true,
            scaleLineColor : "rgba(255,0,0,1)",
            pointLabelFontColor : "#E1E81A",
            angleLineColor : "rgba(255,0,0,1)",
            scaleBackdropColor : "rgba(255,0,0,1)",
            scaleFontColor : "#E1E81A"
        };

        var ctx = document.getElementById("myChart").getContext("2d");
        new Chart(ctx).Bar(DataStats, options);
    })
});
