//html: ng-app="app" , ng-controller = "appController"
//mora $scope.promenljiva ili funkcija
angular.module('app',[])
	.controller('appController', ['$q' ,'$scope' ,'$http' ,'$window' , function($q, $scope, $http, $window){
		
		$scope.aktivnosti = [];
		$scope.aktivnostiSlika = [];
		$scope.ulogovaniKorisnik = null;
		$scope.profilDetalji = null; //ovde se pamti korisnik ciji profil treba da se otvori u posebnoj strani
		$scope.br = 5;
		$scope.trenutnaStrana = 1;
		$scope.ukupanBrojAktivnosti = 0;
		
		$scope.getAktivnosti = function(velicinaStrane, strana, username){
			var q = $q.defer();
			setTimeout(function(){
				
				$http.get("services/rest/getAktivnostiPoStranama2?velicinaStrane="+velicinaStrane+"&strana="+strana+"&username="+username).then(function(response){
					q.resolve(response.data);
				}, 
				function(response){
					console.info("Greska prilikom dobijanja odgovora od zahteva aktivnosti")
				});
				
			},100);
			return q.promise;
			
		}

		
		$scope.getUlogovaniKorisnik = function(){
			var q = $q.defer();
			setTimeout(function(){
				$http.get("services/rest/ulogovaniKorisnik").then(function(response){
					$scope.ulogovaniKorisnik = response.data;
					console.info("getUlogovaniKorisnik: " +$scope.ulogovaniKorisnik);
					q.resolve(response.data);
				}, function(response){
					
				});
			},100);
		return q.promise;
		}
		
		$scope.otvoriProfil = function(username){
			$window.open("/trcanje/services/rest/korisnik/" + username, "_self");
	}
		
		
		$scope.getUlogovaniKorisnikIAktivnosti = function(velicinaStrane, strana){
			var ulogovaniKorisnik = null;
			$scope.getUlogovaniKorisnik().then(function(result1){
				ulogovaniKorisnik = result1;
				$scope.getAktivnosti(velicinaStrane, strana, result1.username).then(function(result2){
					$scope.aktivnosti = result2;
					console.info($scope.aktivnosti);
				
				}).then(function(result3){
					$scope.getBrojAktivnostiKorisnikaIPrijatelja(ulogovaniKorisnik.username);
				});
				
			});
			
		}
		
		$scope.lajkujpomocna = function(username, aktivnost_id){
			var q = $q.defer();
			setTimeout(function(){
			$http.post("services/rest/lajkuj?username=" + username + "&aktivnost_id=" + aktivnost_id).then(function(response){
			q.resolve(response);
			});
			},100);
			return q.promise;
		}
		
		$scope.brojLajkova = function(aktivnost_id){
			var q = $q.defer();
			setTimeout(function(){
				$http.get("services/rest/brojLajkova/" + aktivnost_id).then(function(response){
					q.resolve(response.data);
				});
			},100);
			return q.promise;
		}
		
		$scope.lajkuj = function(username, aktivnost_id){
			var lajk = 0;
				$scope.lajkujpomocna(username, aktivnost_id).then(function(response){
					lajk = response.data;
				}).then(function(response2){
					$scope.brojLajkova(aktivnost_id).then(function(response3){
						
						for(var i=0; i<$scope.aktivnosti.length; i++){
							if($scope.aktivnosti[i].aktivnost.id == aktivnost_id){
								$scope.aktivnosti[i].brojLajkova += lajk;
							}
						}
						
					});
				});
			
			
		}
		
		$scope.ucitajJos = function(){
			$scope.getAktivnosti(3 , $scope.trenutnaStrana+1 , $scope.ulogovaniKorisnik.username).then(function(response){
				$scope.trenutnaStrana++;
				
				for(var i = 0; i<response.length; i++){
					$scope.aktivnosti.push(response[i]);
				}
			}).then(function(response){
				
			});
		}
		
		$scope.getBrojAktivnostiKorisnikaIPrijatelja = function(username){

				$http.get("services/rest/getBrojAktivnostiKorisnikaIPrijatelja?username="+username).then(function(response){
					$scope.ukupanBrojAktivnosti = response.data;
					console.info(response.data);
				});
	
		}
		
		/*Pozivi funkcija*/

		$scope.getUlogovaniKorisnikIAktivnosti(3, 1);
		
	}]);