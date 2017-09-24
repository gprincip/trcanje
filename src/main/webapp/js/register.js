angular.module('app',[])
	.controller('appController', ['$scope' ,'$http' ,'$window' , function($scope, $http, $window){
		
		$scope.username = null;
		$scope.email = null;
		$scope.password = null;
		$scope.opetpassword = null;
		$scope.ime = null;
		$scope.prezime = null;
		$scope.slika = null;
		
		
		//radi kad se prvi put popunjava, ali ako popunis pa obrises onda $scope.nekopolje vise nije undefined
		
		$scope.registrujKorisnika = function(){
			
			if($scope.username == undefined){ $window.alert("Unesite korisnicko ime"); return; }
			if($scope.username == undefined){ $window.alert("Unesite email"); return; }
			if($scope.password == undefined){ $window.alert("Unesite lozinku"); return; }
			if($scope.opetpassword == undefined){ $window.alert("Unesite ponovljenu lozinku"); return; }
			if($scope.ime == undefined){ $window.alert("Unesite Vase ime"); return; }
			if($scope.prezime == undefined){ $window.alert("Unesite Vase prezime"); return; }
			
			
			if($scope.password == $scope.opetpassword){
			
			var korisnik = {
					"username" : $scope.username,
					"email" : $scope.email,
					"password" : $scope.password,
					"ime" : $scope.ime,
					"prezime" : $scope.prezime,
					"password" : $scope.slika,
					"enabled" : 1
				}
			
			$http.post("services/rest/registrujKorisnika" , korisnik).then(function(response){

			}, function(response){
				
			})
			}

		}

		
		
		
	}]);