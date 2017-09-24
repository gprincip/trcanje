angular.module('dodavanjeAktivnosti', []).controller(
		'appController',
		function($scope, $http) {

			$scope.username = null;
			$scope.datum = null;
			$scope.tempo = null;
			$scope.distanca = null;
			$scope.ulogovaniKorisnik = null;

			$scope.dodajAktivnost = function() {

				if ($scope.username != null && $scope.datum != null && $scope.tempo != null && $scope.distanca != null) {

					var aktivnost = {
						"id" : null,
						"username" : $scope.username,
						"datum" : $scope.datum,
						"tempo" : $scope.tempo,
						"distanca" : $scope.distanca
					}

					$http.post("services/rest/dodajAktivnost", aktivnost).then(
							function() {
								
							}, function() {
								
							});

				}
			}

			$scope.getUlogovaniKorisnik = function() {

				$http.get("services/rest/ulogovaniKorisnik").then(function(response) {
							$scope.ulogovaniKorisnik = response.data;

						});

			}

			$scope.getUlogovaniKorisnik();
			
		});